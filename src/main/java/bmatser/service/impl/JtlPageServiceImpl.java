package bmatser.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.GoldMantisMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.JtlRegisterInfo;
import bmatser.model.RefundOrder;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.JtlPageServiceI;
import bmatser.util.FileTypeUtil;
import bmatser.util.FileUpload;
@Service("jtlPageService")
public class JtlPageServiceImpl implements JtlPageServiceI{
	
	@Autowired
	private GoldMantisMapper goldMantisDao;

	@Override
	public int getIsJtl(String dealerId) {
		return goldMantisDao.getJtlStatus(dealerId);
	}

	@Override
	public JtlRegisterInfo getInfo(String dealerId) throws Exception {
		if(getIsJtl(dealerId)==0 || getIsJtl(dealerId) == 3){
			return goldMantisDao.getJtlRegisterInfo(dealerId);
		}else{
			throw new Exception("审核已提交");
		}
	}

	@Override
	public void addInfo(PageMode mode, MultipartFile businessLicenseFile,
			MultipartFile idCardFile, MultipartFile idCardBackFile) throws Exception {
		String dealerId = mode.getDealerId(Channel.SAAS);
		//String businessLicenseFileAddr = null;
		//String idCardFileAddr = null;
		if(getIsJtl(dealerId)==0 || getIsJtl(dealerId) == 3){
			if(FileTypeUtil.isImage(businessLicenseFile.getInputStream())){
				StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
				mode.put("businessLicenseFile", FileUpload.getUpload(businessLicenseFile, filePath));
				//businessLicenseFileAddr = FileUpload.getUpload(businessLicenseFile, filePath);
			}else{
				throw new Exception("营业执照图片格式不能识别");
			}
			if(FileTypeUtil.isImage(idCardFile.getInputStream())){
				StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
				mode.put("idCardFile", FileUpload.getUpload(idCardFile, filePath));
				//idCardFileAddr = FileUpload.getUpload(idCardFile, filePath);
			}else{
				throw new Exception("身份证格式不能识别");
			}
			if(FileTypeUtil.isImage(idCardBackFile.getInputStream())){
				StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
				mode.put("idCardBackFile", FileUpload.getUpload(idCardBackFile, filePath));
				//idCardFileAddr = FileUpload.getUpload(idCardFile, filePath);
			}else{
				throw new Exception("身份证背面格式不能识别");
			}
			JtlRegisterInfo info = mode.toJavaObject(JtlRegisterInfo.class);
			if(info.vMobile()){
				info.encryptMobile();
			}else{
				throw new Exception("手机格式不正确");
			}
			if(goldMantisDao.isExistBusinessLicense(dealerId)>0){
				goldMantisDao.updateJtlRegisterInfo(info,dealerId);
			}else{
				goldMantisDao.addJtlRegisterInfo(info,dealerId);
			}
		}else{
			throw new Exception("请勿重复提交");
		}
		
	}

	@Override
	public Map<String, ?> getRefundInfo(String orderId, String dealerId) throws Exception {
		Map<String , Object> map = goldMantisDao.getRefundInfo(orderId,dealerId);
		if(map!=null){
			map.put("goods", goldMantisDao.getRefundGoods(orderId));
		}else{
			throw new Exception("订单不支持退货或重复提交");
		}
		return map;
	}

	@Override
	public void addRefundOrder(PageMode model, MultipartFile[] refundImage) throws Exception {
		String dealerId = model.getDealerId(Channel.SAAS);
		String orderId = model.getStringValue("orderId");
		Map<String , Object> map = goldMantisDao.getRefundInfo(orderId,dealerId);
		StringBuffer filePath = new StringBuffer(JFig.getInstance().getValue("UPLOAD_PATH", "LICENSE_IMAGE"));
		if(map!=null){
			String refundReason = model.getStringValue("refundReason");
			String remark = model.getStringValue("remark");
			String refundType = "3".equals(model.getStringValue("refundType"))?"3":"1";
			RefundOrder refund = new RefundOrder();
			refund.setOrderId(orderId);
			refund.setRefundReason(refundReason);
			refund.setRefundType(refundType);
			refund.setRemark(remark);
			goldMantisDao.updateRefundToDel(orderId);
			goldMantisDao.addRefundOrder(refund);
			goldMantisDao.updateOrderToRefund(orderId);
			for (MultipartFile image : refundImage) {
				if(image!= null&&image.getSize()>0){
					if(FileTypeUtil.isImage(image.getInputStream())){
						String s = FileUpload.getUpload(image, filePath);
						goldMantisDao.addRefundImage(refund.getId(),s);
					}else{
						throw new GdbmroException(-1,"图片格式不能识别");
					}
				}
			}
		}else{
			throw new Exception("订单不支持退货或重复提交");
		}
	}

	@Override
	public Map<String, ?> getRefundList(PageMode model) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String dealerId =model.getDealerId(Channel.SAAS);
		Map<String, Object> query = new HashMap<>();
		if(model.contains("page","type","status")){
			int page = model.getIntValue("page")-1;
			query.put("page", page*10);
			query.put("row", 10);
			query.put("type", model.getIntValue("type"));
			query.put("status", model.getIntValue("status"));
			query.put("dealerId", dealerId);
		}else{
			throw new Exception("参数不能为空");
		}
		List<Map<String, Object>> list= goldMantisDao.getRefundList(query);
		for (Map<String, Object> data : list) {
			String orderId = data.get("orderId").toString();
			data.put("goods", goldMantisDao.getRefundGoods(orderId));
		}
		map.put("list", list);
		map.put("row", goldMantisDao.getRefundListCount(query));
		return map;
	}
	


}
