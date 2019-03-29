package bmatser.service;

import java.util.Map;

public interface DealerBeanI {

	Map getDealerBeanList(Integer dealerId,int page, int rows);

	Map getFreezeDealerBeanList(Integer dealerId, int page, int rows);

}
