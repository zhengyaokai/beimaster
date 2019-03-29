package bmatser.service.impl;

import com.google.common.collect.Lists;
import bmatser.model.ModelSelectionCategory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.BigCustomerPackageMapper;
import bmatser.dao.CategoryMapper;
import bmatser.dao.GoodsActivityMapper;
import bmatser.dao.GoodsCombinationMapper;
import bmatser.dao.GrouponActivityMapper;
import bmatser.dao.SellerGoodsMapper;
import bmatser.exception.GdbmroException;
import bmatser.model.Dealer;
import bmatser.model.GoodsAttrInfo;
import bmatser.pageModel.GoodsInfo;
import bmatser.pageModel.SearchGoods;
import bmatser.service.SolrCacheDataI;
import bmatser.service.SolrI;
import bmatser.util.Constants;
import bmatser.util.LogoException;
import bmatser.util.SolrUtil;

/**
 * solr搜索
 * @author felx
 * @date 2017-12-18
 */
@Service("solrService")
public class SolrImpl implements SolrI{
	
	private static int MAX_ROWS_NUM = JFig.getInstance().getIntegerValue("solr_options", "MAX_ROWS_NUM", "20");
	private static int MAX_PAGE_NUM = JFig.getInstance().getIntegerValue("solr_options", "MAX_PAGE_NUM", "10");
	private static String MALL_SOLR_URL = JFig.getInstance().getValue("solr_options", "MALL_SOLR_URL", "http://bmatser.com/solr/core2");
	private static String FACE_SOLR_URL = JFig.getInstance().getValue("solr_options", "FACE_SOLR_URL", "http://bmatser.com/solr/core4");
	private static String SAAS_SOLR_URL = JFig.getInstance().getValue("solr_options", "SAAS_SOLR_URL", "http://bmatser.com/solr/core3");
	private static String MALLSHOP_SOLR_URL = JFig.getInstance().getValue("solr_options", "MALLSHOP_SOLR_URL", "http:/bmatser.com/solr/core5");
	private static String SAAS_SERIES_URL = JFig.getInstance().getValue("solr_options", "SAAS_SERIES_URL", "http:/bmatser.com/solr/core6");
	private static String SAAS_MODEL_SELECTION_URL = JFig.getInstance().getValue("solr_options", "SAAS_MODEL_SELECTION_URL", "http://bmatser.com/solr/core3");
	private static HttpSolrServer mallSolrServer = null;
	private static HttpSolrServer mallShopSolrServer = null;
	private static HttpSolrServer faceSolrServer = null;
	private static HttpSolrServer saasSolrServer = null;
	private static HttpSolrServer modelSelectionSolrServer = null;
	private Map attrCache;
	private Map brandCache;
	private Map categoryCache;
	private final String[] charReplace = {"+","(",")","?",":"};
	
	@Autowired
	private SolrCacheDataI cacheDataService;
	@Autowired
	private SellerGoodsMapper sellerGoodsI;
	@Autowired
	private CategoryMapper categoryI;
	@Autowired
	private GoodsActivityMapper goodsActivityMapper;
	@Autowired
	private GrouponActivityMapper grouponActivityMapper;
	@Autowired
	private BigCustomerPackageMapper bigCustomerPackageDao;	
	@Autowired
	private GoodsCombinationMapper goodsCombinationDao;
	static{
		setMallSolrServer();
		setFaceSolrServer();
		setSaasSolrServer();
		setMallShopSolrServer();
		setSaasModelSelectionServer();
	}
	private static void setMallShopSolrServer(){
		if(mallShopSolrServer == null){
			mallShopSolrServer=new HttpSolrServer(MALLSHOP_SOLR_URL);
			mallShopSolrServer.setMaxTotalConnections(100);
			mallShopSolrServer.setSoTimeout(10000);
			mallShopSolrServer.setConnectionTimeout(5000);
		}
	}
	
	private static void setMallSolrServer(){
		if(mallSolrServer == null){
			mallSolrServer=new HttpSolrServer(MALL_SOLR_URL);
			mallSolrServer.setMaxTotalConnections(100);
			mallSolrServer.setSoTimeout(10000);
			mallSolrServer.setConnectionTimeout(5000);
		}
	}
	
