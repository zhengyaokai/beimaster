package bmatser.model;

public class Select {
	
	private String tableName;
	private StringBuffer where = new StringBuffer("WHERE 1=1");
	private StringBuffer query = new StringBuffer("");
	private StringBuffer orderBy = new StringBuffer("");
	private int page;
	private int row;
	
	/**
	 * select sql
	 * @param tableName 表名
	 */
	public Select(String tableName) {
		this.tableName  = tableName;
	}
	
	/**
	 * 查询字段
	 * @param params
	 * @return
	 */
	public Select query(String ... params){
		for (String param : params) {
			if(query.length()!=0){
				query.append("," + param);
			}else{
				query.append(param);
			}
		}
		return this;
	}
	/**
	 * sql输出
	 * @return
	 */
	public String sql(){
		if(query.length()==0)
			query.append("*");
		
		StringBuffer sql = new StringBuffer("SELECT")
				.append(" ")
				.append(query)
				.append(" ")
				.append("FROM")
				.append(" ")
				.append(tableName)
				.append(" ")
				.append(where)
				.append(" ")
				.append(orderBy);
		if(row>0){
			sql.append(" LIMIT "+page*row+","+row);
		}
		return sql.toString();
	}
	/**
	 * 页数从0开始
	 * @param page
	 * @return
	 */
	public Select setPage(int page) {
		this.page = page;
		return this;
	}
	/**
	 * 显示条数
	 * @param row
	 * @return
	 */
	public Select setRow(int row) {
		this.row = row;
		return this;
	}

	public Select where(String param){
		this.where.append(" AND ").append(param);
		return this;
	}
	public Select and(String param){
		this.where.append(" AND ").append(" "+param+" ");
		return this;
	}
	public Select eq(String param){
		this.where.append(" = ").append(" '"+param+"' ");
		return this;
	}
	public Select gt(String param){
		this.where.append(" > ").append(" '"+param+"' ");
		return this;
	}
	public Select lt(String param){
		this.where.append(" < ").append(" '"+param+"' ");
		return this;
	}
	public Select lte(String param){
		this.where.append(" <= ").append(" '"+param+"' ");
		return this;
	}
	public Select gte(String param){
		this.where.append(" >= ").append(" '"+param+"' ");
		return this;
	}
	public Select like(String param,Like like) {
		switch (like) {
		case LEFT:
			this.where.append(" LIKE ").append(" '%"+param+"' ");
			
			break;
		case RIGHT:
			this.where.append(" LIKE ").append(" '"+param+"%' ");
			
			break;
		case ALL:
			this.where.append(" LIKE ").append(" '%"+param+"%' ");
			
			break;
		}
		return this;
	}
	private enum Like{
		/** 左 */
		LEFT,
		/** 右 */
		RIGHT,
		/** 前后 */
		ALL,
	}
	private enum Order{
		/** 从大到小 */
		DESC,
		/** 从小到大 */
		ASC,
	}
	


	public Select order(String param, Order desc) {
		if(this.orderBy.length()==0){
			this.orderBy.append(" ORDER BY ").append(param).append(" "+desc.toString());
		}else{
			this.orderBy.append(" ,").append(param).append(" "+desc.toString());
		}
		return this;
	}
	
	
	public static void main(String[] args) {
		Select select = new Select("order_info");
		select.query("id","seller_id sellerId")
					.where("seller_id").eq("939798")
					.order("id",Order.DESC)
					.order("seller_id", Order.ASC)
					.setRow(8);
		System.out.println(select.sql());
	}
}
