package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DealerDesignMapper {

	Map findByDealerAlias(String alias);

	List<Map> findStoreList(@Param("page")int page,@Param("row") int row,@Param("operateScope")String operateScope);

	int findStoreListCount(@Param("operateScope")String operateScope);

	List<Map> getStoreList(@Param("page")int page,@Param("row") int row,@Param("keyword")String keyword);
	int getStoreListCount(@Param("keyword")String keyword);

}
