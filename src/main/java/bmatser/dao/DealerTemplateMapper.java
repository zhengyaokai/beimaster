package bmatser.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface DealerTemplateMapper {
	//通过别名获得店铺使用的模板 20160808 tiw
	List<Map<String,Object>> selectTemplates(@Param("alias")String alias);
	//通过店铺模板id查询模板数据  20160808 tiw
	List selectTemplateDate(@Param("id")String id);

}
