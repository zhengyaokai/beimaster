package bmatser.service;

import java.util.Map;

public interface GroupActivityI {

	Map addGroupApply(String groupId, String dealerId)  throws Exception;

	void addClick(String groupId)  throws Exception;

}
