package bmatser.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DealerCategoryMapper {

	List findPatentCateByAlias(String alias);

	List findAllCateByAlias(String alias);

	List findCateByParentId(@Param("alias")String alias,@Param("parentId") String cateId);

}
