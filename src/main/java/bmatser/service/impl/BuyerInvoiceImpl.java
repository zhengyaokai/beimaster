package bmatser.service.impl;

import bmatser.dao.OrderGoodsMapper;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bmatser.dao.BuyerInvoiceMapper;
import bmatser.dao.DealerMapper;
import bmatser.pageModel.BuyerInvoicePage;
import bmatser.service.BuyerInvoiceI;
import bmatser.util.DESCoder;
import bmatser.util.FileUpload;

/**
 * 发票信息维护类
 * @author felx
 * 2017-7-24
 */
@Service("invoiceService")
public class BuyerInvoiceImpl implements BuyerInvoiceI {
	
	@Autowired
	private BuyerInvoiceMapper invoiceDao;
	@Autowired
	private DealerMapper dealerDao;

    @Autowired
    private OrderGoodsMapper orderGoodsDao;
	/**
	 * 获取指定用户的发票信息
	 * @param buyerId
	 * @return list
	 * 2017-7-24
	 * @throws Exception 
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List findByBuyerId(Integer buyerId) throws Exception {
		if(buyerId == null){
			throw new Exception("零售商不存在");
		}
		List<Map<String,Object>> list =  invoiceDao.findByBuyerId(buyerId);
		if(list!=null&&list.size()>0){
			for(Map m:list){
				if("2".equals(m.get("invType").toString())){
					m.put("isReview", null);
					Map map = invoiceDao.selectIsWhite(buyerId);
					if(map!=null){
						m.put("isReview", map.get("isReview").toString());
					}
				}
				String recvProvince = m.get("recvProvince")!=null?m.get("recvProvince").toString():"";
                String recvCity = m.get("recvCity")!=null?m.get("recvCity").toString():"";
                String recvArea = m.get("recvArea")!=null?m.get("recvArea").toString():"";
                String recvProvinceName =  orderGoodsDao.findNameById(recvProvince);
                String recvCityName = orderGoodsDao.findNameById(recvCity);
                String recvAreaName = orderGoodsDao.findNameById(recvArea);
                m.put("recvProvinceName",recvProvinceName);
                m.put("recvCityName",recvCityName);
                m.put("recvAreaName",recvAreaName);
			}
		}
		return list;
		
	}

	/**
	 * 设置默认发票
	 * @param id
	 * @param buyerId
	 * @return int
	 * 2017-7-24
	 *
	 */
	@Override
	public int setDefault(Integer id, Integer buyerId) {
		if(id == null || buyerId == null){
			return -1;
		}
		/**先将原有发票设为不默认*/
		invoiceDao.setNoDefault(buyerId);
		return invoiceDao.setDefault(id);
	}

