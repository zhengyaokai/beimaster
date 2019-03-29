package bmatser.service;

import bmatser.pageModel.FeedBack;
import bmatser.util.LoginInfo;

public interface SuggestBoxServiceI {

	void addSuggest(FeedBack feedBack, LoginInfo loginInfo)  throws Exception;

}
