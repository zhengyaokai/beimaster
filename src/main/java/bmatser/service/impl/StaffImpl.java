package bmatser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import bmatser.dao.StaffMapper;
import bmatser.service.StaffI;

@Service("staffService")
public class StaffImpl implements StaffI {
	
	@Autowired
	private StaffMapper staffDao;

	@Override
//	@Cacheable(value = "staff", key = "'findEmailById'+#id")
	public String findEmailById(Integer id) {
		return staffDao.findEmail(id);
	}

}