	/**
	 * 新增发票信息
	 * @param invoicePage
	 * @return int
	 * 2017-7-24
	 * @throws Exception 
	 *
	 */
	@Override
	public int add(BuyerInvoicePage invoicePage, MultipartFile authorImg, MultipartFile licenseFile,
			MultipartFile invApplicationFile) throws Exception {
		Integer buyerId = invoicePage.getBuyerId();
		if(buyerId == null){
			throw new Exception("帐号不存在");
		}
		if("2".equals(invoicePage.getInvType())){
			invoicePage.checkData();
		}
		String isDefault = invoicePage.getIsDefault();
		/**如果缺省值为null或空，更改为0*/
		if(StringUtils.isBlank(isDefault)){
			isDefault = "0";
		}
		/**新增收货地址如果默认收货地址，则将原有收货地址默认字段设为不默认*/
		if(StringUtils.equals(isDefault, "1")){
			invoiceDao.setNoDefault(buyerId);
		}
		DESCoder.instanceMobile();
		if(null != invoicePage.getRecvMobile()&&invoicePage.getRecvMobile().contains("*")){
			throw new Exception("收票人手机号码格式不正确");
		}
		if(null != invoicePage.getRegTelphone()&&invoicePage.getRegTelphone().contains("*")){
			throw new Exception("注册电话格式不正确");
		}
		if(StringUtils.isNotBlank(invoicePage.getRecvMobile()) && !invoicePage.getRecvMobile().contains("*")){
			if(invoicePage.getRecvMobile().length()<8){
				throw new RuntimeException("收票人手机长度不正确");
			}
			invoicePage.setRecvMobileSecret(DESCoder.encoder(invoicePage.getRecvMobile()));//对手机号码加密
			invoicePage.setRecvMobile(DESCoder.getNewTel(invoicePage.getRecvMobile()));//隐藏手机号码
		}else{
			invoicePage.setRecvMobile(null);
		}
		if(StringUtils.isNotBlank(invoicePage.getRecvMobile()) && invoicePage.getRecvMobile().length()<8){
			throw new RuntimeException("收票人手机长度不正确");
		}
		if(StringUtils.isNotBlank(invoicePage.getRegTelphone()) && !invoicePage.getRegTelphone().contains("*")){
			if(invoicePage.getRegTelphone().length()<6){
				throw new RuntimeException("注册电话长度不正确");
			}
			invoicePage.setRegTelphoneSecret(DESCoder.encoder(invoicePage.getRegTelphone()));//对电话加密
			invoicePage.setRegTelphone(DESCoder.getNewTel(invoicePage.getRegTelphone()));//隐藏电话
		}else{
			invoicePage.setRegTelphone(null);
		}
		if(authorImg!=null && licenseFile != null && authorImg.getSize()>0 && licenseFile.getSize()>0){
			StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
			String author = FileUpload.getUpload(authorImg, filePath);
			String license = FileUpload.getUpload(licenseFile, filePath);
			invoicePage.setAuthorization(author);
			invoicePage.setClientCompanyLicenseFile(license);
		}
		//上传开票申请书(在customer_qualified这张表)
		if(invApplicationFile!=null && invApplicationFile.getSize()>0){
			Map m = invoiceDao.selectIsWhite(buyerId);
			if(m!=null){
				if("2".equals(m.get("isReview"))){//查到有记录是审核不通过的就更新
					StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
					String qualifications = FileUpload.getUpload(invApplicationFile, filePath);
					invoicePage.setQualifications(qualifications);//开票申请书资质(在customer_qualified这张表)
					invoicePage.setIsReview("0");//开票申请是否审核（0：未审核 1：已审核 2审核不通过）(在customer_qualified这张表)
					invoiceDao.updateCustomerQualified(buyerId,invoicePage.getQualifications(),invoicePage.getIsReview());
				}
			}else{//没查到就新增
				StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
				String qualifications = FileUpload.getUpload(invApplicationFile, filePath);
				invoicePage.setQualifications(qualifications);//开票申请书资质(在customer_qualified这张表)
				//invoicePage.setIsReview("0");//开票申请是否审核（0：未审核 1：已审核 2审核不通过）(在customer_qualified这张表)
				invoiceDao.addCustomerQualified(buyerId,invoicePage.getQualifications());
			}
		}
		if(invoicePage.getDealerName().equals(invoicePage.getInvTitle())){
			invoicePage.setAuthorization("");
			invoicePage.setClientCompanyLicenseFile("");
		}
		invoiceDao.add(invoicePage);
		return invoicePage.getId();
	}

	/**
	 * 修改发票信息
	 * @param invoicePage
	 * @return int
	 * 2017-7-24
	 * @throws Exception 
	 *
	 */
	@Override
	public void edit(BuyerInvoicePage invoicePage) throws Exception {
		Integer id = invoicePage.getId();
		if(id == null){
			throw new Exception("发票不存在");
		}
		invoicePage.checkData();
		if(StringUtils.equals(invoicePage.getIsDefault(), "1")){
			invoiceDao.setNoDefault(invoicePage.getBuyerId());
		}else{
			invoicePage.setIsDefault("0");
		}
		
		DESCoder.instanceMobile();
		if(StringUtils.isNotBlank(invoicePage.getRecvMobile()) && !invoicePage.getRecvMobile().contains("*")){
			invoicePage.setRecvMobileSecret(DESCoder.encoder(invoicePage.getRecvMobile()));//对手机号码加密
			invoicePage.setRecvMobile(DESCoder.getNewTel(invoicePage.getRecvMobile()));//隐藏手机号码
		}else{
			invoicePage.setRecvMobile(null);
		}
		if(StringUtils.isNotBlank(invoicePage.getRegTelphone()) && !invoicePage.getRegTelphone().contains("*")){
			invoicePage.setRegTelphoneSecret(DESCoder.encoder(invoicePage.getRegTelphone()));//对电话加密
			invoicePage.setRegTelphone(DESCoder.getNewTel(invoicePage.getRegTelphone()));//隐藏电话
		}else{
			invoicePage.setRegTelphone(null);
		}
		
		invoiceDao.edit(invoicePage);
	}

	/**
	 * 删除指定ID的发票信息
	 * @param id
	 * @return int
	 * 2017-7-24
	 *
	 */
	@Override
	public int delete(Integer id) {
		if(id == null){
			id = -1;
		}
		return invoiceDao.delete(id);
	}

