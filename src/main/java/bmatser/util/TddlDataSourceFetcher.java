package bmatser.util;
//package bmatser.util;
//
//import java.util.Map;
//
//import javax.sql.DataSource;
//
//import com.taobao.tddl.common.util.DataSourceFetcher;
//import com.taobao.tddl.interact.rule.bean.DBType;
//
//
//public class TddlDataSourceFetcher implements DataSourceFetcher {
//	
//	private Map<String,DataSource> dataSourceMap;
//	
//	@Override
//	public DataSource getDataSource(String key) {
//		return dataSourceMap.get(key);
//	}
//	
//	@Override
//	public DBType getDataSourceDBType(String key) {
//		return null;
//	}
//
//	public Map<String, DataSource> getDataSourceMap() {
//		return dataSourceMap;
//	}
//
//	public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
//		this.dataSourceMap = dataSourceMap;
//	}
//	
//}