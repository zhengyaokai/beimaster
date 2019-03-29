package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DealerBeanMapper {
	//查询用户工币列表
	List getDealerBeanList(@Param("dealerId")Integer dealerId,@Param("page") int page,@Param("rows") int rows);
	//查询用户可用工币
	Long getBean(@Param("dealerId") Integer dealerId);
	//查询用户已冻结工币
	Long getFreezeBean(@Param("dealerId") Integer dealerId);
	//查询用户工币列表总条数
	int getDealerBeanCount(@Param("dealerId") Integer dealerId);
	//查询用户冻结工币列表
	List getFreezeDealerBeanList(@Param("dealerId")Integer dealerId,@Param("page") int page,@Param("rows") int rows);
	//查询用户冻结工币列表总条数
	int getFreezeDealerBeanCount(@Param("dealerId") Integer dealerId);



}
