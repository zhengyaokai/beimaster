package bmatser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.OutInterfaceMapper;
import bmatser.pageModel.UnitDebit;
import bmatser.service.OutInterfaceI;
/**
 * 对外接口业务实现类
 * @author felx
 *	@Date 2016-12-2
 *	@description
 */
@Service("outInterfaceService")
public class OutInterfaceImpl implements OutInterfaceI{
	
	@Autowired
	private OutInterfaceMapper outInterfaceDao;

	@Override
	public void addUnitDebit(UnitDebit unitDebit) {
		outInterfaceDao.addUnitDebit(unitDebit);
	}

}
