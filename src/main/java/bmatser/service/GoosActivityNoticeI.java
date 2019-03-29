package bmatser.service;

import java.util.List;
import java.util.Map;

public interface GoosActivityNoticeI {

	List findGoodsActivityNotices();
	/**
	 * 下期预告
	 */
	Map selectUnderlineGoodsActivity();
}
