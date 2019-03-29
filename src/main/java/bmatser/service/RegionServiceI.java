package bmatser.service;

import java.util.Map;

import bmatser.pageModel.Adress;

public interface RegionServiceI {
	
	
	Adress getRegionbyOrderId(String id) throws Exception;

}
