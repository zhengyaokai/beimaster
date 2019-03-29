package bmatser.dao;

import bmatser.model.OrderExpress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderExpressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderExpress record);

    int insertSelective(OrderExpress record);

    OrderExpress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderExpress record);

    int updateByPrimaryKey(OrderExpress record);

    List<OrderExpress> findByOriginalOrderId(@Param("originalId") Long originalId);

    OrderExpress findByOriginalOrderIdAndFexpNoAndFexpcomname(@Param("originalId") Long originalId,@Param("fexpNo") String fexpNo,@Param("fexpcomname") String fexpcomname);

}