	private static void setFaceSolrServer(){
		if(faceSolrServer == null){
			faceSolrServer=new HttpSolrServer(FACE_SOLR_URL);
			faceSolrServer.setMaxTotalConnections(100);
			faceSolrServer.setSoTimeout(10000);
			faceSolrServer.setConnectionTimeout(5000);
		}
	}
	private static void setSaasSolrServer(){
		if(saasSolrServer == null){
			saasSolrServer=new HttpSolrServer(SAAS_SOLR_URL);
			saasSolrServer.setMaxTotalConnections(100);
			saasSolrServer.setSoTimeout(10000);
			saasSolrServer.setConnectionTimeout(5000);
		}
	}
	private static void setSaasModelSelectionServer(){
		if(modelSelectionSolrServer == null){
			modelSelectionSolrServer=new HttpSolrServer(SAAS_MODEL_SELECTION_URL);
			modelSelectionSolrServer.setMaxTotalConnections(100);
			modelSelectionSolrServer.setSoTimeout(10000);
			modelSelectionSolrServer.setConnectionTimeout(5000);
		}
	}
	private void loadCacheData(){
		attrCache = cacheDataService.findCategoryAttr();
		brandCache = cacheDataService.findBrand();
		categoryCache = cacheDataService.findCategory();
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void convertAttr(Map countMap, Integer id, Map attrMap, Map sumMap){
		countMap.put("attrValueId", id);
		Integer attrId = Integer.parseInt(String.valueOf(attrMap.get("attrId")));
		countMap.put("attrId", attrId);
		String attrName = String.valueOf(attrMap.get("attrName"));
		String orderSn = String.valueOf(attrMap.get("orderSn"));
		countMap.put("attrName", attrName);
		countMap.put("orderSn", orderSn);
		countMap.put("attrValue", attrMap.get("attrValue"));
		List attrList = new ArrayList();
		attrList.add(countMap);
		if(sumMap.containsKey(attrName)){
			List attrListTemp = (List)sumMap.get(attrName);
			attrListTemp.addAll(attrList);
			sumMap.put(attrName, attrListTemp);
		} else{
			sumMap.put(attrName, attrList);
		}
	}

	/**
	 * 查询面价商品
	 * @param keyword 关键词
	 * @param brandId 品牌ID
	 * @param categoryId 分类ID
	 * @param attrValueIds 属性值ID集合，多个ID通过英文状态下逗号分隔
	 * @param page 当前第几页，从1开始
	 * @param rows 每页显示条数
	 * @return map 供应商的商品ID列表、商品总数
	 * @author felx
	 * @date 2017-12-22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	public Map<String, Object> searchFaceGoods(String keyword, Integer brandId, Integer categoryId, Integer[] attrValueIds, int page, int rows) {
		if(rows>MAX_ROWS_NUM){
			return null;
		}
		if(page > MAX_PAGE_NUM){
			page = 1;
		}
		Map<String, Object> map = new HashMap();
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		try{
			setFaceSolrServer();
			if(StringUtils.isBlank(keyword)){
				params.append("*:*");
			} else{
				params.append("(");
				params.append("text:'"+keyword + "'");
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
				query.addFilterQuery("categoryId:"+categoryId);
			}
			/**选型过滤*/
			if(attrValueIds != null){
				for(Integer attrValueId : attrValueIds){
					query.addFilterQuery("attrValueId:"+attrValueId);
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
			QueryResponse rsp = faceSolrServer.query(query);
			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			if(total > 100){
				total = 100L;
			}
			map.put("total", total);
			StringBuffer ids = new StringBuffer();
			for(SolrDocument solrDocument : solrList){
				String id = (String)solrDocument.getFieldValue("id");
				if(ids.length() == 0){
					ids.append(id);
				} else{
					ids.append(","+id);
				}
			}
			map.put("ids", ids);
			List<FacetField> facetFields = rsp.getFacetFields();
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = Integer.parseInt(count.getName());
					Long countL = count.getCount();
					countMap.put("count", countL);
					if(StringUtils.equals(facetName, "attrValueId")){
						Map attrMap = (Map)attrCache.get(BigInteger.valueOf(id));
						if(attrMap != null && !attrMap.isEmpty()){
							convertAttr(countMap, id, attrMap, sumMap);
						} else{
							continue;
						}
					} else if(StringUtils.equals(facetName, "brandId")){
						String brandName = (String)brandCache.get(id);
						if(StringUtils.isBlank(brandName)){
							continue;
						}
						countMap.put("brandId", id);
						countMap.put("brandName", brandName);
						tempCountList.add(countMap);
					} else if(StringUtils.equals(facetName, "categoryId")){
						String categoryName = (String)categoryCache.get(id);
						countMap.put("categoryId", id);
						countMap.put("categoryName", categoryName);
						if(StringUtils.isBlank(categoryName)){
							continue;
						} else if(StringUtils.equals(categoryName, "其他")){
							otherMap.putAll(countMap);
							continue;
						}
						tempCountList.add(countMap);
					} else{
						countMap.put("id", id);
						tempCountList.add(countMap);
					}
				}
				if(StringUtils.equals(facetName, "attrValueId")){
					mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
					Collections.sort(mappingList, new Comparator<Map.Entry>(){
						   public int compare(Map.Entry mapping1,Map.Entry mapping2){
							   List list1 = (List)mapping1.getValue();
							   Map map1 = (Map)list1.get(0);
							   List list2 = (List)mapping2.getValue();
							   Map map2 = (Map)list2.get(0);
						    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
						   }
					}); 
					map.put("attrs", mappingList);
				} else if(StringUtils.equals(facetName, "brandId")){
					map.put("brands", tempCountList);
				} else if(StringUtils.equals(facetName, "categoryId")){
					if(otherMap != null && !otherMap.isEmpty()){
						tempCountList.add(otherMap);
					}
					map.put("categorys", tempCountList);
				} else{
					map.put(facetName, tempCountList);
				}
			}
		} catch(Exception e){
			 e.printStackTrace();
			 return null;
		}
		return map;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> searchMallGoods(String keyword, Integer dealerId, Integer brandId,
			Integer categoryId, Integer[] attrValueIds, String[] sortFields, Integer[] sortFlags, int page, int rows, String alias) {
		Map<String, Object> map = new HashMap<>();
		List<String> idList = new ArrayList<String>();
		//TODO
		if((StringUtils.isBlank(keyword) && dealerId==null && brandId==null && categoryId==null && attrValueIds==null && StringUtils.isBlank(alias)) || rows>MAX_ROWS_NUM ){
			return null;
		}
		if(page > MAX_PAGE_NUM){
			page = 1;
		}
		StringBuffer params = new StringBuffer();
		if(StringUtils.isBlank(keyword)){
			params.append("*:*");
		} else{
			params.append(keyword);
		}
		SolrUtil solr = new SolrUtil(MALL_SOLR_URL);
		solr.setQuery(params.toString());
		/**商家过滤*/
		if(dealerId != null && dealerId > 0){
			solr.addFilterQuery("dealerId:"+dealerId);
		}
		/**品牌过滤*/
		if(brandId != null && brandId > 0){
			solr.addFilterQuery("brandId:"+brandId);
		}
		/**分类过滤*/
		if(categoryId != null && categoryId > 0){
			solr.addFilterQuery("categoryId:"+categoryId+"*");
		}
		/**店铺过滤*/
		if(alias != null && !"".equals(alias)){
			solr.addFilterQuery("alias:"+alias);
		}
		/**选型过滤*/
		if(attrValueIds != null){
			for(Integer attrValueId : attrValueIds){
				solr.addFilterQuery("attrValueId:"+attrValueId);
			}
		}
		if(sortFields != null && sortFields.length > 0){
			int sortFielsCount = sortFields.length;
			for(int i = 0; i < sortFielsCount; i++){
				if(sortFlags[i]==0){
					solr.addSort(sortFields[i], SolrQuery.ORDER.asc);
				} else{
					solr.addSort(sortFields[i], SolrQuery.ORDER.desc);
				}
			}
		}
		solr.setStart((page-1)*rows);
		solr.setRows(rows);
		solr.setParam(GroupParams.GROUP, true);
		solr.setParam(GroupParams.GROUP_FIELD, "spuId");
		solr.setParam(GroupParams.GROUP_LIMIT, "0");
		solr.setParam("group.ngroups", true);
		
		solr.setFacet(true).setFacetMinCount(1);//.setFacetLimit(100)
		solr.addFacetField("brandId");
		solr.addFacetField("categoryId");
		if(categoryId != null){
			solr.addFacetField("attrValueId");
		}
		solr.setRequestHandler("/browse");
		Integer total = 0;
		try {
			QueryResponse rsp = solr.getResponse();
			GroupResponse groupResponse = rsp.getGroupResponse();
			if(groupResponse!=null){
				 List<GroupCommand> groupList = groupResponse.getValues();  
				    for(GroupCommand groupCommand : groupList) {  
				        List<Group> groups = groupCommand.getValues();  
				        total = groupCommand.getNGroups();
				        for(Group group : groups) {
				        	idList.add(group.getGroupValue());
				        	//System.out.println(group.getGroupValue()+","+(int)group.getResult().getNumFound()+","+group.getResult());
				           // info.put(group.getGroupValue(), (int)group.getResult().getNumFound());  
				        }  
				    }  
			}
			
			List<FacetField> facetFields = rsp.getFacetFields();
			if(total > 100){
				total = 100;
			} 
			//System.out.println(total);
			map.put("total", total);
			SolrUtil solrGoods = new SolrUtil(MALL_SOLR_URL);
			StringBuffer buff = new StringBuffer("(");
			for (int i = idList.size()-1; i >=0; i--) {
				if(i==0){
					buff.append("goodsNo:"+idList.get(i)+" )");
				}else{
					buff.append("goodsNo:"+idList.get(i)+" or ");
				}
			}
			solrGoods.setQuery(buff.toString());
			if(sortFields != null && sortFields.length > 0){
				int sortFielsCount = sortFields.length;
				for(int i = 0; i < sortFielsCount; i++){
					if(sortFlags[i]==0){
						solrGoods.addSort(sortFields[i], SolrQuery.ORDER.asc);
					} else{
						solrGoods.addSort(sortFields[i], SolrQuery.ORDER.desc);
					}
				}
			}
			solrGoods.setRows(rows);
			QueryResponse rsp2 = solrGoods.getResponse();
			List<GoodsInfo> goodsInfoList = rsp2.getBeans(GoodsInfo.class);
			for(GoodsInfo goodsInfo : goodsInfoList){
//				if(goodsInfo.getSellerId()!=null && goodsInfo.getSellerId()==9407609){
//					goodsInfo.setImage("http://img10.360buyimg.com/imgzone/"+goodsInfo.getImage());
//				}else{
					goodsInfo.setImage(Constants.IMAGE_URL+"goodsImg/"+goodsInfo.getImage());
//				}
			}
			map.put("goodsList", goodsInfoList);
			/*List<FacetField> facetFields = rsp.getFacetFields();*/
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = Integer.parseInt(count.getName());
					Long countL = count.getCount();
					countMap.put("count", countL);
					if(StringUtils.equals(facetName, "attrValueId")){
						Map attrMap = (Map)attrCache.get(Long.valueOf(id));
						if(attrMap != null && !attrMap.isEmpty()){
							convertAttr(countMap, id, attrMap, sumMap);
						} else{
							continue;
						}
					} else if(StringUtils.equals(facetName, "brandId")){
						String brandName = (String)brandCache.get(id);
						if(StringUtils.isBlank(brandName)){
							continue;
						}
						countMap.put("brandId", id);
						countMap.put("brandName", brandName);
						tempCountList.add(countMap);
					} else if(StringUtils.equals(facetName, "categoryId")){
						String categoryName = (String)categoryCache.get(id);
						countMap.put("categoryId", id);
						countMap.put("categoryName", categoryName);
						if(StringUtils.isBlank(categoryName)){
							continue;
						} else if(StringUtils.equals(categoryName, "其他")){
							otherMap.putAll(countMap);
							continue;
						}
						tempCountList.add(countMap);
					} else{
						countMap.put("id", id);
						tempCountList.add(countMap);
					}
				}
				map.put("attrs", mappingList);//不管是否有数据都返回出这个attrs
				if(StringUtils.equals(facetName, "attrValueId")){
					mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
					Collections.sort(mappingList, new Comparator<Map.Entry>(){
						   public int compare(Map.Entry mapping1,Map.Entry mapping2){
							   List list1 = (List)mapping1.getValue();
							   Map map1 = (Map)list1.get(0);
							   List list2 = (List)mapping2.getValue();
							   Map map2 = (Map)list2.get(0);
						    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
						   }
					}); 
					map.put("attrs", mappingList);
				} else if(StringUtils.equals(facetName, "brandId")){
					map.put("brands", tempCountList);
				} else if(StringUtils.equals(facetName, "categoryId")){
					if(otherMap != null && !otherMap.isEmpty()){
						tempCountList.add(otherMap);
					}
					map.put("categorys", tempCountList);
				} else{
					map.put(facetName, tempCountList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return map;
	}
	/**
	 * 搜索商城商品
	 * @param keyword 关键词
	 * @param brandId 品牌ID
	 * @param categoryId 分类ID
	 * @param attrValueIds 属性值ID集合，多个ID通过英文状态下逗号分隔
	 * @param sortFields 排序字段
	 * @param sortFlags 排序方式（true正序  false倒序）
	 * @param page 当前第几页，从1开始
	 * @param rows 每页显示条数
	 * @return map 供应商的商品ID列表、商品总数
	 * @author felx
	 * @date 2017-12-22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> searchMallGoods2(String keyword, Integer dealerId, Integer brandId,
			Integer categoryId, Integer[] attrValueIds, String[] sortFields, Integer[] sortFlags, int page, int rows) {
		if(rows>MAX_ROWS_NUM){
			return null;
		}
		if(page > MAX_PAGE_NUM){
			page = 1;
		}
		if(StringUtils.isBlank(keyword) && dealerId==null && brandId==null && categoryId==null && attrValueIds==null){
			return null;
		}
		Map<String, Object> map = new HashMap();
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		try{
			setMallSolrServer();
			if(StringUtils.isBlank(keyword)){
				params.append("*:*");
			} else{
//				params.append("(");
//				params.append("text:'"+keyword+ "'"); 
//				params.append(")");
				
				params.append(keyword);
			}
			/**分类过滤*/
//			if(categoryId != null && categoryId > 0){
//				params.append("((");
//				params.append("categoryId:"+"["+categoryIdToolFont(categoryId)+" TO "+this.categoryIdTool(categoryId)+"]");
//				params.append(")");
//				
//				params.append(" OR (");
//				params.append("categoryId:"+categoryId);
//				params.append("))");
//			}
			query = new SolrQuery();
			query.setQuery(params.toString());
			/**商家过滤*/
			if(dealerId != null && dealerId > 0){
				query.addFilterQuery("dealerId:"+dealerId);
			}
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
			if(sortFields != null && sortFields.length > 0){
				int sortFielsCount = sortFields.length;
				for(int i = 0; i < sortFielsCount; i++){
					if(sortFlags[i]==0){
						query.addSort(sortFields[i], SolrQuery.ORDER.asc);
					} else{
						query.addSort(sortFields[i], SolrQuery.ORDER.desc);
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
//			System.out.println("---solr start:"+new Date());
//			System.out.println("++++mallSolrServer:"+mallSolrServer.getBaseURL());
			query.setRequestHandler("/browse");
			QueryResponse rsp = mallSolrServer.query(query);
//			System.out.println("---solr end:"+new Date());
			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			if(total > 100){
				total = 100L;
			}
			map.put("total", total);
			List<GoodsInfo> goodsInfoList = rsp.getBeans(GoodsInfo.class);
			for(GoodsInfo goodsInfo : goodsInfoList){
//				if(goodsInfo.getSellerId()!=null && goodsInfo.getSellerId()==9407609){
//					goodsInfo.setImage("http://img10.360buyimg.com/imgzone/"+goodsInfo.getImage());
//				}else{
					goodsInfo.setImage(Constants.IMAGE_URL+"goodsImg/"+goodsInfo.getImage());
//				}
			}
			map.put("goodsList", goodsInfoList);
			List<FacetField> facetFields = rsp.getFacetFields();
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = Integer.parseInt(count.getName());
					Long countL = count.getCount();
					countMap.put("count", countL);
					if(StringUtils.equals(facetName, "attrValueId")){
						Map attrMap = (Map)attrCache.get(Long.valueOf(id));
						if(attrMap != null && !attrMap.isEmpty()){
							convertAttr(countMap, id, attrMap, sumMap);
						} else{
							continue;
						}
					} else if(StringUtils.equals(facetName, "brandId")){
						String brandName = (String)brandCache.get(id);
						if(StringUtils.isBlank(brandName)){
							continue;
						}
						countMap.put("brandId", id);
						countMap.put("brandName", brandName);
						tempCountList.add(countMap);
					} else if(StringUtils.equals(facetName, "categoryId")){
						String categoryName = (String)categoryCache.get(id);
						countMap.put("categoryId", id);
						countMap.put("categoryName", categoryName);
						if(StringUtils.isBlank(categoryName)){
							continue;
						} else if(StringUtils.equals(categoryName, "其他")){
							otherMap.putAll(countMap);
							continue;
						}
						tempCountList.add(countMap);
					} else{
						countMap.put("id", id);
						tempCountList.add(countMap);
					}
				}
				map.put("attrs", mappingList);//不管是否有数据都返回出这个attrs
				if(StringUtils.equals(facetName, "attrValueId")){
					mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
					Collections.sort(mappingList, new Comparator<Map.Entry>(){
						   public int compare(Map.Entry mapping1,Map.Entry mapping2){
							   List list1 = (List)mapping1.getValue();
							   Map map1 = (Map)list1.get(0);
							   List list2 = (List)mapping2.getValue();
							   Map map2 = (Map)list2.get(0);
						    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
						   }
					}); 
					map.put("attrs", mappingList);
				} else if(StringUtils.equals(facetName, "brandId")){
					map.put("brands", tempCountList);
				} else if(StringUtils.equals(facetName, "categoryId")){
					if(otherMap != null && !otherMap.isEmpty()){
						tempCountList.add(otherMap);
					}
					map.put("categorys", tempCountList);
				} else{
					map.put(facetName, tempCountList);
				}
			}
		} catch(Exception e){
			 e.printStackTrace();
		}
		return map;
	}
	
	
	@Override
	public Map<String, Object> searchSaasGoods(SearchGoods search) {
		if(search.getRows()>MAX_ROWS_NUM){
			return null;
		}
		if(search.getPage() > MAX_PAGE_NUM){
			search.setPage(1);
		}
		if(search.isNullBySaas()){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		
		try {
			setSaasSolrServer();
			SolrParams saasQuery = search.getSaasQuery();
			QueryResponse rsp = saasSolrServer.query(saasQuery);
			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			List<Integer> catrIdArr =new ArrayList<>();
			if(total > 200){
				total = 200L;
			}
			map.put("total", total);
			List<GoodsInfo> goodsInfoList = rsp.getBeans(GoodsInfo.class);
			dealSaasGoodsInfoList(goodsInfoList,search);
	        map.put("searchList", goodsInfoList);
			List<FacetField> facetFields = rsp.getFacetFields();
			if(search.getKeys()!=null && search.getKeys().length>0){
				return map;
			}
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = Integer.parseInt(count.getName());
					Long countL = count.getCount();
					countMap.put("count", countL);
					switch (facetName) {
					case "attrValueId":
							Map attrMap = (Map)attrCache.get(Long.valueOf(id));
							if(attrMap != null && !attrMap.isEmpty()){
								convertAttr(countMap, id, attrMap, sumMap);
							} else{
								continue;
							}
						break;
					case "brandId":
							String brandName = (String)brandCache.get(id);
							if(StringUtils.isBlank(brandName)){
								continue;
							}
							countMap.put("brandId", id);
							countMap.put("brandName", brandName);
							tempCountList.add(countMap);
						break;
					case "categoryId":
							catrIdArr.add(id);
						break;

					default:
							countMap.put("id", id);
							tempCountList.add(countMap);
						break;
					}
				}
				//FIXME 目前先去掉属性，后期恢复
				map.put("attrs", mappingList);//不管是否有数据都返回出这个attrs
				switch (facetName) {
				case "attrValueId":
						mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
						Collections.sort(mappingList, new Comparator<Map.Entry>(){
							   public int compare(Map.Entry mapping1,Map.Entry mapping2){
								   List list1 = (List)mapping1.getValue();
								   Map map1 = (Map)list1.get(0);
								   List list2 = (List)mapping2.getValue();
								   Map map2 = (Map)list2.get(0);
							    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
							   }
						}); 
						//FIXME 目前先去掉属性，后期恢复
						map.put("attrs", mappingList);
					break;
				case "brandId":
						map.put("tempCount", tempCountList);
					break;
				case "categoryId":
					break;
				default:
						map.put(facetName, tempCountList);
					break;
				}
			}

            /**
             *     create  by yr on 2018-11-21
             *     增加id
             */
			if(catrIdArr.size()>0){
				switch (search.getCategoryId()!=null?search.getCategoryId().toString().length() : 0) {
				case 4:
					map.put("categorys", categoryI.findThreeCate(catrIdArr));
					map.put("parentCategorys", getParentCategorys(search.getCategoryId()));
					break;
				case 6:
					map.put("categorys", categoryI.findFourCate(catrIdArr));
					map.put("parentCategorys",getParentCategorys(search.getCategoryId()));
					break;
				case 8:
					map.put("categorys", new ArrayList<>());
					map.put("parentCategorys", getParentCategorys(search.getCategoryId()));
					break;
				case 0:
					map.put("categorys", categoryI.findThreeCate(catrIdArr));
					map.put("parentCategorys", new ArrayList<>());
					break;
				}
			}

			Map<String, Object> advantage  = new HashMap<String, Object>();
			advantage.put("name", "仅看有货");
			advantage.put("code", 0);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.add(advantage);
//			map.put("advantage", list);
			List<Map> attrArray = new ArrayList<Map>();
			List<Map<String, Object>> attrData = new ArrayList<Map<String, Object>>();
			//FIXME 目前先去掉属性，后期恢复
			if(map.get("attrs")==null){
				map.put("attrs", new ArrayList<>());
			}else{
				List<Map.Entry> attrs = (List<Map.Entry>) map.get("attrs");
				for (Entry entry : attrs) {
					Map<String, Object> attr = new HashMap<String, Object>();
					attr.put("name", entry.getKey());
					attr.put("data", entry.getValue());
					attrArray.add(attr);
				}
				
				for (Map arrary : attrArray) {
					attrData = (List<Map<String, Object>>) arrary.get("data");
					if(attrData!=null&&attrData.size()>1){
						sortAttr(attrData);
					}
				}
			} 
			map.put("attrArray", attrArray);
			
			if(search.getCategoryId()!=null){
				int isLeaf = categoryI.findCategoryIsLeaf(search.getCategoryId());
				if(isLeaf!=1){
					map.put("attrArray", new ArrayList<>());
					map.put("attrs", new ArrayList<>());
				}
			}else{
				map.put("attrArray", new ArrayList<>());
				map.put("attrs", new ArrayList<>());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	
	@Override
	public Map<String, Object> searchFaceSaasGoods(SearchGoods search) {	
		if(search.getRows()>MAX_ROWS_NUM){
			return null;
		}
		if(search.getPage() > MAX_PAGE_NUM){
			search.setPage(1);
		}
		if(search.isNullBySaas()){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		
		try {
			SolrParams saasQuery = search.getSaasQuery();
			QueryResponse rsp = modelSelectionSolrServer.query(saasQuery);
			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			List<Integer> catrIdArr =new ArrayList<>();
			if(total > 150){
				total = 150L;
			}
			map.put("total", total);
			List<GoodsInfo> goodsInfoList = rsp.getBeans(GoodsInfo.class);
			for(GoodsInfo goodsInfo : goodsInfoList){
				goodsInfo.setImage(Constants.IMAGE_URL+"goodsImg/"+goodsInfo.getImage());
				goodsInfo.setPrice(goodsInfo.getMarketPrice().toString());
				goodsInfo.setGoodsName(goodsInfo.getTitle());
				Map sellerGoods = sellerGoodsI.getSellerGoodsByGoodsId(goodsInfo.getId());
				if(sellerGoods!=null && sellerGoods.get("salePrice")!=null ){
					goodsInfo.setSalePrice(Double.parseDouble(sellerGoods.get("salePrice").toString()));
//					goodsInfo.setSellerGoodsId(sellerGoods.get("id").toString());
					goodsInfo.setBatchQuantity(Integer.parseInt(sellerGoods.get("batchQuantity")!=null?sellerGoods.get("batchQuantity").toString():"1"));
					goodsInfo.setOrderQuantity(Integer.parseInt(sellerGoods.get("orderQuantity")!=null?sellerGoods.get("orderQuantity").toString():"1"));
				}
			}
	        map.put("goodsList", goodsInfoList);
			List<FacetField> facetFields = rsp.getFacetFields();
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = Integer.parseInt(count.getName());
					Long countL = count.getCount();
					countMap.put("count", countL);
					switch (facetName) {
					case "attrValueId":
							Map attrMap = (Map)attrCache.get(Long.valueOf(id));
							if(attrMap != null && !attrMap.isEmpty()){
								convertAttr(countMap, id, attrMap, sumMap);
							} else{
								continue;
							}
						break;
					case "brandId":
							String brandName = (String)brandCache.get(id);
							if(StringUtils.isBlank(brandName)){
								continue;
							}
							countMap.put("brandId", id);
							countMap.put("brandName", brandName);
							tempCountList.add(countMap);
						break;
					case "categoryId":
							catrIdArr.add(id);
						break;

					default:
							countMap.put("id", id);
							tempCountList.add(countMap);
						break;
					}
				}
				
				map.put("attrs", mappingList);//不管是否有数据都返回出这个attrs
				switch (facetName) {
				case "attrValueId":
						mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
						Collections.sort(mappingList, new Comparator<Map.Entry>(){
							   public int compare(Map.Entry mapping1,Map.Entry mapping2){
								   List list1 = (List)mapping1.getValue();
								   Map map1 = (Map)list1.get(0);
								   List list2 = (List)mapping2.getValue();
								   Map map2 = (Map)list2.get(0);
							    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
							   }
						}); 
						map.put("attrs", mappingList);
					break;
				case "brandId":
						map.put("brands", tempCountList);
					break;
				case "categoryId":
					break;
				default:
						map.put(facetName, tempCountList);
					break;
				}
			}
			if(catrIdArr.size()>0){
				switch (search.getCategoryId()!=null?search.getCategoryId().toString().length() : 0) {
				case 4:
					map.put("categorys", categoryI.findThreeCate(catrIdArr));
					map.put("parentCategorys", getThirdFourthCategorys(search.getCategoryId()));
					break;
				case 6:
					map.put("categorys", categoryI.findFourCate(catrIdArr));
					map.put("parentCategorys",getThirdFourthCategorys(search.getCategoryId()));
					break;
				case 8:
					map.put("categorys", new ArrayList<>());
					map.put("parentCategorys", getThirdFourthCategorys(search.getCategoryId()));
					break;
				case 0:
					map.put("categorys", categoryI.findThreeCate(catrIdArr));
					map.put("parentCategorys", new ArrayList<>());
					break;
				}
			}

			Map<String, Object> advantage  = new HashMap<String, Object>();
			advantage.put("name", "仅看有货");
			advantage.put("code", 0);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list.add(advantage);
			map.put("advantage", list);
			List<Map> attrArray = new ArrayList<Map>();
			List<Map<String, Object>> attrData = new ArrayList<Map<String, Object>>();
			if(map.get("attrs")==null){
				map.put("attrs", new ArrayList<>());
			}else{
				List<Map.Entry> attrs = (List<Map.Entry>) map.get("attrs");
				for (Entry entry : attrs) {
					Map<String, Object> attr = new HashMap<String, Object>();
					attr.put("name", entry.getKey());
					attr.put("data", entry.getValue());
					attrArray.add(attr);
				}
				
				for (Map arrary : attrArray) {
					attrData = (List<Map<String, Object>>) arrary.get("data");
					if(attrData!=null&&attrData.size()>1){
						sortAttr(attrData);
					}
				}
			} 
			map.put("attrArray", attrArray);
			if(search.getCategoryId()!=null){
				int isLeaf = categoryI.findCategoryIsLeaf(search.getCategoryId());
				if(isLeaf!=1){
					map.put("attrArray", new ArrayList<>());
					map.put("attrs", new ArrayList<>());
				}
			}else{
				map.put("attrArray", new ArrayList<>());
				map.put("attrs", new ArrayList<>());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return map;
	}
	
	
	/**
	 * 处理分类
	 * @param categoryId
	 * @return
	 */
	private List<Map> getParentCategorys(Integer categoryId) {
		List<Map> list =new ArrayList<>();
		Integer[] bit = {4,6,8};
		String str = categoryId.toString();
		int len = str.length();
		for (int i = 0; i < bit.length; i++) {
			if(bit[i]<=len){
				Map map = categoryI.findCate(str.substring(0, bit[i]));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * (brandId=266)商品查询
	 * 获取第三级第四级分类
	 * @param categoryId
	 * @return
	 */
	private List<Map> getThirdFourthCategorys(Integer categoryId) {
		List<Map> list =new ArrayList<>();
		Integer[] bit = {6,8};
		String str = categoryId.toString();
		int len = str.length();
		for (int i = 0; i < bit.length; i++) {
			if(bit[i]<=len){
				Map map = categoryI.findCate(str.substring(0, bit[i]));
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 处理Saas商品数据
	 * @param goodsInfoList
	 * @param search
	 */
	private void dealSaasGoodsInfoList(List<GoodsInfo> goodsInfoList, SearchGoods search) {
		//查询当前用户是否是大客户返回套餐id
		Integer bigCustomerPackageId = bigCustomerPackageDao.isExist(search.getDealerId());
		for (GoodsInfo goodsInfo : goodsInfoList) {
			goodsInfo.initSet();
//			goodsInfo.setIsCombination(0);
			//根据sellerGoodsId查出该商品是否存在组合套餐
			List<Map<String,Object>> list = goodsCombinationDao.selectGoodsCombination(goodsInfo.getId());
			if(list!=null&&list.size()>0){
//				goodsInfo.setIsCombination(1);
			}
			//查询商品是否是套餐商品返回折扣
			BigDecimal rate = bigCustomerPackageDao.goodsIsPackage(goodsInfo.getId(),bigCustomerPackageId);
			if(rate!=null&&rate.compareTo(BigDecimal.ZERO)!=0){
				DecimalFormat df = new DecimalFormat("#.00");
				goodsInfo.setSalePrice(Double.valueOf(df.format(goodsInfo.getMarketPrice()*rate.doubleValue())));
//				goodsInfo.setIsBean(0);
//				goodsInfo.setIsFullcut(0);
//				goodsInfo.setIsFullgive(0);
//				goodsInfo.setIsGroupon(0);
//				goodsInfo.setIsRate(0);
//				goodsInfo.setIsRebates(0);
				goodsInfo.setIsSale(0);
				goodsInfo.setIsCanJhs(0);
				if(StringUtils.isBlank(goodsInfo.getTitle())){
					goodsInfo.setTitle(goodsInfo.getGoodsName());
				}
				
//				goodsInfo.setIsBigCustomer(1);
			}else{
//				if(1==goodsInfo.getIsFullgive()){
//					String fullgiveDesc = goodsActivityMapper.findFullgiveDesc(goodsInfo.getId());
//					goodsInfo.setFullgiveDesc(fullgiveDesc);
//				}
				Integer isGroup = goodsInfo.getIsJhs();
				if(isGroup!=null && isGroup==1){
					String sellerGoodId = goodsInfo.getId();
					//团购描述
//					String desc = grouponActivityMapper.findGrouponDesc(sellerGoodId);
					String groupid = grouponActivityMapper.findGrouponId(sellerGoodId);
					goodsInfo.setIsJhs(grouponActivityMapper.isGroupGoodsBysellerGoodsId(sellerGoodId));
//					goodsInfo.setGroupDesc(desc);
//					goodsInfo.setGroupId(groupid);
//					goodsInfo.setIsFullcut(0);
					int isSupportGroup = 0;
					if(search.getDealerId() != null){
						isSupportGroup = grouponActivityMapper.isGroupGoods(search.getDealerId().toString(),sellerGoodId);
					}
					if(isSupportGroup==0){
						goodsInfo.setIsCanJhs(0);
					}else{
						goodsInfo.setIsCanJhs(1);
					}
				}
//				if(StringUtils.equals("1", goodsInfo.getIsFullcut()==null?"":goodsInfo.getIsFullcut().toString())){
//					Map fullcat= goodsActivityMapper.findFullCut(goodsInfo.getId());
//					if(fullcat!=null){
//						goodsInfo.setFullAmount((fullcat.get("fullAmount")!=null&&!"".equals(fullcat.get("fullAmount").toString()))?fullcat.get("fullAmount").toString():"");
//						goodsInfo.setCutExt((fullcat.get("cutExt")!=null&&!"".equals(fullcat.get("cutExt").toString()))?fullcat.get("cutExt").toString():"0");
//					}
//				}
				if(StringUtils.isBlank(goodsInfo.getTitle())){
					goodsInfo.setTitle(goodsInfo.getGoodsName());
				}
				
			}
			
			
		}
	
	}

	/**
	 * 搜索Saas商品
	 * @param keyword 关键词
	 * @param brandId 品牌ID
	 * @param categoryId 分类ID
	 * @param attrValueIds 属性值ID集合，多个ID通过英文状态下逗号分隔
	 * @param sortFields 排序字段
	 * @param sortFlags 排序方式（true正序  false倒序）
	 * @param page 当前第几页，从1开始
	 * @param rows 每页显示条数
	 * @return map 供应商的商品ID列表、商品总数
	 * @author felx
	 * @date 2017-12-22
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	@Override
	public Map<String, Object> searchSaasGoods(String keyword, Integer dealerId, Integer brandId,
			Integer categoryId, Integer[] attrValueIds, String[] sortFields, Integer[] sortFlags, int page, int rows,String adv) {
		if(rows>MAX_ROWS_NUM){
			return null;
		}
		if(page > MAX_PAGE_NUM){
			page = 1;
		}
		
		if(StringUtils.isBlank(keyword) && brandId==null && categoryId==null && attrValueIds==null){
			return null;
		}
		Map<String, Object> map = new HashMap();
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		try{
			setSaasSolrServer();
			if(StringUtils.isBlank(keyword)){
				params.append("*:*");
			} else{
				keyword = getEscapeChar(keyword);
				params.append("(");
				params.append("text:'"+keyword + "'");
				params.append(")");
				keyword = keyword.replace("-", "");
				params.append(" OR (");
				params.append("text:'"+keyword+"'");
				params.append(")");
			}
			/**分类过滤*/
//			if(categoryId != null && categoryId > 0){
//				params.append("((");
//				params.append("categoryId:"+"["+categoryIdToolFont(categoryId)+" TO "+this.categoryIdTool(categoryId)+"]");
//				params.append(")");
//				
//				params.append(" OR (");
//				params.append("categoryId:"+categoryId);
//				params.append("))");
//			}
			query = new SolrQuery();
			query.setQuery(params.toString());
			/**商家过滤*/
/*			if(dealerId != null && dealerId > 0){
				query.addFilterQuery("dealerId:"+dealerId);
			}*/
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
			if(adv!=null&&"0".equals(adv)){
				query.addFilterQuery("stock:[1 TO *]");
			}
			if(sortFields != null && sortFields.length > 0){
				int sortFielsCount = sortFields.length;
				for(int i = 0; i < sortFielsCount; i++){
					if(sortFlags[i]==0){
						query.addSort(sortFields[i], SolrQuery.ORDER.asc);
					} else{
						query.addSort(sortFields[i], SolrQuery.ORDER.desc);
					}
				}
			}
			query.setStart((page-1)*rows);
			query.setRows(rows);
			
//			query.setFacet(true).setFacetMinCount(1).setFacetLimit(100);
			query.setFacet(true).setFacetMinCount(1);
			query.addFacetField("brandId");
			query.addFacetField("categoryId");
			if(categoryId != null){
				query.addFacetField("attrValueId");
			}
			QueryResponse rsp = saasSolrServer.query(query);
			
			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			if(total > 100){
				total = 100L;
			}
			map.put("total", total);
			List<GoodsInfo> goodsInfoList = rsp.getBeans(GoodsInfo.class);
			//查询当前用户是否是大客户返回套餐id
			Integer bigCustomerPackageId = bigCustomerPackageDao.isExist(dealerId);
			for(GoodsInfo goodsInfo : goodsInfoList){
//				goodsInfo.setIsCombination(0);
				//根据sellerGoodsId查出该商品是否存在组合套餐
				List<Map<String,Object>> list = goodsCombinationDao.selectGoodsCombination(goodsInfo.getId());
				if(list!=null&&list.size()>0){
//					goodsInfo.setIsCombination(1);
				}
				goodsInfo.setImage(Constants.IMAGE_URL+"goodsImg/"+goodsInfo.getImage());
				
				//查询商品是否是套餐商品返回折扣
				BigDecimal rate = bigCustomerPackageDao.goodsIsPackage(goodsInfo.getId(),bigCustomerPackageId);
				if(rate!=null&&rate.compareTo(BigDecimal.ZERO)!=0){
					goodsInfo.setSalePrice(goodsInfo.getMarketPrice()*rate.doubleValue());
//					goodsInfo.setIsBean(0);
//					goodsInfo.setIsFullcut(0);
//					goodsInfo.setIsFullgive(0);
//					goodsInfo.setIsGroupon(0);
//					goodsInfo.setIsRate(0);
//					goodsInfo.setIsRebates(0);
					goodsInfo.setIsSale(0);
					goodsInfo.setIsCanJhs(0);
//					goodsInfo.setIsBigCustomer(1);
				}else{
					//goodsInfo.setImage(Constants.IMAGE_URL+"goodsImg/"+goodsInfo.getImage());
//					goodsInfo.setIsRate((goodsInfo.getIsRate()!=null && goodsInfo.getIsSale() != 1)?goodsInfo.getIsRate() : 0);
					goodsInfo.setIsSale(goodsInfo.getIsSale()!=null?goodsInfo.getIsSale() : 0);
//					goodsInfo.setIsFullcut(goodsInfo.getIsFullcut()!=null?goodsInfo.getIsFullcut() : 0);
//					goodsInfo.setIsRebates(goodsInfo.getIsRebates()!=null?goodsInfo.getIsRebates() : 0);
//					int isGroup = goodsInfo.getIsGroupon();
//					if(goodsInfo.getIsRate() == 1 && goodsInfo.getIsSale() != 1){
//						if(goodsInfo.getRate()!=null){
//							double  ratePrice = goodsInfo.getSalePrice()*goodsInfo.getRate();
//							goodsInfo.setRatePrice(Math.floor(ratePrice));
//						}
//					}
//					if(1==goodsInfo.getIsFullgive()){
//						String fullgiveDesc = goodsActivityMapper.findFullgiveDesc(goodsInfo.getId());
//						goodsInfo.setFullgiveDesc(fullgiveDesc);
//					}
//					if(isGroup==1){
//						String sellerGoodId = goodsInfo.getId();
//						//团购描述
//						String desc = grouponActivityMapper.findGrouponDesc(sellerGoodId);
//						String groupid = grouponActivityMapper.findGrouponId(sellerGoodId);
//						goodsInfo.setIsGroupon(grouponActivityMapper.isGroupGoodsBysellerGoodsId(sellerGoodId));
//						goodsInfo.setGroupDesc(desc);
//						goodsInfo.setGroupId(groupid);
//						goodsInfo.setIsFullcut(0);
//						int isSupportGroup = 0;
//						if(dealerId != null){
//							isSupportGroup = grouponActivityMapper.isGroupGoods(dealerId.toString(),sellerGoodId);
//						}
//						if(isSupportGroup==0){
//							goodsInfo.setIsSupportGroupon(0);
//						}else{
//							goodsInfo.setIsSupportGroupon(1);
//						}
//					}
//					if(StringUtils.equals("1", goodsInfo.getIsFullcut()==null?"":goodsInfo.getIsFullcut().toString())){
//						Map fullcat= goodsActivityMapper.findFullCut(goodsInfo.getId());
//						if(fullcat!=null){
//							goodsInfo.setFullAmount((fullcat.get("fullAmount")!=null&&!"".equals(fullcat.get("fullAmount").toString()))?fullcat.get("fullAmount").toString():"");
//							goodsInfo.setCutExt((fullcat.get("cutExt")!=null&&!"".equals(fullcat.get("cutExt").toString()))?fullcat.get("cutExt").toString():"0");
//						}
//					}
					goodsInfo.setTitle(goodsInfo.getGoodsName());	
				}
				goodsInfo.setTitle(goodsInfo.getGoodsName());	
			}
			map.put("goodsList", goodsInfoList);
			List<FacetField> facetFields = rsp.getFacetFields();
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = Integer.parseInt(count.getName());
					Long countL = count.getCount();
					countMap.put("count", countL);
					if(StringUtils.equals(facetName, "attrValueId")){
						Map attrMap = (Map)attrCache.get(Long.valueOf(id));
						if(attrMap != null && !attrMap.isEmpty()){
							convertAttr(countMap, id, attrMap, sumMap);
						} else{
							continue;
						}
					} else if(StringUtils.equals(facetName, "brandId")){
						String brandName = (String)brandCache.get(id);
						if(StringUtils.isBlank(brandName)){
							continue;
						}
						countMap.put("brandId", id);
						countMap.put("brandName", brandName);
						tempCountList.add(countMap);
					} else if(StringUtils.equals(facetName, "categoryId")){
						String categoryName = (String)categoryCache.get(id);
						countMap.put("categoryId", id);
						countMap.put("categoryName", categoryName);
						if(StringUtils.isBlank(categoryName)){
							continue;
						} else if(StringUtils.equals(categoryName, "其他")){
							otherMap.putAll(countMap);
							continue;
						}
						int isLeaf = categoryI.findCategoryIsLeaf(id);
						if(isLeaf==1){
							tempCountList.add(countMap);
						}
					} else{
						countMap.put("id", id);
						tempCountList.add(countMap);
					}
				}
				map.put("attrs", mappingList);//不管是否有数据都返回出这个attrs
				if(StringUtils.equals(facetName, "attrValueId")){
					mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
					Collections.sort(mappingList, new Comparator<Map.Entry>(){
						   public int compare(Map.Entry mapping1,Map.Entry mapping2){
							   List list1 = (List)mapping1.getValue();
							   Map map1 = (Map)list1.get(0);
							   List list2 = (List)mapping2.getValue();
							   Map map2 = (Map)list2.get(0);
						    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
						   }
					}); 
					map.put("attrs", mappingList);
				} else if(StringUtils.equals(facetName, "brandId")){
					if(tempCountList.size()>0){
						map.put("brands", tempCountList);
					}else{
						if(brandId != null){
							List brandList = new ArrayList<>();
							Map brand = new HashMap<>();
							brand.put("brandId", brandId);
							brand.put("brandName", brandCache.get(brandId).toString());
							brand.put("count", 0);
							brandList.add(brand);
							map.put("brands", brandList);
						}else{
							map.put("brands", tempCountList);
						}
					}
				} else if(StringUtils.equals(facetName, "categoryId")){
					if(otherMap != null && !otherMap.isEmpty()){
						tempCountList.add(otherMap);
					}
					map.put("categorys", tempCountList);
				} else{
					map.put(facetName, tempCountList);
				}
			}
		} catch(Exception e){
			 e.printStackTrace();
		}
		Map<String, Object> advantage  = new HashMap<String, Object>();
		advantage.put("name", "仅看有货");
		advantage.put("code", 0);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		list.add(advantage);
		map.put("advantage", list);
		List<Map> attrArray = new ArrayList<Map>();
		List<Map<String, Object>> attrData = new ArrayList<Map<String, Object>>();
		if(map.get("attrs")==null){
			map.put("attrs", new ArrayList<>());
		}else{
			List<Map.Entry> attrs = (List<Map.Entry>) map.get("attrs");
			for (Entry entry : attrs) {
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("name", entry.getKey());
				attr.put("data", entry.getValue());
				attrArray.add(attr);
			}
			
			for (Map arrary : attrArray) {
				attrData = (List<Map<String, Object>>) arrary.get("data");
				if(attrData!=null&&attrData.size()>1){
					sortAttr(attrData);
				}
			}
		}
		map.put("attrArray", attrArray);
		if(categoryId!=null){
			int isLeaf = categoryI.findCategoryIsLeaf(categoryId);
			if(isLeaf!=1){
				map.put("attrArray", new ArrayList<>());
				map.put("attrs", new ArrayList<>());
			}
		}else{
			map.put("attrArray", new ArrayList<>());
			map.put("attrs", new ArrayList<>());
		}
		return map;
	}
	/**
	 * 对属性进行排序
	 * @param list
	 */
	private void sortAttr(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size() -1; i++){    
            for(int j = 0 ;j < list.size() - i - 1; j++){
            	Map<String, Object> a1= list.get(j);
            	Map<String, Object> a2= list.get(j+1);
            	int sn1 = Integer.parseInt(a1.get("orderSn").toString());
            	int sn2 = Integer.parseInt(a2.get("orderSn").toString());
            	if(sn1 < sn2){    
            		Map<String, Object> temp = a1;  
                    list.set(j, a2);
                    list.set(j + 1, temp);  
                }  
            }
        }
	}

	/**
	 * 分类id处理
	 * @author felx
	 * @date 2017-12-29
	 */
	private Integer categoryIdToolFont(Integer categoryId){
		if(null != categoryId){
			String categoryStr = categoryId.toString();
			int temp = 8;
			int length = categoryStr.length();
			if(length != temp){
				categoryStr = categoryStr + "00";
			}
			categoryId = Integer.valueOf(categoryStr);
		}
		return categoryId;
	}
	
	/**
	 * 分类id处理
	 * @author felx
	 * @date 2017-12-29
	 */
	private Integer categoryIdTool(Integer categoryId){
		if(null != categoryId){
			String categoryStr = categoryId.toString();
			int temp = 8;
			int length = categoryStr.length();
			int cou =  temp - length;
			if(cou>0){
				for(int i=0;i<cou;i++){
					categoryStr = categoryStr + "9";
				}
			}
			categoryId = Integer.valueOf(categoryStr);
		}
		return categoryId;
	}

	@Override
	public Map<String, Object> searchFaceGoods(SearchGoods search) {
		if(search.getRows()>MAX_ROWS_NUM){
			return null;
		}
		if(search.getPage() > MAX_PAGE_NUM){
			search.setPage(1);
		}
		if(search.isNullByFace()){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
			try {
				setFaceSolrServer();
				SolrParams faceQuery = search.getFaceQuery();
				QueryResponse rsp = faceSolrServer.query(faceQuery);
				SolrDocumentList solrList = rsp.getResults();
				Long total = solrList.getNumFound();
				if(total > 150){
					total = 150L;
				}
				map.put("total", total);
				List<GoodsInfo> goodsInfoList = rsp.getBeans(GoodsInfo.class);
				for(GoodsInfo goodsInfo : goodsInfoList){
					goodsInfo.setImage(Constants.IMAGE_URL+"goodsImg/"+goodsInfo.getImage());
					goodsInfo.setPrice(goodsInfo.getMarketPrice().toString());
					goodsInfo.setGoodsName(goodsInfo.getTitle());
					Map sellerGoods = sellerGoodsI.getSellerGoodsByGoodsId(goodsInfo.getId());
					if(sellerGoods!=null && sellerGoods.get("salePrice")!=null ){
						goodsInfo.setSalePrice(Double.parseDouble(sellerGoods.get("salePrice").toString()));
//						goodsInfo.setSellerGoodsId(sellerGoods.get("id").toString());
						goodsInfo.setBatchQuantity(Integer.parseInt(sellerGoods.get("batchQuantity")!=null?sellerGoods.get("batchQuantity").toString():"1"));
						goodsInfo.setOrderQuantity(Integer.parseInt(sellerGoods.get("orderQuantity")!=null?sellerGoods.get("orderQuantity").toString():"1"));
					}
				}
				map.put("goodsList", goodsInfoList);
				List<FacetField> facetFields = rsp.getFacetFields();
				loadCacheData();
				for(FacetField facetField : facetFields){
					String facetName = facetField.getName();
					List<Count> countList = facetField.getValues();
					List<Object> tempCountList = new ArrayList();
					Map<String, Object> countMap = null;
					List<Map.Entry> mappingList = null;
					Map sumMap = new TreeMap(); 
					Collections.sort(countList, new Comparator<Count>() {
			            public int compare(Count arg0, Count arg1) {
			                return arg0.getName().compareTo(arg1.getName());
			            }
			        });
					Map otherMap = new HashMap();
					for(Count count : countList){
						countMap = new HashMap();
						Integer id = null;
						try {
							id =Integer.parseInt(count.getName());
						} catch (Exception e) {
							throw new LogoException(e);
						}
						Long countL = count.getCount();
						countMap.put("count", countL);
						if(StringUtils.equals(facetName, "attrValueId")){
							Map attrMap = (Map)attrCache.get(Long.valueOf(id));
							if(attrMap != null && !attrMap.isEmpty()){
								convertAttr(countMap, id, attrMap, sumMap);
							} else{
								continue;
							}
						} else if(StringUtils.equals(facetName, "brandId")){
							String brandName = (String)brandCache.get(id);
							if(StringUtils.isBlank(brandName)){
								continue;
							}
							countMap.put("brandId", id);
							countMap.put("brandName", brandName);
							tempCountList.add(countMap);
						} else if(StringUtils.equals(facetName, "categoryId")){
							String categoryName = (String)categoryCache.get(id);
							countMap.put("categoryId", id);
							countMap.put("categoryName", categoryName);
							if(StringUtils.isBlank(categoryName)){
								continue;
							} else if(StringUtils.equals(categoryName, "其他")){
								otherMap.putAll(countMap);
								continue;
							}
							int isLeaf = categoryI.findCategoryIsLeaf(id);
							if(isLeaf==1){
								tempCountList.add(countMap);
							}	
						} else{
							countMap.put("id", id);
							tempCountList.add(countMap);
						}
					}
					map.put("attrs", mappingList);//不管是否有数据都返回出这个attrs
					if(StringUtils.equals(facetName, "attrValueId")){
						mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
						Collections.sort(mappingList, new Comparator<Map.Entry>(){
							   public int compare(Map.Entry mapping1,Map.Entry mapping2){
								   List list1 = (List)mapping1.getValue();
								   Map map1 = (Map)list1.get(0);
								   List list2 = (List)mapping2.getValue();
								   Map map2 = (Map)list2.get(0);
							    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
							   }
						}); 
						map.put("attrs", mappingList);
					} else if(StringUtils.equals(facetName, "brandId")){
						map.put("brands", tempCountList);
					} else if(StringUtils.equals(facetName, "categoryId")){
						if(otherMap != null && !otherMap.isEmpty()){
							tempCountList.add(otherMap);
						}
						map.put("categorys", tempCountList);
					} else{
						map.put(facetName, tempCountList);
					}
				}
			List<Map> attrArray = new ArrayList<Map>();
			List<Map<String, Object>> attrData = new ArrayList<Map<String, Object>>();
			if(map.get("attrs")==null){
				map.put("attrs", new ArrayList<>());
			}else{
				List<Map.Entry> attrs = (List<Map.Entry>) map.get("attrs");
				for (Entry entry : attrs) {
					Map<String, Object> attr = new HashMap<String, Object>();
					attr.put("name", entry.getKey());
					attr.put("data", entry.getValue());
					attrArray.add(attr);
				}
				for (Map arrary : attrArray) {
					attrData = (List<Map<String, Object>>) arrary.get("data");
					if(attrData!=null&&attrData.size()>1){
						sortAttr(attrData);
					}
				}
			}
			map.put("attrArray", attrArray);
			if(search.getCategoryId()!=null){
				int isLeaf = categoryI.findCategoryIsLeaf(search.getCategoryId());
				if(isLeaf!=1){
					map.put("attrArray", new ArrayList<>());
					map.put("attrs", new ArrayList<>());
				}
			}else{
				map.put("attrArray", new ArrayList<>());
				map.put("attrs", new ArrayList<>());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	@Deprecated
	public Map<String, Object> searchFaceGoods(String keyword,
			Integer dealerId, Integer brandId, Integer categoryId,
			Integer[] attrValueIds, String[] sortFields,
			Integer[] sortFlags, int page, int rows) {
		if(rows>MAX_ROWS_NUM){
			return null;
		}
		if(page > MAX_PAGE_NUM){
			page = 1;
		}
		if(StringUtils.isBlank(keyword) &&  brandId==null && categoryId==null && attrValueIds==null){
			return null;
		}
		Map<String, Object> map = new HashMap();
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		try{
			setFaceSolrServer();
			if(StringUtils.isBlank(keyword)){
				params.append("*:*");
			} else{
				keyword = getEscapeChar(keyword);
				params.append("(");
				params.append("text:'"+keyword + "'");
				params.append(")");
				keyword = keyword.replace("-", "");
				params.append(" OR (");
				params.append("text:'"+keyword+"'");
				params.append(")");
			}
			/**分类过滤*/
//			if(categoryId != null && categoryId > 0){
//				params.append("((");
//				params.append("categoryId:"+"["+categoryIdToolFont(categoryId)+" TO "+this.categoryIdTool(categoryId)+"]");
//				params.append(")");
//				
//				params.append(" OR (");
//				params.append("categoryId:"+categoryId);
//				params.append("))");
//			}
			query = new SolrQuery();
			query.setQuery(params.toString());
			/**商家过滤*/
/*			if(dealerId != null && dealerId > 0){
				query.addFilterQuery("dealerId:"+dealerId);
			}*/
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
			if(sortFields != null && sortFields.length > 0){
				int sortFielsCount = sortFields.length;
				for(int i = 0; i < sortFielsCount; i++){
					if(sortFlags[i]==0){
						query.addSort(sortFields[i], SolrQuery.ORDER.asc);
					} else{
						query.addSort(sortFields[i], SolrQuery.ORDER.desc);
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
			QueryResponse rsp = faceSolrServer.query(query);
			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			if(total > 100){
				total = 100L;
			}
			map.put("total", total);
			List<GoodsInfo> goodsInfoList = rsp.getBeans(GoodsInfo.class);
			for(GoodsInfo goodsInfo : goodsInfoList){
				goodsInfo.setImage(Constants.IMAGE_URL+"goodsImg/"+goodsInfo.getImage());
				goodsInfo.setPrice(goodsInfo.getMarketPrice().toString());
				goodsInfo.setGoodsName(goodsInfo.getTitle());
				Map sellerGoods = sellerGoodsI.getSellerGoodsByGoodsId(goodsInfo.getId());
				if(sellerGoods!=null && sellerGoods.get("salePrice")!=null ){
					goodsInfo.setSalePrice(Double.parseDouble(sellerGoods.get("salePrice").toString()));
//					goodsInfo.setSellerGoodsId(sellerGoods.get("id").toString());
					goodsInfo.setBatchQuantity(Integer.parseInt(sellerGoods.get("batchQuantity")!=null?sellerGoods.get("batchQuantity").toString():"1"));
					goodsInfo.setOrderQuantity(Integer.parseInt(sellerGoods.get("orderQuantity")!=null?sellerGoods.get("orderQuantity").toString():"1"));
				}
			}
			map.put("goodsList", goodsInfoList);
			List<FacetField> facetFields = rsp.getFacetFields();
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = null;
					try {
						id =Integer.parseInt(count.getName());
					} catch (Exception e) {
						throw new LogoException(e);
					}
					Long countL = count.getCount();
					countMap.put("count", countL);
					if(StringUtils.equals(facetName, "attrValueId")){
						Map attrMap = (Map)attrCache.get(Long.valueOf(id));
						if(attrMap != null && !attrMap.isEmpty()){
							convertAttr(countMap, id, attrMap, sumMap);
						} else{
							continue;
						}
					} else if(StringUtils.equals(facetName, "brandId")){
						String brandName = (String)brandCache.get(id);
						if(StringUtils.isBlank(brandName)){
							continue;
						}
						countMap.put("brandId", id);
						countMap.put("brandName", brandName);
						tempCountList.add(countMap);
					} else if(StringUtils.equals(facetName, "categoryId")){
						String categoryName = (String)categoryCache.get(id);
						countMap.put("categoryId", id);
						countMap.put("categoryName", categoryName);
						if(StringUtils.isBlank(categoryName)){
							continue;
						} else if(StringUtils.equals(categoryName, "其他")){
							otherMap.putAll(countMap);
							continue;
						}
						int isLeaf = categoryI.findCategoryIsLeaf(id);
						if(isLeaf==1){
							tempCountList.add(countMap);
						}	
					} else{
						countMap.put("id", id);
						tempCountList.add(countMap);
					}
				}
				map.put("attrs", mappingList);//不管是否有数据都返回出这个attrs
				if(StringUtils.equals(facetName, "attrValueId")){
					mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
					Collections.sort(mappingList, new Comparator<Map.Entry>(){
						   public int compare(Map.Entry mapping1,Map.Entry mapping2){
							   List list1 = (List)mapping1.getValue();
							   Map map1 = (Map)list1.get(0);
							   List list2 = (List)mapping2.getValue();
							   Map map2 = (Map)list2.get(0);
						    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
						   }
					}); 
					map.put("attrs", mappingList);
				} else if(StringUtils.equals(facetName, "brandId")){
					map.put("brands", tempCountList);
				} else if(StringUtils.equals(facetName, "categoryId")){
					if(otherMap != null && !otherMap.isEmpty()){
						tempCountList.add(otherMap);
					}
					map.put("categorys", tempCountList);
				} else{
					map.put(facetName, tempCountList);
				}
			}
		} catch(Exception e){
			 e.printStackTrace();
		}
		List<Map> attrArray = new ArrayList<Map>();
		List<Map<String, Object>> attrData = new ArrayList<Map<String, Object>>();
		if(map.get("attrs")==null){
			map.put("attrs", new ArrayList<>());
		}else{
			List<Map.Entry> attrs = (List<Map.Entry>) map.get("attrs");
			for (Entry entry : attrs) {
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("name", entry.getKey());
				attr.put("data", entry.getValue());
				attrArray.add(attr);
			}
			for (Map arrary : attrArray) {
				attrData = (List<Map<String, Object>>) arrary.get("data");
				if(attrData!=null&&attrData.size()>1){
					sortAttr(attrData);
				}
			}
		}
		map.put("attrArray", attrArray);
		if(categoryId!=null){
			int isLeaf = categoryI.findCategoryIsLeaf(categoryId);
			if(isLeaf!=1){
				map.put("attrArray", new ArrayList<>());
				map.put("attrs", new ArrayList<>());
			}
		}else{
			map.put("attrArray", new ArrayList<>());
			map.put("attrs", new ArrayList<>());
		}
		return map;
	}
	
	/**
	 * 
	 * 商城店铺查询
	 * @author felx
	 * @date 20160607
	 */
	public Map<String, Object> findStoreList(int page,int row,String keyword){
		if(row>MAX_ROWS_NUM){
			return null;
		}
		if(page > MAX_PAGE_NUM){
			page = 1;
		}
		List<Map> result = new ArrayList<Map>();
		Map<String, Object> map = new HashMap();
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		try{
			setMallShopSolrServer();
			query = new SolrQuery();
			
			if(StringUtils.isBlank(keyword)){
				params.append("*:*");
			} else{
//				String s=escapeQueryChars(keyword);//对关键词含有特殊字符处理
//				params.append("(");
//				params.append("shopName:*"+s+"*");
//				params.append(")");
				keyword = escapeQueryChars(keyword);
				params.append("(");
				params.append("text:'"+keyword + "'");
				params.append(")");
			}
			query.setQuery(params.toString());
			query.setStart((page-1)*row);
			query.setRows(row);
			query.setFacet(true).setFacetMinCount(1);//.setFacetLimit(100)
			QueryResponse rsp = mallShopSolrServer.query(query);
			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			if(total > 100){
				total = 100L;
			}
			map.put("total", total);
			for(SolrDocument solrDocument : solrList){
				result.add(solrDocument);
			}
			map.put("storyList", result);


		} catch(Exception e){
			 e.printStackTrace();
		}
		return map;
	}
	/**
	 * 店铺查询关键字输入非法字符处理
	 * @param s
	 * @return
	 */
	public static String escapeQueryChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
          char c = s.charAt(i);
          // These characters are part of the query syntax and must be escaped
          if (c == '\\' || c == '+' || c == '-' || c == '!'  || c == '(' || c == ')' || c == ':' || c == '·' || c == ',' || c == '.'
                  || c == '^' || c == '[' || c == ']' || c == '\"' || c == '@' || c == '#' || c == '{' || c == '}' || c == '~' || c == '。'
                  || c == '%' || c == '*' || c == '?' || c == '|' || c == '&'  || c == ';' || c == '/' || c == '$' || c == '-'
                  || Character.isWhitespace(c)) {

          }else{
        	  sb.append(c);
          }
        }
        String ss=sb.toString().trim();
        return ss;
      }
	/**
	 * saas商品联想
	 * @author felx
	 * @date 2016-3-30
	 */
	public List<Map> searchSuggestGoods(String keyword){
		if(StringUtils.isBlank(keyword)){
			return null;
		}
		List<Map> result = new LinkedList<Map>();
		SolrQuery query = null;
		StringBuffer params = new StringBuffer();
		try{
			setSaasSolrServer();
			params.append("*:*");
			query = new SolrQuery();
			query.setQuery(params.toString());
			
			query.setFacet(true);//设置facet=on
			query.addFacetField(new String[] { "orderNum", "model" });//设置需要facet的字段
			query.setFacetPrefix(keyword.toLowerCase());
			query.setFacetLimit(10);//限制facet返回的数量
			QueryResponse response = saasSolrServer.query(query);
			List<FacetField> facets = response.getFacetFields();//返回的facet列表
			for (FacetField facet : facets) {
			    List<Count> counts = facet.getValues();
			    for (Count count : counts) {
			        String name = count.getName();
			        long num = count.getCount();
			        Map<String, Object> countMap = new HashMap<String, Object>();
			        if(result.isEmpty()){
			        	countMap.put("name", name);
	        			countMap.put("num", num);
	        			result.add(countMap);
			        }else{
			        	for(Map map : result){
			        		if(name.equals(map.get("name"))){
			        			num = num + Long.valueOf(map.get("num").toString());
			        			map.put("num", num);
			        			break;
			        		}else{
			        			countMap.put("name", name);
			        			countMap.put("num", num);
			        			result.add(countMap);
			        			break;
			        		}
			        	}
			        }
			    }
			}
			
			
		} catch(Exception e){
			 e.printStackTrace();
		}
		for(Map map : result){
			map.put("name", StringUtils.upperCase(map.get("name").toString()));
		}
		return result;
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

	@Override
	public Map<String, Object> getGoodsSeries(String spuId) throws Exception {
		SolrUtil solrUtil = new SolrUtil(SAAS_SERIES_URL);
		solrUtil.addFilterQuery("spuId", spuId);
		solrUtil.addSort("orderSn", ORDER.asc);
		solrUtil.setRows(1000);
		QueryResponse data = solrUtil.getResponse();
		List<GoodsAttrInfo> goodsAttrInfoList = data.getBeans(GoodsAttrInfo.class);
		
		Map<Long,String> map = new HashMap<>();
		Map<String,Object> result = new HashMap<>();
		Map<String,Set<String>> attrMap = new HashMap<>();
		List<Map<String, Object>> keyList = new ArrayList<>();
		List attrList = new ArrayList<>();
		for (GoodsAttrInfo goodsAttrInfo : goodsAttrInfoList) {
			String value = map.get(goodsAttrInfo.getSellerGoodsId());
			if(value==null){
				map.put(goodsAttrInfo.getSellerGoodsId(), goodsAttrInfo.getAttrValue());
			}else{
				map.put(goodsAttrInfo.getSellerGoodsId(), value+","+goodsAttrInfo.getAttrValue());
			}
			Set<String> attrValue = attrMap.get(goodsAttrInfo.getAttrName());
			if(attrValue==null||attrValue.isEmpty()){
				attrValue = new LinkedHashSet<>();
			}
			attrValue.add(goodsAttrInfo.getAttrValue());
			attrMap.put(goodsAttrInfo.getAttrName(), attrValue);
		}
		for (Entry<Long, String> entity : map.entrySet()) {
			Map<String, Object> entityMap = new HashMap<>();
			entityMap.put("attrValue", entity.getValue().split(","));
			entityMap.put("sellerGoodsId", entity.getKey());
			keyList.add(entityMap);
		}
		for (Entry<String, Set<String>> entity : attrMap.entrySet()) {
			Map<String, Object> entityMap = new HashMap<>();
			entityMap.put("attrName", entity.getKey());
			entityMap.put("attrValue", entity.getValue());
			attrList.add(entityMap);
		}
		result.put("attrList", attrList);
		result.put("keyList", keyList);
		return result;//resultList
	}

	@Override
	public Map<String, Object> searchModelSelectionGoods(SearchGoods search) {
		
		if(search.getRows()>MAX_ROWS_NUM){
			return null;
		}
		if(search.getPage() > MAX_PAGE_NUM){
			search.setPage(1);
		}
		if(search.isNullBySaas()){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		
		try {
			setSaasModelSelectionServer();

            /**
             * create by yr on 2018-11-19
             * 修改接口
             */
            SolrParams saasQuery ;
            String isRootNode = new String();
            if(search.getModelSelectionCateId()!=null){
                 isRootNode = categoryI.findNodeByCateId(search.getModelSelectionCateId()).get("isRootNode").toString();
                    if(isRootNode.equals("1")){
                        saasQuery = search.getModelSelectionSaasQuery();
                }else{
                    saasQuery = getModelSelectionSaasQueryForIos(search);
                }
            }else{
                saasQuery = search.getModelSelectionSaasQuery();
            }
			QueryResponse rsp = modelSelectionSolrServer.query(saasQuery);


			SolrDocumentList solrList = rsp.getResults();
			Long total = solrList.getNumFound();
			if(total > 200){
				total = 200L;
			}
			map.put("total", total);
			List<GoodsInfo> goodsInfoList = rsp.getBeans(GoodsInfo.class);
			dealSaasGoodsInfoList(goodsInfoList,search);
	        map.put("searchList", goodsInfoList);
			List<FacetField> facetFields = rsp.getFacetFields();
			if(search.getKeys()!=null && search.getKeys().length>0){
				return map;
			}
			loadCacheData();
			for(FacetField facetField : facetFields){
				String facetName = facetField.getName();
				List<Count> countList = facetField.getValues();
				List<Object> tempCountList = new ArrayList();
				Map<String, Object> countMap = null;
				List<Map.Entry> mappingList = null;
				Map sumMap = new TreeMap(); 
				Collections.sort(countList, new Comparator<Count>() {
		            public int compare(Count arg0, Count arg1) {
		                return arg0.getName().compareTo(arg1.getName());
		            }
		        });
				Map otherMap = new HashMap();
				for(Count count : countList){
					countMap = new HashMap();
					Integer id = Integer.parseInt(count.getName());
					Long countL = count.getCount();
					countMap.put("count", countL);
					switch (facetName) {
					case "attrValueId":
							Map attrMap = (Map)attrCache.get(Long.valueOf(id));
							if(attrMap != null && !attrMap.isEmpty()){
								convertAttr(countMap, id, attrMap, sumMap);
							} else{
								continue;
							}
						break;

					default:
						break;
					}
				}
				map.put("params", mappingList);
				switch (facetName) {
				case "attrValueId":
						mappingList = new ArrayList<Map.Entry>(sumMap.entrySet());
						Collections.sort(mappingList, new Comparator<Map.Entry>(){
							   public int compare(Map.Entry mapping1,Map.Entry mapping2){
								   List list1 = (List)mapping1.getValue();
								   Map map1 = (Map)list1.get(0);
								   List list2 = (List)mapping2.getValue();
								   Map map2 = (Map)list2.get(0);
							    return String.valueOf(map1.get("attrId")).compareTo(String.valueOf(map2.get("attrId")));
							   }
						}); 
						map.put("params", mappingList);
					break;
				
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new GdbmroException(-1, "get list faild");
		}
		return map;
	}



    /**
     * 解析商品属性
     * @return
     */
    public static Integer[] parsingAttrValueIds(SearchGoods searchGoods){
        Integer[] attrValueIdInt = null;
        if(StringUtils.isNoneBlank(searchGoods.getAttrValueIds())){
            String[] attrValueIdStr = searchGoods.getAttrValueIds().split(",");
            attrValueIdInt = new Integer[attrValueIdStr.length];
            for(int i = 0,size=attrValueIdStr.length; i < size; i++){
                attrValueIdInt[i] = Integer.parseInt(attrValueIdStr[i].trim());
            }
        }
        return attrValueIdInt;
    }
    /**
     * sass(Query)
     * @return
     */
    public SolrParams getModelSelectionSaasQueryForIos(SearchGoods searchGoods) {
        SolrQuery query = null;
        StringBuffer params = new StringBuffer();
        Integer[] attrValueIds = parsingAttrValueIds(searchGoods);
        String[] sortFieldArr = null;
        Integer[] sortFlagInt = null;
        if(StringUtils.isNoneBlank(searchGoods.getSortFields())){
            sortFieldArr = searchGoods.getSortFields().split(",");
            String[] sortFlagArr = searchGoods.getSortFlags().split(",");
            int sortFieldsize = sortFieldArr.length;
            sortFlagInt = new Integer[sortFieldsize];
            for(int i = 0; i < sortFieldsize; i++){
                sortFlagInt[i] = Integer.parseInt(sortFlagArr[i].trim());
            }
        }
        params.append("*:*");
        query = new SolrQuery();
        query.setQuery(params.toString());

        /**分类过滤*/
        if(searchGoods.getModelSelectionCateId() != null && searchGoods.getModelSelectionCateId() > 0){
            /**
             * create by yr on 2018-11-16判断借点是否为末节点
             */
            //首先验证节点是否是末节点
            StringBuffer sb = new StringBuffer();
            String strQuery = new String();
            String isRootNode = categoryI.findNodeByCateId(searchGoods.getModelSelectionCateId()).get("isRootNode").toString();
            if(isRootNode.equals("1")){
                sb.append("selectionCateId:"+searchGoods.getModelSelectionCateId()+"*");
                strQuery = sb.toString();
                query.addFilterQuery(strQuery);
            }else{
                /**查询非顶级的所有选型分类*/
                List<Integer> leafList = Lists.newArrayList();
                List<ModelSelectionCategory> allCategoryList = categoryI.getNoTopModelSelectionCategory();
                leafList = getLeafList(searchGoods.getModelSelectionCateId(),allCategoryList,leafList);
                for(Integer cateId : leafList){
                    sb.append("selectionCateId:"+cateId).append(" or ");
                }
                System.out.println(sb.toString());
                if(StringUtils.isNotBlank(sb.toString())){
                    strQuery = StringUtils.substring(sb.toString(),0,sb.toString().length()-4);
                }
                query.setQuery(strQuery);
            }


        }
        /**选型过滤*/
        if(attrValueIds != null){
            for(Integer attrValueId : attrValueIds){
                query.addFilterQuery("attrValueId:"+attrValueId);
            }
        }
        /**品牌过滤*/
        if(searchGoods.getBrandId() != null && searchGoods.getBrandId() > 0){
            query.addFilterQuery("brandId:"+searchGoods.getBrandId());
        }
        if(searchGoods.getAdvantage()!=null&&"0".equals(searchGoods.getAdvantage())){
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
        query.setStart((searchGoods.getPage()-1)*searchGoods.getRows());
        query.setRows(searchGoods.getRows());

//		query.setFacet(true).setFacetMinCount(1).setFacetLimit(100);
        query.setFacet(true).setFacetMinCount(1);
        query.addFacetField("attrValueId");
        return query;
    }

    public List<Integer> getLeafList( Integer modelSelectionCateId ,List<ModelSelectionCategory> allCategoryList,List<Integer> list){
        /**第一次先判断allCategoryList 的父节点为参数modelselectionCateid*/
        for(ModelSelectionCategory modelSelectionCategory:allCategoryList){
            if(modelSelectionCategory.getParentId().equals(modelSelectionCateId)){
                if(modelSelectionCategory.getIsRootNode().equals(1)){
                    list.add(modelSelectionCategory.getCateId());
                }else{
                    getLeafList(modelSelectionCategory.getCateId(),allCategoryList,list);
                }
            }
        }
        return list;

    }







}