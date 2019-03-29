package bmatser.service;

import bmatser.exception.GdbmroException;
import bmatser.pageModel.PageMode;

public interface UserBlackListServiceI {

	void checkUserAccess(PageMode pageMode) throws Exception,GdbmroException ;

	void saveSearchCount(PageMode pageModel);

}
