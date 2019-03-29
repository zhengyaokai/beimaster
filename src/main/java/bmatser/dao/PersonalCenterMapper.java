package bmatser.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import bmatser.model.Dealer;

public interface PersonalCenterMapper {

	Map<String, Object> getQrcode(@Param("dealerId")String dealerId);

	List<HashMap> getInvitedRecord(@Param("dealerId") String dealerId,@Param("page") int page,@Param("rows") int rows,@Param("startTime")  String startTime,@Param("endTime") String endTime);

	int getInvitedRecordCount(@Param("dealerId") String dealerId,@Param("startTime") String startTime,@Param("endTime") String endTime);

	String getDealerName(@Param("dealerId") String dealerId);

	void saveBankAccount(@Param("dealerId")String dealerId,@Param("accountName") String accountName,@Param("bankName") String bankName,@Param("card") String card);

	List<HashMap> selectBankAccount(@Param("dealerId")String dealerId);

	String getCard(@Param("id")String id);

	void modifyBankCard(@Param("id") String id,@Param("bankName") String bankName,@Param("card") String card,@Param("dealerId") String dealerId);

}
