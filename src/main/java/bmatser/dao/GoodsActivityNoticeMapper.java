package bmatser.dao;

import java.util.List;
import java.util.Map;

import bmatser.model.GoodsActivityNotice;

public interface GoodsActivityNoticeMapper {

	List findGoodsActivityNotices();
	/**
	 * @author felx
	 * 下期预告
	 */
	List selectUnderlineGoodsActivity();
}