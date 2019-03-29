package bmatser.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import bmatser.model.JtlRegisterInfo;
import bmatser.pageModel.PageMode;

public interface JtlPageServiceI {

	int getIsJtl(String dealerId);

	JtlRegisterInfo getInfo(String dealerId) throws Exception;

	void addInfo(PageMode mode, MultipartFile businessLicenseFile,
			MultipartFile idCardFile, MultipartFile idCardBackFile) throws Exception;

	Map<String, ?> getRefundInfo(String orderId, String dealerId) throws Exception;

	void addRefundOrder(PageMode model, MultipartFile[] refundImage) throws Exception;

	Map<String, ?> getRefundList(PageMode model) throws Exception;

}
