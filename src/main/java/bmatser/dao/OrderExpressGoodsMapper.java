package bmatser.dao;

import bmatser.model.OrderExpressGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderExpressGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderExpressGoods record);

    int insertSelective(OrderExpressGoods record);

    OrderExpressGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderExpressGoods record);

    int updateByPrimaryKey(OrderExpressGoods record);

    OrderExpressGoods findByOrderExpressIdAndSyncCode(@Param("orderExpressId") String orderExpressId, @Param("id")String syncCode);

    List<OrderExpressGoods> findByOrderExpressId(@Param("orderExpressId")String orderExpressId);
}