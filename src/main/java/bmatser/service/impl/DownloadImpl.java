package bmatser.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import bmatser.dao.DownloadMapper;
import bmatser.service.DownloadI;

@Service("downloadService")
public class DownloadImpl implements DownloadI{
	@Autowired
	private DownloadMapper downloadDao;
	@Override
	public Map authorizeBydealerId(String dealerId) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("username", downloadDao.authorizeBydealerId(dealerId));
		return m;
	}

}
