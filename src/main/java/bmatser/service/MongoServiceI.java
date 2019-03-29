package bmatser.service;
//package bmatser.service;
//
//import java.util.List;
//import java.util.Map;
//
//public interface MongoServiceI<T> {
//
//	/**
//	 * 保存一个对象（在文档不存在时插入，存在时则是更新）
//	 * @param t
//	 */
//	public void save(T t);
//	
//	/**
//	 * 插入一个对象（如果文档不存在则插入，如果文档存在则忽略）
//	 * @param t
//	 */
//	public void insert(T t);
//	
//	/**
//	 * 删除一个对象
//	 * @param t
//	 */
//	public void delete(T t);
//	
//	/**
//	 * 根据主键获取实体对象
//	 * @param clazz 实体对象
//	 * @param id 主键Id
//	 * @return T 实体对象
//	 */
//	public T get(Class<T> clazz, Object id);
//	
//	/**
//	 * 根据指定对象及条件获取单条记录
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @return
//	 */
//	public T get(Class<T> clazz, Map<String, Object> whereParam);
//	
//	/**
//	 * 根据指定对象及条件获取记录集合
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @return List 对象集合
//	 */
//	public List<T> find(Class<T> clazz, Map<String, Object> whereParam, Map<String, Boolean> sortMap);
//	
//	/**
//	 * 查询指定对象的所有的记录
//	 * @param clazz 实体对象
//	 * @return List 对象集合
//	 */
//	public List<T> findAll(Class<T> clazz);
//	
//	/**
//	 * 根据指定对象及条件获取记录集合(带分页)
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @param sortMap 排序
//	 * @param page 查询第几页
//	 * @param rows 每页显示的条数
//	 * @return List 对象集合
//	 */
//	public List<T> find(Class<T> clazz, Map<String, Object> whereParam, Map<String, Boolean> sortMap, Integer page, Integer rows);
//	
//	/**
//	 * 根据指定对象及条件获取记录集合(带分页)
//	 * @param clazz 实体对象
//	 * @param whereParam 查询条件
//	 * @param sortMap 排序
//	 * @param conditions 比较条件
//	 * @param page 查询第几页
//	 * @param rows 每页显示的条数
//	 * @return List 对象集合
//	 */
//	public List<T> find(Class<T> clazz, Map<String, Object> whereParam, Map<String, Object> conditions, Map<String, Boolean> sortMap, Integer page, Integer rows);
//	
//	/**
//	 * 根据指定对象及条件获取记录条数
//	 * @param clazz 实体对象
//	 * @param whereParam 条件参数
//	 * @return Long 记录数
//	 */
//	public Long count(Class<T> clazz, Map<String, Object> whereParam);
//	
//	/**
//	 * 根据指定对象及条件获取记录条数
//	 * @param clazz 实体对象
//	 * @param whereParam 条件参数
//	 * @return Long 记录数
//	 */
//	public Long count(Class<T> clazz, Map<String, Object> whereParam, Map<String, Object> conditions);
//}
