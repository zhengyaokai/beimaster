package bmatser.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DealerBalanceMapper {
	/**获取用户余额及使用记录*/
	public List getDealerBalanceByBuyerId(@Param("buyerId")Integer buyerId, @Param("page")int page, @Param("rows")int rows);
	//获取总数
	public Long getDealerBalanceCount(@Param("buyerId")Integer buyerId);
	//获取用户余额
	public BigDecimal getDealerBalance(@Param("buyerId")Integer buyerId);

}
