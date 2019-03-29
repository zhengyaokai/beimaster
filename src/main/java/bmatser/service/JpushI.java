package bmatser.service;

import bmatser.pageModel.JpushPage;

public interface JpushI {

	void sendMessage(JpushPage jpushPage) throws Exception ;

}