	/**
	 * 获取默认发票
	 * @param buyerId
	 * @return
	 * 2017-9-24
	 *
	 */
	@Override
	public Map getDefaultByBuyerId(Integer buyerId) {
		Map map = invoiceDao.getDefaultByBuyerId(buyerId);
		if(map == null || map.isEmpty()){
			 map = new HashMap();
		}
		return map;
	}

	/**
	 * 获取发票详情
	 * @param id
	 * @return
	 * 2017-9-25
	 *
	 */
	@Override
	public Map findInvoiceById(Integer id) {
		return invoiceDao.findInvoiceById(id);
	}
	
	/**
	 * 商城新增发票信息
	 * @param invoicePage
	 * @return int
	 * 2016-1-8
	 * @throws Exception 
	 *
	 */
	@Override
	public int addInvoice(BuyerInvoicePage invoicePage) throws Exception {
		Integer buyerId = invoicePage.getBuyerId();
		Integer id = invoicePage.getId();
		if(buyerId == null){
			throw new Exception("帐号不存在");
		}
		if("2".equals(invoicePage.getInvType())){
			invoicePage.checkData();
		}
		if(null!=invoicePage.getIsDefault() && !"".equals(invoicePage.getIsDefault())){
			if(StringUtils.equals(invoicePage.getIsDefault(), "1")){
				invoiceDao.setNoDefault(buyerId);
			}
		}else{
//		String isDefault = "1";
			invoicePage.setIsDefault("1");
			/**新增收货地址如果默认收货地址，则将原有收货地址默认字段设为不默认*/
//		if(StringUtils.equals(isDefault, "1")){
			invoiceDao.setNoDefault(buyerId);
//		}
		}
		DESCoder.instanceMobile();
		
		if(null != invoicePage.getRecvMobile() && !"".equals(invoicePage.getRecvMobile())){
			if(invoicePage.getRecvMobile().contains("*")){
				if(null==id && "2".equals(invoicePage.getInvType())){
					throw new Exception("收票人手机格式错误");
				}else{
					invoicePage.setRecvMobileSecret("");
					invoicePage.setRecvMobile("");
				}
			}else{
				invoicePage.setRecvMobileSecret(DESCoder.encoder(invoicePage.getRecvMobile()));//对手机号码加密
				invoicePage.setRecvMobile(DESCoder.getNewTel(invoicePage.getRecvMobile()));//隐藏手机号码
			}
		}
		if(null != invoicePage.getRegTelphone() && !"".equals(invoicePage.getRegTelphone())){
			if(invoicePage.getRegTelphone().contains("*")){
				if(null==id && "2".equals(invoicePage.getInvType())){
					throw new Exception("注册电话格式错误");
				}else{
					invoicePage.setRegTelphoneSecret("");
					invoicePage.setRegTelphone("");
				}
			}else{
				invoicePage.setRegTelphoneSecret(DESCoder.encoder(invoicePage.getRegTelphone()));//对电话加密
				invoicePage.setRegTelphone(DESCoder.getNewTel(invoicePage.getRegTelphone()));//隐藏电话
			}
		}
		
		if(null==id){
			invoiceDao.add(invoicePage);
		}else{
			invoiceDao.edit(invoicePage);
		}
		return invoicePage.getId();
	}

	@Override
	public int delete(String id, String buyerId) {
		// TODO Auto-generated method stub
		return invoiceDao.deleteByPage(id,buyerId);
	}

	/**
	 * 商城首页获取发票列表 
	 */
	@Override
	public Map getMallInvoiceList(Integer buyerId) {
		Map map = new HashMap();
		List list = invoiceDao.getMallInvoiceList(buyerId);
		if(list !=null &&list.size()!=0){
			 map.put("list", list);
		}
		return map;
	}

