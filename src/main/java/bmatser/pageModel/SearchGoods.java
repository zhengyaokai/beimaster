package bmatser.pageModel;

import com.google.common.collect.Lists;
import bmatser.dao.CategoryMapper;
import bmatser.dao.GoodsMapper;
import bmatser.model.ModelSelectionCategory;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.SolrParams;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

@ApiModel(value="商品搜索",description="商品搜索参数")
@ComponentScan
public class SearchGoods {
    @Autowired
    CategoryMapper categoryMapper;

	@ApiModelProperty(value="关键字")
	private String keyword;
	@ApiModelProperty(value="商家ID")
	private Integer dealerId;
	@ApiModelProperty(value="品牌ID")
	private Integer brandId;
	@ApiModelProperty(value="分类ID")
	private Integer categoryId;
	@ApiModelProperty(value="分类名称")
	private String categoryName;
	@ApiModelProperty(value="选型分类ID")
	private Integer modelSelectionCateId;
	@ApiModelProperty(value="属性ID")
	private String attrValueIds;
	@ApiModelProperty(value="排序参数")
	private String sortFields;
	@ApiModelProperty(value="正序或倒序")
	private String sortFlags;
	@ApiModelProperty(value="商品ID")
	private String advantage;

	private String channel;

	private String type;
	@ApiModelProperty(value="页数")
	private int page;
	@ApiModelProperty(value="行数")
	private int rows=10;
	
	private String[] keys;
	
