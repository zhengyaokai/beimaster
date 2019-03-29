package bmatser.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.management.RuntimeOperationsException;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

import bmatser.dao.CrmCustomerCategoryMapper;
import bmatser.dao.CrmCustomerManagerMapper;
import bmatser.dao.CrmCustomerMapper;
import bmatser.dao.CustomerLinkmanMapper;
import bmatser.dao.CustomerServiceMapper;
import bmatser.dao.DealerBusinessLicenseMapper;
import bmatser.dao.DealerMapper;
import bmatser.dao.RegisterMapper;
import bmatser.dao.StaffMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.CrmCustomer;
import bmatser.model.CrmCustomerCategory;
import bmatser.model.CrmCustomerService;
import bmatser.model.CustomerLinkman;
import bmatser.model.Dealer;
import bmatser.model.DealerBusinessLicense;
import bmatser.model.Register;
import bmatser.model.Staff;
import bmatser.pageModel.RegisterPage;
import bmatser.service.DealerBusinessLicenseI;
import bmatser.util.Constants;
import bmatser.util.DateTypeHelper;
import bmatser.util.Email;
import bmatser.util.FileTypeUtil;
import bmatser.util.FileUpload;
import bmatser.util.LoginInfo;
import bmatser.util.PaymentIDProduce;
import bmatser.util.PropertyUtil;
import bmatser.util.ResponseMsg;

/**
 * 营业执照
 * @author felx
 * @date 2016.02.23 
 */
@Service("dealerBusinessLicense")
public class DealerBusinessLicenseImpl implements DealerBusinessLicenseI {
	
	@Autowired
	private DealerBusinessLicenseMapper dealerBusinessLicenseDao;
	@Autowired
	private DealerMapper dealerDao;
	@Autowired
	private CrmCustomerMapper crmCustomerDao;
	@Autowired
	private CrmCustomerCategoryMapper crmCustomerCategoryDao;
	@Autowired
	private StaffMapper staffDao;
	@Autowired
	private CrmCustomerManagerMapper customerManagerDao;
	@Autowired
	private CustomerServiceMapper customerServiceDao;
	@Autowired
	private MemcachedClient memcachedClient;
	@Autowired
	private RegisterMapper registerDao;
	@Autowired
	private CustomerLinkmanMapper customerLinkmanDao;


