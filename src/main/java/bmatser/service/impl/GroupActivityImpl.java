package bmatser.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.GrouponActivityMapper;
import bmatser.service.GroupActivityI;
@Service("groupActivityService")
public class GroupActivityImpl implements GroupActivityI {

	@Autowired
	private GrouponActivityMapper grouponActivityDao;
	
	@Override
	public Map addGroupApply(String groupId, String dealerId) throws Exception {
		Map m=new HashMap();
		int isExist = grouponActivityDao.isExistActivity(groupId);
		if(isExist>0){
			int isBuyer = grouponActivityDao.isActivityExistBuyer(groupId, dealerId);
			if(isBuyer==0){
				grouponActivityDao.addGroupBuyer(groupId, dealerId);
				grouponActivityDao.updateGroupAmount(groupId);
			}
			//报名后返回此团购信息 
			m = grouponActivityDao.getGroupBuyInfo(groupId,dealerId);
		}else{
			throw new Exception("活动不存在或未开始");
		}
		return m;

	}

	@Override
	public void addClick(String groupId) throws Exception {
		int isExist = grouponActivityDao.isExistActivity(groupId);
		if(isExist>0){
			grouponActivityDao.updateGroupClick(groupId);
		}
	}

}
