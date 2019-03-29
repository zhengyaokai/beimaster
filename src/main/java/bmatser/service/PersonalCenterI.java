package bmatser.service;

import java.util.Map;

public interface PersonalCenterI {

	Map<String, Object> getQrcode(String dealerId);

	Map<String, Object> getInvitedRecord(String dealerId, int page, int rows,String startTime,String endTime);

	String getDealerName(String dealerId);

	void saveBankAccount(String dealerId, String accountName, String bankName,String card);

	Map<String, Object> selectBankAccount(String dealerId);

	String getCard(String id);

	void modifyBankCard(String id, String bankName, String card, String dealerId);

}