	private Integer[] nums;
	
	
	public SearchGoods setPathVariable(String type,int page,int rows){
		this.type = type;
		this.page = page > 15?15 : page;
		this.rows = rows;
		return this;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getDealerId() {
		return dealerId;
	}
	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getAttrValueIds() {
		return attrValueIds;
	}
	public void setAttrValueIds(String attrValueIds) {
		this.attrValueIds = attrValueIds;
	}
	public String getSortFields() {
		return sortFields;
	}
	public void setSortFields(String sortFields) {
		this.sortFields = sortFields;
	}
	public String getSortFlags() {
		return sortFlags;
	}
	public void setSortFlags(String sortFlags) {
		this.sortFlags = sortFlags;
	}
	public String getAdvantage() {
		return advantage;
	}
	public void setAdvantage(String advantage) {
		this.advantage = advantage;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
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
	
	public String[] getKeys() {
		return keys;
	}
	public void setKeys(String[] keys) {
		this.keys = keys;
	}
	public Integer[] getNums() {
		return nums;
	}
	public void setNums(Integer[] nums) {
		this.nums = nums;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public Integer getModelSelectionCateId() {
		return modelSelectionCateId;
	}
	public void setModelSelectionCateId(Integer modelSelectionCateId) {
		this.modelSelectionCateId = modelSelectionCateId;
	}
	/**
	 * 根据参数数据decode转码
	 */
	public SearchGoods decode() {
		try {
			this.keyword = decodeString(this.keyword,this.keyword);
			this.attrValueIds = decodeString(this.attrValueIds,this.attrValueIds);
			switch (type) {
			case "goods_list":
				this.sortFields = decodeString(this.sortFields,"salesVolume");
				this.sortFlags = decodeString(this.sortFlags,"1");
				break;
			case "face":
				this.sortFields = decodeString(this.sortFields,"image");
				this.sortFlags = decodeString(this.sortFlags,"1");
				break;
			case "pcface":
				this.sortFields = decodeString(this.sortFields,"salesVolume,image");
				this.sortFlags = decodeString(this.sortFlags,"1,1");
				break;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * decode
	 * @param param 转码值
	 * @param defaultParam 转码失败的值
	 * @return
	 * @throws Exception
	 */
	private String decodeString(String param,String defaultParam) throws Exception{
		return StringUtils.isNotBlank(param)?URLDecoder.decode(param, "UTF-8"):defaultParam;
	}
	/**
	 * 解析商品属性
	 * @return
	 */
	public Integer[] parsingAttrValueIds(){
		Integer[] attrValueIdInt = null;
		if(StringUtils.isNoneBlank(this.attrValueIds)){
			String[] attrValueIdStr = this.attrValueIds.split(",");
			attrValueIdInt = new Integer[attrValueIdStr.length];
			for(int i = 0,size=attrValueIdStr.length; i < size; i++){
				attrValueIdInt[i] = Integer.parseInt(attrValueIdStr[i].trim());
			}
		}
		return attrValueIdInt;
	}

	
	public boolean isSaas(){
		if(null != this.channel && "pc".equals(this.channel)){
			return true;
		}
		return false;
	}
	/**
	 * 判断查询数据是否为空
	 * @return
	 */
	public boolean isNullBySaas() {
		if(StringUtils.isBlank(this.keyword) 
				&& this.brandId==null 
				&& this.categoryId==null 
				&& this.attrValueIds==null
				&& this.modelSelectionCateId==null){
			
			return true;
		}
		return false;
	}
	/**
	 * sass(Query)
	 * @return
	 */
	public SolrParams getSaasQuery() { 
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		Integer[] attrValueIds = this.parsingAttrValueIds();
		String[] sortFieldArr = null;
		Integer[] sortFlagInt = null;
		if(StringUtils.isNoneBlank(sortFields)){
			sortFieldArr = sortFields.split(",");
			String[] sortFlagArr = sortFlags.split(",");
			int sortFieldsize = sortFieldArr.length; 
			sortFlagInt = new Integer[sortFieldsize];
			for(int i = 0; i < sortFieldsize; i++){
				sortFlagInt[i] = Integer.parseInt(sortFlagArr[i].trim());
			}
		}
		if(StringUtils.isBlank(keyword)){
			params.append("*:*");
		} else{
			keyword = getEscapeChar(keyword);
			try {
				keyword = URLDecoder.decode(keyword,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			params.append("(");
			params.append("text:"+keyword + "");
			params.append(")");
			keyword = keyword.replace("-", "");
			params.append(" OR (");
			params.append("text:'"+keyword+"'");
			params.append(")");
		}
		query = new SolrQuery();
		query.setQuery(params.toString());
		/**品牌过滤*/
		if(brandId != null && brandId > 0){
			query.addFilterQuery("brandId:"+brandId);
		}
		/**分类过滤*/
		if(categoryId != null && categoryId > 0){
			query.addFilterQuery("categoryId:"+categoryId+"*");
		}
		
		
		
		/**选型过滤*/
		if(attrValueIds != null){
			for(Integer attrValueId : attrValueIds){
				query.addFilterQuery("attrValueId:"+attrValueId);
			}
		}
		if(this.advantage!=null&&"0".equals(this.advantage)){
			query.addFilterQuery("stock:[1 TO *]");
		}
		if(sortFieldArr != null && sortFieldArr.length > 0){
			int sortFielsCount = sortFieldArr.length;
			for(int i = 0; i < sortFielsCount; i++){
				if(sortFlagInt[i]==0){
					query.addSort(sortFieldArr[i], SolrQuery.ORDER.asc);
				} else{
					query.addSort(sortFieldArr[i], SolrQuery.ORDER.desc);
				}
			}
		}
		query.setStart((page-1)*rows);
		query.setRows(rows);
		
//		query.setFacet(true).setFacetMinCount(1).setFacetLimit(100);
		query.setFacet(true).setFacetMinCount(1);
		query.addFacetField("brandId");
		query.addFacetField("categoryId");
		if(categoryId != null){
			query.addFacetField("attrValueId");
		}
		return query;
	}
	/**
	 * sass(Query)
	 * @return
	 */
	public SolrParams getModelSelectionSaasQuery() { 
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		Integer[] attrValueIds = this.parsingAttrValueIds();
		String[] sortFieldArr = null;
		Integer[] sortFlagInt = null;
		if(StringUtils.isNoneBlank(sortFields)){
			sortFieldArr = sortFields.split(",");
			String[] sortFlagArr = sortFlags.split(",");
			int sortFieldsize = sortFieldArr.length; 
			sortFlagInt = new Integer[sortFieldsize];
			for(int i = 0; i < sortFieldsize; i++){
				sortFlagInt[i] = Integer.parseInt(sortFlagArr[i].trim());
			}
		}
			params.append("*:*");
		query = new SolrQuery();
		query.setQuery(params.toString());
        /**品牌过滤*/
        if(brandId != null && brandId > 0){
            query.addFilterQuery("brandId:"+brandId);
        }
		/**分类过滤*/
		if(modelSelectionCateId != null && modelSelectionCateId > 0){
            query.addFilterQuery("selectionCateId:"+modelSelectionCateId+"*");
		}
		/**选型过滤*/
		if(attrValueIds != null){
			for(Integer attrValueId : attrValueIds){
				query.addFilterQuery("attrValueId:"+attrValueId);
			}
		}
		if(this.advantage!=null&&"0".equals(this.advantage)){
			query.addFilterQuery("stock:[1 TO *]");
		}
		if(sortFieldArr != null && sortFieldArr.length > 0){
			int sortFielsCount = sortFieldArr.length;
			for(int i = 0; i < sortFielsCount; i++){
				if(sortFlagInt[i]==0){
					query.addSort(sortFieldArr[i], SolrQuery.ORDER.asc);
				} else{
					query.addSort(sortFieldArr[i], SolrQuery.ORDER.desc);
				}
			}
		}
		query.setStart((page-1)*rows);
		query.setRows(rows);
		
//		query.setFacet(true).setFacetMinCount(1).setFacetLimit(100);
		query.setFacet(true).setFacetMinCount(1);
		query.addFacetField("attrValueId");
		return query;
	}

	public  String getEscapeChar(String input){
		StringBuffer sb = new StringBuffer();
		String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)\\s]";
		Pattern pattern =Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			matcher.appendReplacement(sb, "\\\\"+matcher.group());
		}
		matcher.appendTail(sb);
		return sb.toString();
		
	}
	public boolean isNullByFace() {
		if(StringUtils.isBlank(this.keyword) 
				&&  this.brandId==null 
				&& this.categoryId==null 
				&& this.attrValueIds==null){
			
			return true;
		}
		return false;
	}






	public SolrParams getFaceQuery() {
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		Integer[] attrValueIds = this.parsingAttrValueIds();
		String[] sortFieldArr = null;
		Integer[] sortFlagInt = null;
		if(StringUtils.isNoneBlank(sortFields)){
			sortFieldArr = sortFields.split(",");
			String[] sortFlagArr = sortFlags.split(",");
			int sortFieldsize = sortFieldArr.length; 
			sortFlagInt = new Integer[sortFieldsize];
			for(int i = 0; i < sortFieldsize; i++){
				sortFlagInt[i] = Integer.parseInt(sortFlagArr[i].trim());
			}
		}
		if(StringUtils.isBlank(keyword)){
			params.append("*:*");
		} else{
			keyword = getEscapeChar(keyword);
			params.append("(");
			params.append("text:"+keyword + "");
			params.append(")");
			keyword = keyword.replace("-", "");
			params.append(" OR (");
			params.append("text:'"+keyword+"'");
			params.append(")");
		}

		query = new SolrQuery();
		query.setQuery(params.toString());
		/**品牌过滤*/
		if(brandId != null && brandId > 0){
			query.addFilterQuery("brandId:"+brandId);
		}
		/**分类过滤*/
		if(categoryId != null && categoryId > 0){
			query.addFilterQuery("categoryId:"+categoryId+"*");
		}
		/**选型过滤*/
		if(attrValueIds != null){
			for(Integer attrValueId : attrValueIds){
				query.addFilterQuery("attrValueId:"+attrValueId);
			}
		}
		if(sortFields != null && sortFieldArr.length > 0){
			int sortFielsCount = sortFieldArr.length;
			for(int i = 0; i < sortFielsCount; i++){
				if(sortFlagInt[i]==0){
					query.addSort(sortFieldArr[i], SolrQuery.ORDER.asc);
				} else{
					query.addSort(sortFieldArr[i], SolrQuery.ORDER.desc);
				}
			}
		}
		query.setStart((page-1)*rows);
		query.setRows(rows);
		
		query.setFacet(true).setFacetMinCount(1);//.setFacetLimit(100)
		query.addFacetField("brandId");
		query.addFacetField("categoryId");
		if(categoryId != null){
			query.addFacetField("attrValueId");
		}
		return query;
	}
}