	/**
	 * 营业执照副本电子版上传
	 * @param 
	 * @return
	 */
	@Override
	@Rollback
	public int insert(String pcOrMobile,String staffNo,Integer dealerId,DealerBusinessLicense dealerBusinessLicense,MultipartFile []files,Dealer dealer,CrmCustomer crmCustomer,int type) throws Exception {
		String value=null;
		Dealer de=new Dealer();
		if(StringUtils.isBlank(dealer.getDealerName())){
			throw new RuntimeException("公司名称为空");
		}
		dealerDao.delCrmCustomer(dealerId);
		dealerDao.delDealerName(dealerId);
		int isExist = dealerDao.IsExistName(dealer.getDealerName());
		if(isExist>0){
			throw new RuntimeException("公司名称已经存在");
		}
		
		if(StringUtils.isBlank(dealer.getOperateScope())){
			throw new RuntimeException("经营范围为空");
		}
//		if(pcOrMobile.equals(Constants.mobileIs)&&null==files[0]){
//			throw new RuntimeException("营业执照副本电子版为空");
//		}
		if(StringUtils.isNotBlank(staffNo)){
			RegisterPage registerPage=new RegisterPage();
			registerPage.setStaffNo(staffNo);
			Integer id=staffDao.selectIdByInvitation(staffNo);
			if(null==id){
				throw new RuntimeException("邀请码不正确");
			}
		}else{
			//注册页新增统一社会信用代码 和营业执照注册号两字段的判断 modify 20160602
			if(StringUtils.isBlank(dealerBusinessLicense.getCreditCode()) && StringUtils.isBlank(dealerBusinessLicense.getBusinessLicenseNo())){
				throw new Exception("统一社会信用代码或营业执照注册号不能同时为空!");
			}
		}
        int isCheck = dealerDao.IsExistCheck(dealerId);
		
		if(isCheck>0){
			if (isCheck == 2){
				Map<Integer, String> getPath = upload(files);
			    /*if(pcOrMobile.equals(Constants.mobileIs)&&getPath.isEmpty() && getPath.size()==0){
					throw new RuntimeException("认证失败");
				}*/
				for(Integer key:getPath.keySet()){
					if(key.equals(0)){
						value=getPath.get(key);
						dealerBusinessLicense.setBusinessLicenseFile(value);
					}else if(key.equals(1)){
						value=getPath.get(key);
						dealerBusinessLicense.setTaxCertificateFile(value);
					}else if(key.equals(2)){
						value=getPath.get(key);
						dealerBusinessLicense.setOrganizeCodeFile(value);
					}
				}
				dealerBusinessLicense.setDealerId(dealerId);
				/**
				 * 审核不通过后 提交进行更新
				 * 审核状态（0：待审核 1：审核通过 上架 2：审核不通过 3：已提交资料，未审核 下架）
				 */
				int i=dealerBusinessLicenseDao.update(dealerBusinessLicense);
				dealer.setId(dealerId);
				int j=dealerDao.updateById(dealer);
				
				Register register = registerDao.selectIdByDealerId(dealerId);
				
				if(null!=crmCustomer.getLinkMan()){
					crmCustomer.setLinkMan(URLDecoder.decode(crmCustomer.getLinkMan(), "UTF-8"));
				}
				crmCustomer.setDealerId(dealerId);
				crmCustomer.setName(dealer.getDealerName());
				crmCustomer.setRegisterMobile(register.getMobile());
				crmCustomer.setMobile(register.getMobile());
				crmCustomer.setMobileSecret(register.getMobileSecret());
				crmCustomer.setProvinceId(dealer.getProvinceId().longValue());
				crmCustomer.setCityId(dealer.getCityId().longValue());
				crmCustomer.setStatus("3");
				crmCustomer.setRankId(2);
				//更新客户信息表 
				int k=crmCustomerDao.updateByDealerId(crmCustomer);
				if(k==0){
					throw new RuntimeException("注册失败");
				}
				Integer idValue=crmCustomerDao.selectId(crmCustomer);
				if(null==idValue){
					throw new RuntimeException("认证失败");
				}
				crmCustomer.setId(idValue);
				//if(type == 0 ){手机联系人保存
				CustomerLinkman customerLinkman = new CustomerLinkman();
				customerLinkman.setCustomerId(crmCustomer.getId());
				customerLinkman.setIsDefault("1");
				customerLinkman.setLinkMan(crmCustomer.getLinkMan());
				customerLinkman.setMobile(register.getMobile());
				customerLinkman.setMobileSecret(register.getMobileSecret());
				customerLinkman.setStatus("1");
				customerLinkman.setDutyId(3);
				//更新客户联系人表 
				customerLinkmanDao.updateByCustomerId(customerLinkman);
				//}		
				/*if(null!=dealer){
					crmCustomer.setName(dealer.getDealerName());
				}
				int k=0;
				crmCustomer.setDealerId(dealerId);
				if(StringUtils.isNotBlank(crmCustomer.getName())){
					k=crmCustomerDao.updateById(crmCustomer);
				}*/
			
				String operateScopeArray[]=dealer.getOperateScope().split(",");
				/*CrmCustomer crm=new CrmCustomer();
				crm.setDealerId(dealerId);
				Integer idValue=crmCustomerDao.selectId(crm);//获取crmCustomer表ID
				*/
				
				
				int m=0;
				if(operateScopeArray.length>0){
					for(String str:operateScopeArray){
						if(!StringUtils.isNotBlank(str)){
							throw new RuntimeException("认证失败");
						}
						CrmCustomerCategory crmCustomerCategory=new CrmCustomerCategory();
						crmCustomerCategory.setCustomerId(idValue);
						crmCustomerCategory.setCategoryId(Integer.parseInt(str));
						//更新客户经营类别 
						m=crmCustomerCategoryDao.updateByCustomerId(crmCustomerCategory);
						if(m==0){
							throw new RuntimeException("认证失败");
						}
					}
				}else{
					throw new RuntimeException("认证失败");
				}
				if(StringUtils.isNotBlank(crmCustomer.getName())){
					if(i==0||j==0||k==0){
						throw new RuntimeException("认证失败");
					}
				}else{
					if(i==0||j==0){
						throw new RuntimeException("认证失败");
					}
				}
			    //邀请码  
				if(StringUtils.isNotBlank(staffNo)){
					RegisterPage registerPage=new RegisterPage();
					registerPage.setStaffNo(staffNo);
					Integer id=staffDao.selectIdByInvitation(staffNo);
					if(null==id){
						throw new RuntimeException("邀请码不正确");
					}
					//更新地推数据
					Map<String,Integer> crmCustomerManager=new HashMap<String,Integer>();
					crmCustomerManager.put("customerId", idValue);
					crmCustomerManager.put("managerId", id);
					
					/* 邀请码只要填了就会审核成功  不存在更新操作
					 * 
					 * //更新客户对应经理表 
					int mm=customerManagerDao.updateByCustomerId(crmCustomerManager);*/
					int mm=customerManagerDao.insert(crmCustomerManager);
					if(mm==0){
						throw new RuntimeException("新增客户专员数据失败");
					}
					
					//更新客服数据  start
					List<Staff> staffList = new ArrayList<Staff>();
					if(null != dealer.getOperateScope() && "60".equals(dealer.getOperateScope())){
						//劳保
						staffList = staffDao.selectAutoLBService();
					}else{
						//工控
						staffList = staffDao.selectAutoService();
					}
					Object autoStaffIndexObj = memcachedClient.get("autoStaffIndex");
					int autoStaffIndex=0;
					if(null!=autoStaffIndexObj){
						autoStaffIndex = (int)autoStaffIndexObj;
					}
					if(autoStaffIndex>=staffList.size()){
						autoStaffIndex = 0;
					}
					Staff staff = new Staff();
					int assignCount = 0;
					int loopCount = 1;
					do{
						if(loopCount==staffList.size()){
							break;
						}
						if(autoStaffIndex>=staffList.size()){
							autoStaffIndex = 0;
						}
						staff = staffList.get(autoStaffIndex);
						assignCount = staffDao.selectServiceAssign(staff.getId());
						loopCount++;
						autoStaffIndex++;
					}while(assignCount>60);
					
//							while(assignCount>150){
//								count++;
//								staff = staffList.get(autoStaffIndex+count);
//								assignCount = staffDao.selectServiceAssign(staff.getId());
//								autoStaffIndex++;
//							}
					
					if(null!=staff){
						CrmCustomerService crmCustomerService = new CrmCustomerService();
						crmCustomerService.setCustomerId(idValue);
						crmCustomerService.setServiceId(staff.getId());
						crmCustomerService.setCreateTime(DateTypeHelper.getCurrentTimestamp());
						crmCustomerService.setStatus("1");
						crmCustomerService.setIsAutoAssign("1");
						customerServiceDao.insertSelective(crmCustomerService);
					}
					
					/*邀请码只要填了就会审核成功  不存在更新操作
					 * 
					 * //更新客户所对应的客服 表
					customerServiceDao.updateByCustomerId(crmCustomerService);*/
					//更新缓存中index
					memcachedClient.set("autoStaffIndex", 0, autoStaffIndex);
					//更新客服数据     end
					
					
					//更新客户表状态
					crmCustomer.setStatus("1");
					crmCustomerDao.updateById(crmCustomer);

					//发送邮件
					if(null!=staff.getEmail() && !"".equals(staff.getEmail())){
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("dealerName", dealer.getDealerName());
						Email.sendHtmlMail(data, staff.getEmail(), "自动分配客户--"+dealer.getDealerName(), "autoAssignService.ftl");
					}
					de.setId(dealerId);
					de.setCheckStatus("1");
					de.setCheckTime(new Date());
					de.setCash(10000);
					int mr=dealerDao.updateValue(de);
					if(mr==0){
						throw new RuntimeException("修改审核状态失败");
					}
				}
			    return Integer.parseInt(null==de.getCheckStatus()?"3":de.getCheckStatus().toString());//3是待审核；1是审核通过
			}else{
				throw new RuntimeException("请勿重复提交审核");
			}	
		}
		//Map<Integer, String> getPath = upload(files);
	    /*if(pcOrMobile.equals(Constants.mobileIs)&&getPath.isEmpty() && getPath.size()==0){
			throw new RuntimeException("认证失败");
		}*/
/*		for(Integer key:getPath.keySet()){
			if(key.equals(0)){
				value=getPath.get(key);
				dealerBusinessLicense.setBusinessLicenseFile(value);
			}else if(key.equals(1)){
				value=getPath.get(key);
				dealerBusinessLicense.setTaxCertificateFile(value);
			}else if(key.equals(2)){
				value=getPath.get(key);
				dealerBusinessLicense.setOrganizeCodeFile(value);
			}
		}*/
		dealerBusinessLicense.setDealerId(dealerId);
		//TODO
		int i=dealerBusinessLicenseDao.insert(dealerBusinessLicense);
		dealer.setId(dealerId);
		int j=dealerDao.updateById(dealer);
		
		Register register = registerDao.selectIdByDealerId(dealerId);
		
		if(null!=crmCustomer.getLinkMan()){
			crmCustomer.setLinkMan(URLDecoder.decode(crmCustomer.getLinkMan(), "UTF-8"));
		}
		crmCustomer.setDealerId(dealerId);
		crmCustomer.setName(dealer.getDealerName());
		crmCustomer.setRegisterMobile(register.getMobile());
		crmCustomer.setMobile(register.getMobile());
		crmCustomer.setMobileSecret(register.getMobileSecret());
		crmCustomer.setStatus("3");
		crmCustomer.setRankId(2);
		crmCustomer.setCityId(dealer.getCityId().longValue());
		crmCustomer.setProvinceId(dealer.getProvinceId().longValue());
		int k=crmCustomerDao.insert(crmCustomer);
		if(k==0){
			throw new RuntimeException("注册失败");
		}
		//if(type == 0 ){手机联系人保存
		CustomerLinkman customerLinkman = new CustomerLinkman();
		customerLinkman.setCustomerId(crmCustomer.getId());
		customerLinkman.setIsDefault("1");
		customerLinkman.setLinkMan(crmCustomer.getLinkMan());
		customerLinkman.setMobile(register.getMobile());
		customerLinkman.setMobileSecret(register.getMobileSecret());
		customerLinkman.setStatus("1");
		customerLinkman.setDutyId(3);
		customerLinkmanDao.insertSelective(customerLinkman);
		//}		
		/*if(null!=dealer){
			crmCustomer.setName(dealer.getDealerName());
		}
		int k=0;
		crmCustomer.setDealerId(dealerId);
		if(StringUtils.isNotBlank(crmCustomer.getName())){
			k=crmCustomerDao.updateById(crmCustomer);
		}*/
	
		String operateScopeArray[]=dealer.getOperateScope().split(",");
		/*CrmCustomer crm=new CrmCustomer();
		crm.setDealerId(dealerId);
		Integer idValue=crmCustomerDao.selectId(crm);//获取crmCustomer表ID
		*/
		Integer idValue=crmCustomer.getId();
		if(null==idValue){
			throw new RuntimeException("认证失败");
		}
		
		int m=0;
		if(operateScopeArray.length>0){
			for(String str:operateScopeArray){
				if(!StringUtils.isNotBlank(str)){
					throw new RuntimeException("认证失败");
				}
				CrmCustomerCategory crmCustomerCategory=new CrmCustomerCategory();
				crmCustomerCategory.setCustomerId(idValue);
				crmCustomerCategory.setCategoryId(Integer.parseInt(str));
				m=crmCustomerCategoryDao.insert(crmCustomerCategory);
				if(m==0){
					throw new RuntimeException("认证失败");
				}
			}
		}else{
			throw new RuntimeException("认证失败");
		}
		if(StringUtils.isNotBlank(crmCustomer.getName())){
			if(i==0||j==0||k==0){
				throw new RuntimeException("认证失败");
			}
		}else{
			if(i==0||j==0){
				throw new RuntimeException("认证失败");
			}
		}
	    //邀请码  
		if(StringUtils.isNotBlank(staffNo)){
			RegisterPage registerPage=new RegisterPage();
			registerPage.setStaffNo(staffNo);
			Integer id=staffDao.selectIdByInvitation(staffNo);
			if(null==id){
				throw new RuntimeException("邀请码不正确");
			}
			//增加地推数据
			Map<String,Integer> crmCustomerManager=new HashMap<String,Integer>();
			crmCustomerManager.put("customerId", idValue);
			crmCustomerManager.put("managerId", id);
			int mm=customerManagerDao.insert(crmCustomerManager);
			if(mm==0){
				throw new RuntimeException("新增客户专员数据失败");
			}
			//增加客服数据
			List<Staff> staffList = new ArrayList<Staff>();
			if(null != dealer.getOperateScope() && "60".equals(dealer.getOperateScope())){
				//劳保
				staffList = staffDao.selectAutoLBService();
			}else{
				//工控
				staffList = staffDao.selectAutoService();
			}
			Object autoStaffIndexObj = memcachedClient.get("autoStaffIndex");
			System.out.println("autoStaffIndexObj:"+autoStaffIndexObj);
			System.out.println("staffList.size():"+staffList.size());
			int autoStaffIndex=0;
			if(null!=autoStaffIndexObj){
				autoStaffIndex = (int)autoStaffIndexObj;
			}
			if(autoStaffIndex>=staffList.size()){
				autoStaffIndex = 0;
			}
			Staff staff = new Staff();
			int assignCount = 0;
			int loopCount = 1;
			do{
				if(loopCount==staffList.size()){
					break;
				}
				if(autoStaffIndex>=staffList.size()){
					autoStaffIndex = 0;
				}
				System.out.println("autoStaffIndex:"+autoStaffIndex);
				staff = staffList.get(autoStaffIndex);
				assignCount = staffDao.selectServiceAssign(staff.getId());
				loopCount++;
				autoStaffIndex++;
			}while(assignCount>60);
			
//					while(assignCount>150){
//						count++;
//						staff = staffList.get(autoStaffIndex+count);
//						assignCount = staffDao.selectServiceAssign(staff.getId());
//						autoStaffIndex++;
//					}
			
			if(null!=staff){
				CrmCustomerService crmCustomerService = new CrmCustomerService();
				crmCustomerService.setCustomerId(idValue);
				crmCustomerService.setServiceId(staff.getId());
				crmCustomerService.setCreateTime(DateTypeHelper.getCurrentTimestamp());
				crmCustomerService.setStatus("1");
				crmCustomerService.setIsAutoAssign("1");
				customerServiceDao.insertSelective(crmCustomerService);
			}
			//更新缓存中index
			memcachedClient.set("autoStaffIndex", 0, autoStaffIndex);
			//更新客户表状态
			crmCustomer.setStatus("1");
			crmCustomerDao.updateById(crmCustomer);

			//发送邮件
			if(null!=staff.getEmail() && !"".equals(staff.getEmail())){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("dealerName", dealer.getDealerName());
				Email.sendHtmlMail(data, staff.getEmail(), "自动分配客户--"+dealer.getDealerName(), "autoAssignService.ftl");
			}
			de.setId(dealerId);
			de.setCheckStatus("1");
			de.setCheckTime(new Date());
			de.setCash(10000);
			int mr=dealerDao.updateValue(de);
			if(mr==0){
				throw new RuntimeException("修改审核状态失败");
			}
		}
	    return Integer.parseInt(null==de.getCheckStatus()?"3":de.getCheckStatus().toString());//3是待审核；1是审核通过
	}

