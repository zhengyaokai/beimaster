package bmatser.pageModel;

import bmatser.model.Goods;

public class GoodsPage extends Goods{
	
	private int page = 0;

	private int rows = 10 ;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

}
