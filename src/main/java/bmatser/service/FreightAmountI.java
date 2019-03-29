package bmatser.service;

import java.util.Map;

public interface FreightAmountI {

	Map getFreightAmount(String cartIds, String buyerAddrId)  throws Exception;

}
