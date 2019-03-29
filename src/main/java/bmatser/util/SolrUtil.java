package bmatser.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SolrUtil {
	
	private String url;
	private HttpSolrServer solrServer;
	private int maxTotalConnections = 100;
	private int soTimeout = 10000;
	private int connectionTimeout = 5000;
	private int start = 0;
	private int rows = 15;
	private SolrQuery query;
	private boolean isFacet =false;
	private int facetMinCount = 1;
	
	
	public SolrUtil(String url) {
		this.url = url;
		this.query = new SolrQuery();
		this.query.setRows(rows);
		this.setQuery("*:*");
	}
	public void setParam(String key,String value){
		this.query.setParam(key, value);
	}
	public void setParam(String key,boolean value){
		this.query.setParam(key, value);
	}
	
	public QueryResponse getResponse() throws SolrServerException{
		this.toConnect();
		
		return this.solrServer.query(this.query);
	}
	
	private void toConnect(){
		this.solrServer = new HttpSolrServer(this.url);
		this.solrServer.setMaxTotalConnections(100);
		this.solrServer.setSoTimeout(10000);
		this.solrServer.setConnectionTimeout(5000);
	}
	
	public SolrQuery setQuery(String query){
		return this.query.setQuery(query);
	}
	
	/**
	 * 添加过滤
	 * @param key
	 * @param value
	 * @return
	 */
	public SolrQuery addFilterQuery(String key,Object value){
		if(value !=null){
			return this.query.addFilterQuery(key + ":"+ value.toString());
		}
		return this.query;
	}
	/**
	 * 添加过滤
	 * @param param
	 * @return
	 */
	public SolrQuery addFilterQuery(String param){
		return this.query.addFilterQuery(param);
	}
	/**
	 * 添加过滤
	 * @param param
	 * @return
	 */
	public SolrQuery addFilterQuery(Map<String, Object> param){
		for (Entry<String, Object> data : param.entrySet()) {
			if(data.getValue() != null){
				this.query.addFilterQuery(data.getKey() + ":"+ data.getValue().toString());
			}
		}
		return this.query;
	}
	
	public SolrQuery addFacetField(String ...value){
		if(this.isFacet==false){
			this.query.setFacet(true).setFacetMinCount(this.facetMinCount);
			this.isFacet=true;
		}
		this.query.addFacetField(value);
		return this.query;
	}
	/**
	 * 添加排序
	 * @param field
	 * @param order
	 * @return
	 */
	public SolrQuery addSort(String field,ORDER order){
		this.query.addSort(field, order);
		return this.query;
	}
	
	
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpSolrServer getSolrServer() {
		return solrServer;
	}

	public void setSolrServer(HttpSolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.query.setStart(start);
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
		this.query.setRows(rows);
	}

	public SolrQuery getQuery() {
		return query;
	}

	public void setQuery(SolrQuery query) {
		this.query = query;
	}

	public boolean isFacet() {
		return isFacet;
	}

	public SolrQuery setFacet(boolean isFacet) {
		this.isFacet = isFacet;
		return this.query;
	}

	public int getFacetMinCount() {
		return facetMinCount;
	}

	public void setFacetMinCount(int facetMinCount) {
		this.facetMinCount = facetMinCount;
	}
	public void setRequestHandler(String param) {
		this.query.setRequestHandler(param);
		
	}

}
