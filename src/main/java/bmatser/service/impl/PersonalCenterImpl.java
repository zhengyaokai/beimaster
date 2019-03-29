package bmatser.service.impl;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.PersonalCenterMapper;
import bmatser.model.Dealer;
import bmatser.service.PersonalCenterI;
@Service("personalCenterService")
public class PersonalCenterImpl implements PersonalCenterI{
	 @Autowired
	 private PersonalCenterMapper PersonalCenterDao;
	@Override
	public Map<String, Object> getQrcode(String dealerId) {
		return PersonalCenterDao.getQrcode(dealerId);
	}
	
	@Override
	public Map<String, Object> getInvitedRecord(String dealerId, int page,
			int rows,String startTime,String endTime) {
		Map<String,Object> m = new HashMap<String,Object>();
		List<HashMap> list = PersonalCenterDao.getInvitedRecord(dealerId,page,rows,startTime,endTime);
  		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
  		if(list != null && list.size()!= 0){
  			
  			for(int i=0;i<list.size();i++){
  				HashMap map = list.get(i);
				Timestamp t =(Timestamp) map.get("createTime");
				String createTime = fmt.format(t);
				map.put("createTime", createTime);
				StringBuffer dealerName = new StringBuffer(map.get("dealerName").toString());
					switch (dealerName.length()) {
					case 0:
						break;
					case 1:
						dealerName.append("***");
						break;
					case 2:
						dealerName.replace(1, 2, "***");
						break;
					case 3:
						dealerName.replace(1, 2, "***");
						break;
					default:
						dealerName.replace(1, dealerName.length()-1, "***");
						break;
					}
				 map.put("dealerName", dealerName);	
				 list.set(i, map);
			}
			
		}
  		int count = PersonalCenterDao.getInvitedRecordCount(dealerId,startTime,endTime);
  		m.put("list", list);
  		m.put("count", count);
		return m;
	}

	@Override
	public String getDealerName(String dealerId) {
		
		return PersonalCenterDao.getDealerName(dealerId);
	}

	@Override
	public void saveBankAccount(String dealerId, String accountName,
			String bankName, String card) {
		PersonalCenterDao.saveBankAccount(dealerId,accountName,bankName,card);
		
	}

	@Override
	public Map<String, Object> selectBankAccount(String dealerId) {
		Map<String,Object> m = new HashMap<String,Object>();
		List<HashMap> list = PersonalCenterDao.selectBankAccount(dealerId);		
  		if(list != null && list.size()!= 0){ 			
  			for(int i=0;i<list.size();i++){
  				HashMap map = list.get(i);
  				map.put("id", map.get("id"));
  				map.put("accountName", map.get("accountName"));
  				map.put("bankName", map.get("bankName"));
				StringBuffer card = new StringBuffer(map.get("card").toString());
				card.replace(3, card.length()-4, "***");
				map.put("card", card);	
				list.set(i, map);
			}
			
		}
  		m.put("list", list);
		return m;
	}

	@Override
	public String getCard(String id) {
		// TODO Auto-generated method stub
		return PersonalCenterDao.getCard(id);
	}

	@Override
	public void modifyBankCard(String id, String bankName, String card,
			String dealerId) {
		PersonalCenterDao.modifyBankCard(id,bankName,card,dealerId);		
	}

}