	/**
	 * 查询营业执照关联信息
	 * @param HttpServletRequest request,Map mp
	 * @return 
	 */
	@Override
	public List selectByDealerId(String dealerId) {
		return dealerBusinessLicenseDao.selectByDealerId(Integer.parseInt(dealerId));
	}
	
	/**
	 * 营业执照副本电子版上传
	 * @param MultipartFile file
	 * @return
	 */
    public Map<Integer,String> upload(MultipartFile []files) throws JFigException{  
    	String filePath = JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE");
    	Map<Integer,String> mpForm=new LinkedHashMap<Integer,String>(); 
    	String path = null;
    	if(files!=null&&files.length>0){  
            for(int i = 0;i<files.length;i++){  
                MultipartFile file = files[i];
                
                if(null!=file){
                	if(file.getSize()<=1024*1024*2){
                		try {
        					if(FileTypeUtil.isImage(file.getInputStream())){
        						path =getUploadImg(file,new StringBuffer(filePath));
        						mpForm.put(i, path);
        						path =  filePath.substring(filePath.lastIndexOf("/")+1) + path;
        					}
        				}  catch (Exception e) {
        					throw new RuntimeException("图片保存失败");
        				}
                	}else{
                		throw new RuntimeException("第"+i+"个图片大小不能超过2M");
                	}
                	
                }
            }
	}
    	return mpForm;  
    }
    /**
	 * 上传文件到自定义路径
	 * @param file
	 * @param filePath  手机上传图片
	 * @return 文件路径
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String getUploadImg(MultipartFile file,StringBuffer filePath) throws Exception{
		String path = null;
		String myFileName = ".jpg";
    			/**重命名上传后的文件名*/
    			String fileName = PaymentIDProduce.getUUID();
    			/**定义上传路径*/
    			path = "/"+fileName+myFileName;
    			filePath = filePath.append(path);
    			File myFile = new File(filePath.toString());
    			if(!myFile.exists()){
    				myFile.mkdirs();
    			}
    			try {
    	            BufferedImage image = ImageIO.read(file.getInputStream());
    	            if (image != null) {
    	            	file.transferTo(myFile);
    	            	return path;
    	            }else{
    	            	throw new RuntimeException("图片不存在");
    	            }
    	        } catch (IOException e) {
    	        	throw new RuntimeException("图片保存失败");
    	        }
	}

	@Override
	public synchronized void  insert(DealerBusinessLicense license, MultipartFile file,
			MultipartFile fileTwo, MultipartFile fileThree,String type) throws Exception{
		Integer dealerId = license.getDealerId();
		if(license.isEmpty()){
			throw new RuntimeException("信息填写不全，请重新填写");
		}
		if(file==null){
			throw new RuntimeException("请填写营业执照");
		}
		
		if((!FileTypeUtil.isImage(file.getInputStream())) ||  
				(fileTwo != null && fileTwo.getSize()>0 && !FileTypeUtil.isImage(fileTwo.getInputStream())) || 
				(fileThree !=null && fileThree.getSize()>0 &&!FileTypeUtil.isImage(file.getInputStream()))
			){
			throw new RuntimeException("图片文件格式不正确");
		}
		int isCheck = dealerDao.IsExistCheck(dealerId);
		if(isCheck==1 || isCheck==3){
			throw new RuntimeException("请勿重复提交审核");
		}
		dealerDao.delCrmCustomerInfo(dealerId);
		dealerDao.delDealerName(dealerId);
		dealerDao.delDealerLicense(dealerId);
		int isExist = dealerDao.IsExistName(license.getDealerName());
		if(isExist>0){
			throw new RuntimeException("公司名称已经存在");
		}
		Integer staffId =null;
		if(StringUtils.isNotBlank(license.getStaffNo())){
			staffId=staffDao.selectIdByInvitation(license.getStaffNo());
			if(null==staffId){
				throw new RuntimeException("邀请码不正确");
			}
		}
		
		//新增公司信息dealerBusinessLicense表
		StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
        String businessLicenseFile ;
		if(StringUtils.equals(type,"ios")){
             businessLicenseFile = FileUpload.getUploadForIos(file, filePath);
        }else{
             businessLicenseFile = FileUpload.getUpload(file, filePath);
        }
		license.setBusinessLicenseFile(businessLicenseFile);
		if(fileTwo!=null && fileTwo.getSize()>0){
			String taxCertificateFile = FileUpload.getUpload(fileTwo, filePath);
			license.setTaxCertificateFile(taxCertificateFile);
		}
		if(fileThree!=null && fileThree.getSize()>0){
			String organizeCodeFile = FileUpload.getUpload(fileThree, filePath);
			license.setOrganizeCodeFile(organizeCodeFile);
		}



		int i = dealerBusinessLicenseDao.insert(license);
		//END
		
		Dealer d = new Dealer();
		d.setDealerName(license.getDealerName());
		d.setId(license.getDealerId());
		d.setOperateScope(license.getOperateScope());
		int j =dealerDao.updateById(d);
		System.out.println(dealerDao.IsExistCheck(dealerId));
		//新增crm数据
		Register register = registerDao.selectIdByDealerId(dealerId);
		CrmCustomer crm = license.toCustomer();
		crm.setRegisterMobile(register.getMobile());
		crm.setMobile(register.getMobile());
		crm.setMobileSecret(register.getMobileSecret());
		if(null!=staffId){
			crm.setHasInvit(1);
		}
		int k =crmCustomerDao.insert(crm);
		//END
		
		//新增crmLinkMan数据
		CustomerLinkman customerLinkman = crm.toCrmLinkMan();
		customerLinkmanDao.insertSelective(customerLinkman);
		//END
		
		String operateScopeArray[]=license.getOperateScope().split(",");
		Integer idValue=crm.getId();
		if(null==idValue){
			throw new RuntimeException("认证失败");
		}
		
		int m=0;
		if(operateScopeArray.length>0){
			for(String str:operateScopeArray){
				if(!StringUtils.isNotBlank(str)){
					throw new RuntimeException("认证失败");
				}
				CrmCustomerCategory crmCustomerCategory=new CrmCustomerCategory();
				crmCustomerCategory.setCustomerId(idValue);
				crmCustomerCategory.setCategoryId(Integer.parseInt(str));
				m=crmCustomerCategoryDao.insert(crmCustomerCategory);
				if(m==0){
					throw new RuntimeException("认证失败");
				}
			}
		}else{
			throw new RuntimeException("认证失败");
		}
		if(i==0||j==0||k==0){
			throw new RuntimeException("认证失败");
		}
	    //邀请码  
		if(StringUtils.isNotBlank(license.getStaffNo())){
			//增加地推数据
			Map<String,Integer> crmCustomerManager=new HashMap<String,Integer>();
			crmCustomerManager.put("customerId", idValue);
			crmCustomerManager.put("managerId", staffId);
			int mm=customerManagerDao.insert(crmCustomerManager);
			if(mm==0){
				throw new RuntimeException("新增客户专员数据失败");
			}
			//增加客服数据
			List<Staff> staffList = new ArrayList<Staff>();
			if(null != license.getOperateScope() && "60".equals(license.getOperateScope())){
				//劳保
				staffList = staffDao.selectAutoLBService();
			}else{
				//工控
				staffList = staffDao.selectAutoService();
			}
			Object autoStaffIndexObj = memcachedClient.get("autoStaffIndex");
			System.out.println("autoStaffIndexObj:"+autoStaffIndexObj);
			System.out.println("staffList.size():"+staffList.size());
			int autoStaffIndex=0;
			if(null!=autoStaffIndexObj){
				autoStaffIndex = (int)autoStaffIndexObj;
			}
			if(autoStaffIndex>=staffList.size()){
				autoStaffIndex = 0;
			}
			Staff staff = new Staff();
			int assignCount = 0;
			int loopCount = 1;
			int count = Integer.parseInt(PropertyUtil.getPropertyValue("/properties/info.properties", "dealerBusiness.assignCount"));
			do{
				if(loopCount==staffList.size()){
					break;
				}
				if(autoStaffIndex>=staffList.size()){
					autoStaffIndex = 0;
				}
				System.out.println("autoStaffIndex:"+autoStaffIndex);
				staff = staffList.get(autoStaffIndex);
				assignCount = staffDao.selectServiceAssign(staff.getId());
				loopCount++;
				autoStaffIndex++;
			}while(assignCount>count);
			
//					while(assignCount>150){
//						count++;
//						staff = staffList.get(autoStaffIndex+count);
//						assignCount = staffDao.selectServiceAssign(staff.getId());
//						autoStaffIndex++;
//					}
			
			if(null!=staff){
				CrmCustomerService crmCustomerService = new CrmCustomerService();
				crmCustomerService.setCustomerId(idValue);
				crmCustomerService.setServiceId(staff.getId());
				crmCustomerService.setCreateTime(DateTypeHelper.getCurrentTimestamp());
				crmCustomerService.setStatus("1");
				crmCustomerService.setIsAutoAssign("1");
				customerServiceDao.insertSelective(crmCustomerService);
			}
			//更新缓存中index
			memcachedClient.set("autoStaffIndex", 0, autoStaffIndex);
			//更新客户表状态
			/*crm.setStatus("1");
			crmCustomerDao.updateById(crm);*/

			//发送邮件
			if(null!=staff.getEmail() && !"".equals(staff.getEmail())){
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("dealerName", license.getDealerName());
				Email.sendHtmlMail(data, staff.getEmail(), "自动分配客户--"+license.getDealerName(), "autoAssignService.ftl");
			}
		}
	}

	@Override
	public void isRepeatDealerName(String dealerName, String dealerId) {
		int isExist = dealerDao.IsExistDealerName(dealerName,dealerId);
		if(isExist>0){
			throw new RuntimeException("公司名称已存在");
		}
		
	}

	@Override
	public String checkReason(String dealerId) {
		// TODO Auto-generated method stub
		return dealerDao.checkReason(dealerId);
	}

	@Override
	public Object checkInfo(String dealerId) {
		return dealerDao.checkInfo(dealerId);
	}

	@Override
	public Object getRegChannel(String dealerId) {
		// TODO Auto-generated method stub
		return dealerDao.getRegChannel(dealerId);
	}
}
