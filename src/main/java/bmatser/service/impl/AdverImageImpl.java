package bmatser.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import bmatser.dao.AdverImageMapper;
import bmatser.service.AdverImageI;

/**
 * 图片广告操作类
 * @author felx
 * 2017-8-3
 */
@Service("imageService")
public class AdverImageImpl implements AdverImageI {
	
	@Autowired
	private AdverImageMapper imageDao;

	/**
	 * 获取图片广告列表
	 * @return
	 * 2017-8-3
	 *
	 */
	@Override
//	@Cacheable(value = "adver", key="'image-'+#positionId")
//	@CacheEvict(value="adver", key="'image-'+#positionId")
	public List findAdverImages(int positionId) {
		return imageDao.findAdverImages(positionId);
	}



	@Override
	public List findAllAdverImages(int positionId) throws Exception {
		// TODO Auto-generated method stub
		return imageDao.findAllAdverImages(positionId);
	}



	@Override
	public List findMallAdverImages(int positionId) {
		return imageDao.findMallAdverImages(positionId);
	}



	@Override
	public List findAppMallAdverImages(int positionId) {
		// TODO Auto-generated method stub
		return imageDao.findAppMallAdverImages(positionId);
	}

}
