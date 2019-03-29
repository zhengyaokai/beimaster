package bmatser.service.impl;
//package bmatser.service.impl;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.data.domain.Sort.Order;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import bmatser.service.MongoServiceI;
//
//
//
///**
// * mongo数据库操作实现类
// * @author felx
// * @version 1.0
// * 
// */
//@Service("mongoService")
//public class MongoServiceImpl<T> implements MongoServiceI<T> {
//
//	@Autowired
//	@Qualifier("mongoTemplate")
//	private MongoTemplate mongoTemplate;
//	
//	/**
//	 * 保存一个实体对象（在文档不存在时插入，存在时则是更新）
//	 * @param t
//	 */
//	public void save(T t){
//		mongoTemplate.save(t);
//	}
//	
//	/**
//	 * 插入一个对象（如果文档不存在则插入，如果文档存在则忽略）
//	 * @param t
//	 */
//	public void insert(T t){
//		mongoTemplate.insert(t);
//	}
//	
//	/**
//	 * 删除一个对象
//	 * @param t
//	 */
//	public void delete(T t){
//		mongoTemplate.remove(t);
//	}
//	
//	/**
//	 * 根据主键获取实体对象
//	 * @param clazz 实体对象
//	 * @param id 主键Id
//	 * @return T 实体对象
//	 */
//	public T get(Class<T> clazz, Object id) {
//		return mongoTemplate.findById(id, clazz);
//	}
//	
//	/**
//	 * 根据指定对象及条件获取单条记录
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @return
//	 */
//	public T get(Class<T> clazz, Map<String, Object> whereParam){
//		Query query = new Query();
//		Criteria criteria = new Criteria();
//		if (whereParam != null && whereParam.size() > 0) {
//			for(Map.Entry<String, Object> entry : whereParam.entrySet()){    
//				String key = entry.getKey();
//				if(StringUtils.isNotBlank(key)){
//					criteria.and(key).is(entry.getValue());
//				} 
//			}  
//			query.addCriteria(criteria);
//		}
//		return mongoTemplate.findOne(query, clazz);
//	}
//	
//	/**
//	 * 根据指定对象及条件获取记录集合
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @param sortMap 排序（值为true表示正序asc，false表示倒序desc）
//	 * @return List 对象集合
//	 */
//	public List<T> find(Class<T> clazz, Map<String, Object> whereParam, Map<String, Boolean> sortMap){
//		Query query = new Query();
//		Criteria criteria = new Criteria();
//		if (whereParam != null && whereParam.size() > 0) {
//			for(Map.Entry<String, Object> entry : whereParam.entrySet()){    
//				String key = entry.getKey();
//				if(StringUtils.isNotBlank(key)){
//					criteria.and(key).is(entry.getValue());
//				} 
//			}  
//			query.addCriteria(criteria);
//		}
//		if (sortMap != null && sortMap.size() > 0) {
//			List<Order> sortList = new ArrayList<Order>();
//			for(Map.Entry<String, Boolean> entry : sortMap.entrySet()){    
//				String key = entry.getKey();
//				Boolean value = entry.getValue();
//				if(StringUtils.isNotBlank(key) && value){
//					sortList.add(new Sort.Order(Direction.ASC, key));
//				} else{
//					sortList.add(new Sort.Order(Direction.DESC, key));
//				}
//			}
//			query.with(new Sort(sortList));
//		}
//		return mongoTemplate.find(query, clazz);
//	}
//	
//	/**
//	 * 查询指定对象的所有的记录
//	 * @param clazz 实体对象
//	 * @return List 对象集合
//	 */
//	public List<T> findAll(Class<T> clazz){
//		return mongoTemplate.findAll(clazz);
//	}
//	
//	/**
//	 * 根据指定对象及条件获取记录集合(带分页)
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @param sortMap 排序（值为true表示正序asc，false表示倒序desc）
//	 * @param page 查询第几页
//	 * @param rows 每页显示的条数
//	 * @return List 对象集合
//	 */
//	public List<T> find(Class<T> clazz, Map<String, Object> whereParam, Map<String, Boolean> sortMap, Integer page, Integer rows){
//		Query query = new Query();
//		Criteria criteria = new Criteria();
//		if (whereParam != null && whereParam.size() > 0) {
//			for(Map.Entry<String, Object> entry : whereParam.entrySet()){
//				String key = entry.getKey();
//				if(StringUtils.isNotBlank(key)){
//					criteria.and(key).is(entry.getValue());
//				} 
//			}  
//			query.addCriteria(criteria);
//		}
//		if (sortMap != null && sortMap.size() > 0) {
//			List<Order> sortList = new ArrayList<Order>();
//			for(Map.Entry<String, Boolean> entry : sortMap.entrySet()){    
//				String key = entry.getKey();
//				Boolean value = entry.getValue();
//				if(StringUtils.isNotBlank(key) && value){
//					sortList.add(new Sort.Order(Direction.ASC, key));
//				} else{
//					sortList.add(new Sort.Order(Direction.DESC, key));
//				}
//			}
//			query.with(new Sort(sortList));
//		}
//		query.skip(page*rows);
//		query.limit(rows);
//		return mongoTemplate.find(query, clazz);
//	}
//	
//	/**
//	 * 根据指定对象及条件获取记录集合(带分页)
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @param sortMap 排序（值为true表示正序asc，false表示倒序desc）
//	 * @param conditions 比较条件
//	 * @param page 查询第几页
//	 * @param rows 每页显示的条数
//	 * @return List 对象集合
//	 */
//	public List<T> find(Class<T> clazz, Map<String, Object> whereParam, Map<String, Object> conditions, Map<String, Boolean> sortMap, Integer page, Integer rows){
//		Query query = new Query();
//		Criteria criteria = new Criteria();
//		if (whereParam != null && whereParam.size() > 0) {
//			for(Map.Entry<String, Object> entry : whereParam.entrySet()){
//				String key = entry.getKey();
//				if(StringUtils.isNotBlank(key)){
//					if(!conditions.containsKey(key)){
//						criteria.and(key).is(entry.getValue());
//					} else{
//						String condition = conditions.get(key).toString();
//						if(condition.equals("=")){
//							criteria.and(key).is(entry.getValue());
//						} else if(condition.equals(">")){
//							criteria.and(key).gt(entry.getValue());
//						} else if(condition.equals(">=")){
//							criteria.and(key).gte(entry.getValue());
//						} else if(condition.equals("<")){
//							criteria.and(key).lt(entry.getValue());
//						} else if(condition.equals("<=")){
//							criteria.and(key).lte(entry.getValue());
//						} else if(condition.equals("like")){
//							Pattern pattern = Pattern.compile("^.*" + entry.getValue() + ".*$", Pattern.MULTILINE);
//							criteria.and(key).regex(pattern);
//						}
//						
//					}
//				} 
//			}  
//			query.addCriteria(criteria);
//		}
//		if (sortMap != null && sortMap.size() > 0) {
//			List<Order> sortList = new ArrayList<Order>();
//			for(Map.Entry<String, Boolean> entry : sortMap.entrySet()){    
//				String key = entry.getKey();
//				Boolean value = entry.getValue();
//				if(StringUtils.isNotBlank(key) && value){
//					sortList.add(new Sort.Order(Direction.ASC, key));
//				} else{
//					sortList.add(new Sort.Order(Direction.DESC, key));
//				}
//			}
//			query.with(new Sort(sortList));
//		}
//		query.skip(page*rows);
//		query.limit(rows);
//		return mongoTemplate.find(query, clazz);
//	}
//	
//	/**
//	 * 根据指定对象及条件获取记录条数
//	 * @param clazz 实体对象
//	 * @param whereParam 条件参数
//	 * @return Long 记录数
//	 */
//	public Long count(Class<T> clazz, Map<String, Object> whereParam){
//		Query query = new Query();
//		Criteria criteria = new Criteria();
//		if (whereParam != null && whereParam.size() > 0) {
//			for(Map.Entry<String, Object> entry : whereParam.entrySet()){
//				String key = entry.getKey();
//				if(StringUtils.isNotBlank(key)){
//					criteria.and(key).is(entry.getValue());
//				} 
//			}  
//			query.addCriteria(criteria);
//		}
//		return mongoTemplate.count(query, clazz);
//	}
//	
//	/**
//	 * 根据指定对象及条件获取记录条数
//	 * @param clazz 实体对象
//	 * @param whereParam 条件参数
//	 * @return Long 记录数
//	 */
//	public Long count(Class<T> clazz, Map<String, Object> whereParam, Map<String, Object> conditions){
//		Query query = new Query();
//		Criteria criteria = new Criteria();
//		if (whereParam != null && whereParam.size() > 0) {
//			for(Map.Entry<String, Object> entry : whereParam.entrySet()){
//				String key = entry.getKey();
//				if(StringUtils.isNotBlank(key)){
//					if(!conditions.containsKey(key)){
//						criteria.and(key).is(entry.getValue());
//					} else{
//						String condition = conditions.get(key).toString();
//						if(condition.equals("=")){
//							criteria.and(key).is(entry.getValue());
//						} else if(condition.equals(">")){
//							criteria.and(key).gt(entry.getValue());
//						} else if(condition.equals(">=")){
//							criteria.and(key).gte(entry.getValue());
//						} else if(condition.equals("<")){
//							criteria.and(key).lt(entry.getValue());
//						} else if(condition.equals("<=")){
//							criteria.and(key).lte(entry.getValue());
//						} else if(condition.equals("like")){
//							Pattern pattern = Pattern.compile("^.*" + entry.getValue() + ".*$", Pattern.MULTILINE);
//							criteria.and(key).regex(pattern);
//						}
//					}
//				} 
//			}  
//			query.addCriteria(criteria);
//		}
//		return mongoTemplate.count(query, clazz);
//	}
//	
//}