	@Override
	public void edit(BuyerInvoicePage invoicePage, MultipartFile authorImg,
			MultipartFile licenseFile,MultipartFile invApplicationFile) throws Exception {
		// TODO Auto-generated method stub

		Integer id = invoicePage.getId();
		if(id == null){
			throw new Exception("发票不存在");
		}
		invoicePage.checkData();
		if(StringUtils.equals(invoicePage.getIsDefault(), "1")){
			invoiceDao.setNoDefault(invoicePage.getBuyerId());
		}else{
			invoicePage.setIsDefault("0");
		}
		
		DESCoder.instanceMobile();
		if(StringUtils.isNotBlank(invoicePage.getRecvMobile()) && !invoicePage.getRecvMobile().contains("*")){
			invoicePage.setRecvMobileSecret(DESCoder.encoder(invoicePage.getRecvMobile()));//对手机号码加密
			invoicePage.setRecvMobile(DESCoder.getNewTel(invoicePage.getRecvMobile()));//隐藏手机号码
		}else{
			invoicePage.setRecvMobile(null);
		}
		if(StringUtils.isNotBlank(invoicePage.getRegTelphone()) && !invoicePage.getRegTelphone().contains("*")){
			invoicePage.setRegTelphoneSecret(DESCoder.encoder(invoicePage.getRegTelphone()));//对电话加密
			invoicePage.setRegTelphone(DESCoder.getNewTel(invoicePage.getRegTelphone()));//隐藏电话
		}else{
			invoicePage.setRegTelphone(null);
		}
		StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
		if(authorImg!=null &&  authorImg.getSize()>0 ){
			String author = FileUpload.getUpload(authorImg, filePath);
			invoicePage.setAuthorization(author);
		}
		if(licenseFile != null && licenseFile.getSize()>0){
			String license = FileUpload.getUpload(licenseFile, filePath);
			invoicePage.setClientCompanyLicenseFile(license);
		}
		//修改上传开票申请书(在customer_qualified这张表)
		if(invApplicationFile!=null && invApplicationFile.getSize()>0){
			Map m = invoiceDao.selectIsWhite(invoicePage.getBuyerId());
			if(m!=null){
				if("2".equals(m.get("isReview"))){//查到有记录是审核不通过的就更新
					String qualifications = FileUpload.getUpload(invApplicationFile, filePath);
					invoicePage.setQualifications(qualifications);//开票申请书资质(在customer_qualified这张表)
					invoicePage.setIsReview("0");//开票申请是否审核（0：未审核 1：已审核 2审核不通过）(在customer_qualified这张表)
					invoiceDao.updateCustomerQualified(invoicePage.getBuyerId(),invoicePage.getQualifications(),invoicePage.getIsReview());
				}
			}else{//没查到就新增
				String qualifications = FileUpload.getUpload(invApplicationFile, filePath);
				invoicePage.setQualifications(qualifications);//开票申请书资质(在customer_qualified这张表)
				//invoicePage.setIsReview("0");//开票申请是否审核（0：未审核 1：已审核 2审核不通过）(在customer_qualified这张表)
				invoiceDao.addCustomerQualified(invoicePage.getBuyerId(),invoicePage.getQualifications());
			}
		}
		if(invoicePage.getDealerName().equals(invoicePage.getInvTitle())){
			invoicePage.setAuthorization("");
			invoicePage.setClientCompanyLicenseFile("");
		}
		invoiceDao.edit(invoicePage);
	
	}

	
	@Override
	public Map findById(String dealerId) {
		
		return dealerDao.findById(dealerId);
	}

	@Override
	public Map selectIsWhite(Integer buyerId) {
		// TODO Auto-generated method stub
		return invoiceDao.selectIsWhite(buyerId);
	}

	@Override
	public void addApplication(String dealerId, MultipartFile invApplicationFile)
			throws Exception {
		//上传开票申请书(在customer_qualified这张表)
		if(invApplicationFile!=null && invApplicationFile.getSize()>0){
			StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
			String qualifications = FileUpload.getUpload(invApplicationFile, filePath);//开票申请书资质(在customer_qualified这张表)
			//invoicePage.setIsReview("0");开票申请是否审核（0：未审核 1：已审核 2审核不通过）(在customer_qualified这张表)
			invoiceDao.addCustomerQualified(Integer.valueOf(dealerId),qualifications);
		}
		
	}
	
	/**
	 * 发票明细列表
	 * @param page
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map getInvoiveDetialList(Integer dealerId, Long orderId,
			String fjsNo, int page, int rows) {
		Map m = new HashMap();
		//查询用户发票明细列表
		List<Map<String,Object>> list = invoiceDao.getInvoiveDetialList(dealerId,orderId,fjsNo,page,rows);
		for(Map map: list){
			BigDecimal decimal = new BigDecimal(map.get("famt")!=null?map.get("famt").toString():"0");
			map.put("famt", decimal.setScale(2,BigDecimal.ROUND_HALF_UP));
		}
		int count = invoiceDao.getInvoiveDetialListCount(dealerId,orderId,fjsNo);
		m.put("list", list);
		m.put("count", count);
		return m;
	}

}
