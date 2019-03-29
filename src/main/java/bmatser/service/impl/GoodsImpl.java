package bmatser.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import bmatser.dao.AdverImageMapper;
import bmatser.dao.BigCustomerPackageMapper;
import bmatser.dao.CategoryMapper;
import bmatser.dao.GoodsCombinationMapper;
import bmatser.dao.GoodsMapper;
import bmatser.dao.GrouponActivityMapper;
import bmatser.model.SellerGoods;
import bmatser.pageModel.GoodsDetail;
import bmatser.pageModel.GoodsInfo;
import bmatser.pageModel.MallGoodsInfo;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.SearchGoods;
import bmatser.service.GoodsI;
import bmatser.service.SolrI;
import bmatser.util.Analyzer;
import bmatser.util.Constants;
import bmatser.util.DESCoder;
import bmatser.util.LogoException;

/**
 * 商品
 * @author felx
 * 2017-9-1
 */
@Service("goodsService")
public class GoodsImpl implements GoodsI {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private GoodsMapper goodsDao;
	
	@Autowired
	private SolrI solrService;
	
	@Autowired
	private GrouponActivityMapper grouponActivityMapper;
	
	@Autowired
	private AdverImageMapper adverImageMapper;
	
	@Autowired
	private BigCustomerPackageMapper bigCustomerPackageDao;
	
	@Autowired
	private GoodsCombinationMapper goodsCombinationDao;
	
	
	
	@Override
	public List findHotGoods(String payTime, int page, int rows) {
		return goodsDao.findHotGoods(payTime, page, rows);
	}
	private String version;

	/**
	 * 查询直发区商品
	 * @param keyword
	 * @param cateId
	 * @param priceOrder
	 * @param stockOrder
	 * @param page
	 * @param rows
	 * @return
	 * 2017-9-5
	 *
	 */
	@Override
	public List findSellGoodsByKeyword(String keyword, Integer cateId, Integer brandId, String priceOrder, String stockOrder, int page, int rows) {
		List<String> keywords = Analyzer.IKAnalyzer(keyword);
		return goodsDao.findSellGoodsByKeyword(keywords, cateId, brandId, priceOrder, stockOrder, page, rows);
	}

	/**
	 * 查询面价查询区商品
	 * @param keyword
	 * @param cateId
	 * @param brandId
	 * @param page
	 * @param rows
	 * @return
	 * 2017-9-5
	 *
	 */
	@Override
	public List findBaseGoodsByKeyword(String keyword, Integer cateId, Integer brandId, int page, int rows) {
		List<String> keywords = Analyzer.IKAnalyzer(keyword);
		return goodsDao.findBaseGoodsByKeyword(keywords, cateId, brandId, page, rows);
	}

	/**
	 * 获取直发区商品分类
	 * @param keyword
	 * @param brandId
	 * @param page
	 * @param rows
	 * @return
	 * 2017-9-9
	 *
	 */
	@Override
	public List findSellGoodsCategory(String keyword, Integer brandId,
			int page, int rows) {
		List<String> keywords = Analyzer.IKAnalyzer(keyword);
		return goodsDao.findSellGoodsCategory(keywords, brandId, page, rows);
	}

	/**
	 * 获取面价查询区商品分类
	 * @param keyword
	 * @param brandId
	 * @param page
	 * @param rows
	 * @return
	 * 2017-9-9
	 *
	 */
	@Override
	public List findBaseGoodsCategory(String keyword, Integer brandId,
			int page, int rows) {
		List<String> keywords = Analyzer.IKAnalyzer(keyword);
		return goodsDao.findBaseGoodsCategory(keywords, brandId, page, rows);
	}

	/**
	 * 取得详细信息中的图片地址，并将其转换最新的图片地址
	 * 
	 * @param img
	 * @param oldImg
	 * @return
	 */
	private String getUrlImg(String img, String oldImg,String path) {
		if (img.indexOf("img") > -1) {
			String reImg = img.substring(img.indexOf("img"));// 截取第一个img以后的字符串
			if (reImg.indexOf("src") > -1) {
				String reSrc = reImg.substring(reImg.indexOf("src"));// 截取第一个src以后的字符串
				if (reSrc.indexOf("\"") > -1) {
					String reDou = reSrc.substring(reSrc.indexOf("\"") + 1);// 截取第一个
					// "
					// 以后的字符串
					String imgSrc = reDou.substring(0, reDou.indexOf("\""));// 字符串中包含的图片信息
//					String filename = imgSrc.substring(imgSrc.lastIndexOf("/") + 1);// 网络上的图片名称
					String repl = reDou.substring(reDou.indexOf("\""));// 处理完图片后面的字符串
					String replace = path + imgSrc;
					if(imgSrc.indexOf("http") <= -1){
					   oldImg = oldImg.replace(imgSrc, replace);
					}
					oldImg = getUrlImg(repl, oldImg,path);
				}
			}
		}
		return oldImg;
	}

	/**
	 * 获取商品属性
	 * @param goodsId
	 * @return
	 * 2017-9-14
	 *
	 */
	@Override
	public List getGoodsAttr(long goodsId) {
		return goodsDao.getGoodsAttr(goodsId);
	}

	@Override
	public Map<String, Object> findGoodsById(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		SellerGoods goods = goodsDao.getShelvesGoodsById(id);
		map.put("goods", goods);
		return map;
	}

	/**
	 * 商城搜索商品（搜索引擎）
	 * @param keyword
	 * @param dealerId
	 * @param brandId
	 * @param categoryId
	 * @param attrValueIds
	 * @param page
	 * @param rows
	 * @return 当前页商品
	 * @author felx
	 * 2017-12-23
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> searchMallGoods(String keyword,
			Integer dealerId, Integer brandId, Integer categoryId,
			Integer[] attrValueIds, String sortFields, String sortFlags, int page, int rows, String alias) {
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
		
		Map mallGoodsMap = solrService.searchMallGoods(keyword, dealerId, brandId, categoryId, attrValueIds, sortFieldArr, sortFlagInt, page, rows,alias);
		if(mallGoodsMap != null && !mallGoodsMap.isEmpty()){
			List<GoodsInfo> goodsList = (List<GoodsInfo>)mallGoodsMap.get("goodsList");
			try {
				DESCoder.instance();
				for(GoodsInfo goodsInfo : goodsList){
					goodsInfo.setGoodsName(DESCoder.encoder(goodsInfo.getGoodsName()));
					String model = goodsInfo.getModel();
					if(StringUtils.isNoneBlank(model)){
						goodsInfo.setModel(DESCoder.encoder(model));
					} else{
						goodsInfo.setModel("");
					}
					String buyNo = goodsInfo.getOrderNum();
					if(StringUtils.isNoneBlank(buyNo)){
						goodsInfo.setOrderNum(DESCoder.encoder(buyNo));
					} else{
						goodsInfo.setOrderNum("");
					}
				}
			} catch (Exception e) {
				logger.error("encryption fail:{}", e.getMessage());
			}
		}
		return mallGoodsMap;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, Object> searchAppMallGoods(String keyword,
			Integer dealerId, Integer brandId, Integer categoryId,
			Integer[] attrValueIds, String sortFields, String sortFlags, int page, int rows, String alias) {
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
		
		Map mallGoodsMap = solrService.searchMallGoods(keyword, dealerId, brandId, categoryId, attrValueIds, sortFieldArr, sortFlagInt, page, rows,alias);
		return mallGoodsMap;
	}
	
	
	
	@Override
	public Map<String, Object> searchSaasGoods(SearchGoods search) throws Exception {
		Map<String, Object> saasGoodsMap = null;
		if(search.isSaas()){
			
			saasGoodsMap = solrService.searchSaasGoods(search);
//			if(saasGoodsMap != null && !saasGoodsMap.isEmpty()){
//				List<GoodsInfo> goodsList = (List<GoodsInfo>)saasGoodsMap.get("goodsList");
//				DESCoder.instance();
//				for(GoodsInfo goodsInfo : goodsList){
//					goodsInfo.setGoodsName(DESCoder.encoder(goodsInfo.getGoodsName()));
//				}
//			};
		}else{
			String[] sortFieldArr = null;
			Integer[] sortFlagInt = null;
			if(StringUtils.isNoneBlank(search.getSortFields())){
				sortFieldArr = search.getSortFields().split(",");
				String[] sortFlagArr = search.getSortFlags().split(",");
				int sortFieldsize = sortFieldArr.length; 
				sortFlagInt = new Integer[sortFieldsize];
				for(int i = 0; i < sortFieldsize; i++){
					sortFlagInt[i] = Integer.parseInt(sortFlagArr[i].trim());
				}
			}
			saasGoodsMap = solrService.searchSaasGoods(search.getKeyword(), 
					search.getDealerId(), 
					search.getBrandId(), 
					search.getCategoryId(), 
					search.parsingAttrValueIds(), 
					sortFieldArr, 
					sortFlagInt, 
					search.getPage(), 
					search.getRows(), 
					search.getAdvantage());
		}
		return saasGoodsMap;
	}
	
	/**
	 * (brandId=266)商品查询
	 * 	 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> searchFaceSaasGoods(SearchGoods search) throws Exception {
		Map<String, Object> saasGoodsMap =  solrService.searchFaceSaasGoods(search);	
		Map<String,Object> map = new HashMap<String,Object>();
		List list = new ArrayList();		
		if(saasGoodsMap != null && !saasGoodsMap.isEmpty()){
			List<GoodsInfo> goodsList = (List<GoodsInfo>)saasGoodsMap.get("goodsList");
			    DESCoder.instance();
				for(int i=0;i<goodsList.size();i++){
					Map m=new HashMap();
					//对商品型号和订货号进行加密
					String title = goodsList.get(i).getTitle();
					String model = goodsList.get(i).getModel();
					String buyNo = goodsList.get(i).getOrderNum();
					if(StringUtils.isNoneBlank(title)){
						m.put("title",DESCoder.encoder(title));//商品型号
					} else{
						m.put("title","");
					}
					if(StringUtils.isNoneBlank(model)){
						m.put("model",DESCoder.encoder(model));//商品型号
					} else{
						m.put("model","");
					}
					if(StringUtils.isNoneBlank(buyNo)){
						m.put("buyNo",DESCoder.encoder(buyNo));//订货号
					} else{
						m.put("buyNo","");
					}
					m.put("goodsId",goodsList.get(i).getId());//商品id(goods表的id)
					m.put("image",goodsList.get(i).getImage());//商品图片
					/*m.put("price",goodsList.get(i).getPrice());//供应商定义的商品价格 
*/					//友众的商品价格=上架价格/0.0714*0.09
					BigDecimal salePrice = new BigDecimal(goodsList.get(i).getSalePrice());
					BigDecimal newPrice1 = salePrice.multiply(new BigDecimal(0.0714)); 
					BigDecimal newPrice2 = newPrice1.divide(new BigDecimal(0.09),2,BigDecimal.ROUND_HALF_UP);  
					m.put("price", newPrice2);
					/*m.put("marketPrice",goodsList.get(i).getMarketPrice());*/
					/*m.put("salePrice",goodsList.get(i).getSalePrice());*///modify 20160615
					/*m.put("sellerGoodsId",goodsList.get(i).getSellerGoodsId());//供应商商品ID
					m.put("batchQuantity",goodsList.get(i).getBatchQuantity());//批量   
					m.put("orderQuantity",goodsList.get(i).getOrderQuantity());//起定量            返回增加起定量和批量 20160614
*/					list.add(i, m);
					
				}
				map.put("goodsList", list);
				map.put("total", saasGoodsMap.get("total"));
				map.put("categorys", saasGoodsMap.get("categorys"));
				map.put("parentCategorys", saasGoodsMap.get("parentCategorys"));
				/*map.put("attrArray", saasGoodsMap.get("attrArray"));*///返回增加属性 20160614
		}
		return map;
	}
	
	@Override
	public Map<String, Object> searchMobGoods(String keyword, Integer brandId,
			Integer categoryId, String sortFields, String sortFlags, int page,
			int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sort = new StringBuilder("ORDER BY ");
		if(StringUtils.isNotBlank(sortFields)){
			sort.append(sortFields);
		}else{
			sort.append("sg.id ");
		}
		sort.append(" ");
		if(StringUtils.isNotBlank(sortFlags)){
			sort.append(sortFlags);
		}
		map.put("goodsList", goodsDao.findByMobPage(keyword,brandId,categoryId,sort.toString(),page,rows));
		map.put("count", goodsDao.findByMobPageCount(keyword,brandId,categoryId,sort.toString()));
		return map;
	}

	@Override
	public Map<String, Object> searchMobGoodsPrice(String keyword,
			Integer brandId, Integer categoryId, String sortFields,
			String sortFlags, int page, int rows) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sort = new StringBuilder("ORDER BY ");
		if(StringUtils.isNotBlank(sortFields)){
			sort.append(sortFields);
		}else{
			sort.append("g.id ");
		}
		sort.append(" ");
		if(StringUtils.isNotBlank(sortFlags)){
			sort.append(sortFlags);
		}
		map.put("goodsList", goodsDao.findByMobPricePage(keyword,brandId,categoryId,sort.toString(),page,rows));
		map.put("count", goodsDao.findByMobPricePageCount(keyword,brandId,categoryId,sort.toString()));
		return map;
	}

	@Override
	public List findGoodsCategory(String keyword, Integer brandId, int page,
			int rows) {
		List<String> keywords = Analyzer.IKAnalyzer(keyword);
		return goodsDao.findGoodsCategory(keywords, brandId, page, rows);
	}

	
	//面价查询数据接口返回的数据中将没用的数据去掉 20160603
	public Map<String, Object> searchPCFaceGoods(String keyword,
			Integer dealerId, Integer brandId, Integer categoryId,
			Integer[] attrValueIds, String sortFields, String sortFlags,
			int page, int rows) throws Exception {
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
		
		Map faceGoodsMap = solrService.searchFaceGoods(keyword, dealerId, brandId, categoryId, attrValueIds, sortFieldArr, sortFlagInt, page, rows);
		Map<String,Object> map = new HashMap<String,Object>();
		List list = new ArrayList();		
		if(faceGoodsMap != null && !faceGoodsMap.isEmpty()){
			List<GoodsInfo> goodsList = (List<GoodsInfo>)faceGoodsMap.get("goodsList");
			    DESCoder.instance();
				for(int i=0;i<goodsList.size();i++){
					Map m=new HashMap();
					//对商品型号和订货号进行加密
					String title = goodsList.get(i).getTitle();
					String model = goodsList.get(i).getModel();
					String buyNo = goodsList.get(i).getOrderNum();
					if(StringUtils.isNoneBlank(title)){
						m.put("title",DESCoder.encoder(title));//商品型号
					} else{
						m.put("title","");
					}
					if(StringUtils.isNoneBlank(model)){
						m.put("model",DESCoder.encoder(model));//商品型号
					} else{
						m.put("model","");
					}
					if(StringUtils.isNoneBlank(buyNo)){
						m.put("buyNo",DESCoder.encoder(buyNo));//订货号
					} else{
						m.put("buyNo","");
					}
					m.put("image",goodsList.get(i).getImage());//商品图片
					m.put("price",goodsList.get(i).getPrice());//供应商定义的商品价格 
					m.put("marketPrice",goodsList.get(i).getMarketPrice());
					m.put("salePrice",goodsList.get(i).getSalePrice());//modify 20160615
//					m.put("sellerGoodsId",goodsList.get(i).getSellerGoodsId());//供应商商品ID
					m.put("batchQuantity",goodsList.get(i).getBatchQuantity());//批量   
					m.put("orderQuantity",goodsList.get(i).getOrderQuantity());//起定量            返回增加起定量和批量 20160614
					list.add(i, m);
					
				}
				map.put("goodsList", list);
				map.put("total", faceGoodsMap.get("total"));
				map.put("attrArray", faceGoodsMap.get("attrArray"));//返回增加属性 20160614
		}
		return map;
	}
	/**
	 * 输入框联想提示
	 * @author felx
	 * @date 2016-3-30
	 */
	public List suggestGoods(String keyword){
		return solrService.searchSuggestGoods(keyword);
	}
	/**
	 * 
	 * @param sellerGoodsId
	 * @param goods 
	 * @param activity[] 0特卖,1现金券2满减3返现4满赠
	 * @param isSale
	 * @param isRate
	 * @param isFullcut
	 * @param isFullgive
	 * @param isRebates
	 * @return
	 */
	public List<Map<String, Object>> getGoodsActivity(String sellerGoodsId,String[] activity, GoodsDetail goods) {
		List<Map<String, Object>> activityList = new ArrayList<Map<String,Object>>();
		for (int i = 0,len = activity.length; i < len; i++) {
			if(StringUtils.equals("1", activity[i])){
				String  desc = null;
				if(i==1){
					desc = goodsDao.getGoodsRateDec(sellerGoodsId);
				}else if(i==5){
					desc = grouponActivityMapper.findGrouponDesc(sellerGoodsId);
				}else if(i==6){
//					desc ="可获得工币"+goods.getBean()+"个,等值于"+(new BigDecimal(goods.getBean()).divide(new BigDecimal(100)))+"元现金";
				}else{
					if("2".equals(goods.showChannel())){
						desc = goodsDao.getMallGoodsActivityDec(sellerGoodsId,i);
					}else{
						desc = goodsDao.getGoodsActivityDec(sellerGoodsId,i);
					}
				}
				if(StringUtils.isNotBlank(desc)){
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("activityType", i);
					map.put("desc", desc);
					activityList.add(map);
				}
			}
		}
		return activityList;
	}

	/**
	 *商品详情(Saas)
	 */
	@Override
	public Object getGoodsDetailBySaas(String sellerGoodsId,
			String dealerId) throws Exception {
		GoodsDetail goods = goodsDao.getGoodsDetail(sellerGoodsId,1);
		if(goods != null){
			//代理证书
//			goods.setProxyCertificate(goodsDao.getProxyCertificate(Integer.parseInt(goods.getBrandId()),"939817"));//goods.getSellerId().toString()
			//查询当前用户是否是大客户返回套餐id
			Integer bigCustomerPackageId = bigCustomerPackageDao.isExist(Integer.valueOf(dealerId));
			//查询商品是否是套餐商品返回折扣
			BigDecimal rate = bigCustomerPackageDao.goodsIsPackage(sellerGoodsId,bigCustomerPackageId);
            /**
             * 以前的折扣使用了换新的折扣
             */
            BigDecimal rate1 = goodsDao.getDiscountByGoodsNo(goods.getGoodsNo(),dealerId);
            BigDecimal rate2 = goodsDao.getDiscountBySeries(goods.getSeries(),dealerId);
            if(rate1 != null){
                rate = rate1;
            }
            else if(rate2 != null) {
                rate = rate2;
            }
            else{
                rate = rate;
            }
			if(rate!=null&&rate.compareTo(BigDecimal.ZERO)!=0){
				goods.setSalePrice((goods.getMarketPrice().multiply(rate)).setScale(2, BigDecimal.ROUND_HALF_UP));
//				goods.setIsBean(0);
//				goods.setIsFullcut(0);
//				goods.setIsFullgive(0);
				goods.setIsJhs(0);
				goods.setIsRate(0);
//				goods.setIsRebates(0);
				goods.setIsxsms(0);
				goods.setIsCanJhs(0);
//				goods.setIsBigCustomer(1);
				goods.setAttrs(goodsDao.getGoodsAttr(goods.getgId()));//属性
				if(StringUtils.isNotBlank(goods.getDetail())){//详情图片
					String detail = "";
					if(goods.getSellerId()!=null && goods.getSellerId() ==9407609){
						detail = getUrlImg(goods.getDetail(), goods.getDetail(), "http:");
					}else{
						detail = getUrlImg(goods.getDetail(), goods.getDetail(), Constants.IMAGE_URL + "goodsImg/");
					}
					goods.setDetail(detail);
				}		
			}else{
				toinitGoodsDetail(goods);
				/** saas 单独逻辑*/
				if(goods.getIsJhs()==1){
					int isGroup = grouponActivityMapper.isGroupGoods(dealerId,goods.getSaleGId().toString());
					Map  groupInfo = grouponActivityMapper.getGroupInfoByApp(dealerId,goods.getSaleGId().toString());
					goods.toGroupInfoByApp(groupInfo);
					if(isGroup==0){
						goods.setIsCanJhs(0);
					}else{
						goods.setIsCanJhs(1);
					}
				}
			}			
//			DESCoder.instance();
//			for (Object o : goods.getAttrs()) {
//				Map attr= (Map)o;
//				Object attrValue = attr.get("attrValue");
//				if(attrValue!=null && StringUtils.isNotBlank(attrValue.toString())){
//					attr.put("attrValue", DESCoder.getEncoderLeftStr(attrValue.toString()));
//				}
//			}
			/** saas 单独逻辑*/
		}else{
			throw new Exception("商品不存在");
		}
		return goods;
	}
	
	
	/**
	 * 获取商品详情(APP)
	 * @param sellerGoodsId
	 * @return
	 * 2017-9-14
	 * @throws Exception 
	 *
	 */
	@Override
	public Object getGoodsDetail(String sellerGoodsId) throws Exception {
		GoodsDetail goods = goodsDao.getGoodsDetail(sellerGoodsId,2);
		if(goods != null){
			toinitGoodsDetail(goods);
			goods.setProxyCertificate(goodsDao.getProxyCertificate(Integer.parseInt(goods.getBrandId()),"939817"));//goods.getSellerId().toString()
		}else{
			throw new Exception("商品不存在");
		}
		return goods;
	}
	/**
	 * 获取商品详情(APP)
	 * @param sellerGoodsId
	 * @return
	 * 2017-9-14
	 * @throws Exception 
	 *
	 */
//	@Override
//	public Object getGoodsDetailByApp(String sellerGoodsId,String dealerId,PageMode model) throws Exception {
//		GoodsDetail goods = goodsDao.getGoodsDetail(sellerGoodsId,1);
//		this.version = model.getStringValue("version");
//		if(goods != null){
//			goods.setIsCombination(0);
//			//根据sellerGoodsId查出该商品是否存在组合套餐
//			List<Map<String,Object>> list = goodsCombinationDao.selectGoodsCombination(sellerGoodsId);
//			if(list!=null&&list.size()>0){
//				goods.setIsCombination(1);
//			}
//			//代理证书
//			goods.setProxyCertificate(goodsDao.getProxyCertificate(Integer.parseInt(goods.getBrandId()),"939817"));//goods.getSellerId().toString()
//			//查询当前用户是否是大客户返回套餐id
//			Integer bigCustomerPackageId = bigCustomerPackageDao.isExist(Integer.valueOf(dealerId));
//			//查询商品是否是套餐商品返回折扣
//			BigDecimal rate = bigCustomerPackageDao.goodsIsPackage(sellerGoodsId,bigCustomerPackageId);
//			if(rate!=null&&rate.compareTo(BigDecimal.ZERO)!=0){
//				goods.setSalePrice(goods.getMarketPrice().multiply(rate));
//				goods.setIsBean(0);
//				goods.setIsFullcut(0);
//				goods.setIsFullgive(0);
//				goods.setIsGroupon(0);
//				goods.setIsRate(0);
//				goods.setIsRebates(0);
//				goods.setIsSale(0);
//				goods.setIsSupportGroupon(0);
//				goods.setIsBigCustomer(1);
//				goods.setAttrs(goodsDao.getGoodsAttr(goods.getGoodsId()));//属性
//				if(StringUtils.isNotBlank(goods.getDetail())){//详情图片
//					String detail = "";
//					if(goods.getSellerId()!=null && goods.getSellerId() ==9407609){
//						detail = getUrlImg(goods.getDetail(), goods.getDetail(), "http:");
//					}else{
//						detail = getUrlImg(goods.getDetail(), goods.getDetail(), Constants.IMAGE_URL + "goodsImg/");
//					}
//					goods.setDetail(detail);
//				}
//			}else{
//				toinitGoodsDetail(goods);
//				/** APP 单独逻辑*/
//				if(goods.getIsGroupon()==1){
//					int isGroup = grouponActivityMapper.isGroupGoods(dealerId,goods.getSellerGoodsId().toString());
//					Map  groupInfo = grouponActivityMapper.getGroupInfoByApp(dealerId,goods.getSellerGoodsId().toString());
//					goods.toGroupInfoByApp(groupInfo);
//					if(isGroup==0){
//						goods.setIsSupportGroupon(0);
//					}else{
//						goods.setIsSupportGroupon(1);
//					}
//				}				
//			}	
//			/** APP 单独逻辑*/
//		}else{
//			throw new Exception("商品不存在");
//		}
//		return goods;
//	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object  getGoodsDetailByMall(String sellerGoodsId) throws Exception {
		GoodsDetail goods = goodsDao.getGoodsDetail(sellerGoodsId,2);//商城增加返回字段店铺描述(note) modify 20161013
		StringBuffer sb = new StringBuffer();
		if(goods != null){
			toinitGoodsDetail(goods);
			/** mall 单独逻辑*/
//			goods.getAdverList().addAll(adverImageMapper.findMallAdverImages(4));
//			goods.getHotGoods().addAll(getRecommendGoods(goods));
			goods.setProxyCertificate(goodsDao.getProxyCertificateByMall(sellerGoodsId));//goods.getSellerId().toString()
			//商城商品详情 增加返回字段 活动描述List add 20161013
			List<Map<String,Object>> list = goodsDao.getActivityDescList(sellerGoodsId);
			for(Map map :list){
				sb.append(map.get("activityDesc")+";");
			}
			if(sb.length()>1){
				String str = sb.substring(0,sb.length()-1);
				goods.setActivityDesc(str);
				List activityList = new ArrayList<>();
				Map<String, Object> map = new HashMap<>();
				map.put("activityType",null);
				map.put("desc", str);
				activityList.add(map);
//				goods.setActivityList(activityList);
			}else{
				goods.setActivityDesc("");
			}//商品详情 最后一个活动描述不加“;”
			
			/** mall 单独逻辑*/
		}else{
			throw new Exception("商品不存在");
		}
		return goods;
	}
	
	/**
	 * 获取热门商品
	 * @param goods
	 * @return
	 */
	private List getRecommendGoods(GoodsDetail goods) {
		return goodsDao.getRecommendGoods(goods.getSaleGId());
	}

	/**
	 * 商品详情通用的查询数据
	 * @param goods
	 */
	private void toinitGoodsDetail(GoodsDetail goods) {
		String[] activityList = {  //0特卖,1现金券2满减3返现4满赠5团购6工币
				String.valueOf(goods.getIsxsms()),
				String.valueOf(goods.getIsRate()),
//				String.valueOf(goods.getIsFullcut()),
//				String.valueOf(goods.getIsRebates()),
//				String.valueOf(goods.getIsFullgive()),
				String.valueOf(goods.getIsJhs()),
//				String.valueOf(goods.getIsBean())
			};
		
//		goods.setActivityList(getGoodsActivity(goods.getSaleGId().toString(),activityList,goods));
		goods.setGrouponDesc(grouponActivityMapper.findGrouponDesc(goods.getSaleGId().toString()));
		goods.setAttrs(goodsDao.getGoodsAttr(goods.getgId()));
		if(StringUtils.isNotBlank(goods.getDetail())){
			String detail = "";
			if(goods.getSellerId()!=null && goods.getSellerId() ==9407609){
				detail = getUrlImg(goods.getDetail(), goods.getDetail(), "http:");
			}else{
				detail = getUrlImg(goods.getDetail(), goods.getDetail(), Constants.IMAGE_URL + "goodsImg/");
			}
			goods.setDetail(detail);
		}
	}

	/**
     * 根据goodsId (去除手工单)查询商品销售记录
     */
	@Override
	public Map<String, Object> selectGoodsSalesRecords(PageMode mode,int page, int rows) throws Exception {
		Pattern p = Pattern.compile("/(^([0\\*][0-9\\*]{2,3}\\s)?([2-9\\*][0-9\\*]{6,7})+(\\-[0-9\\*]{1,4})?$)|(^([0\\*][0-9\\*]{2,3})?([2-9\\*][0-9\\*]{6,7})+(\\-[0-9\\*]{1,4})?$)|(^([0\\*][0-9\\*]{2,3}[\\-\\*])?([2-9\\*][0-9\\*]{6,7})+(\\-[0-9\\*]{1,4})?$)|(^((\\(\\d{3}\\))|(\\d{3}\\-))?([1\\*][3578\\*][0-9\\*]{9})$)/");    		
		Map<String,Object> map = new HashMap<String,Object>();
		if(mode.contains("goodsId")){
			 //查询商品销售记录
			 List<Map<String,Object>> list = goodsDao.selectGoodsSalesRecords(mode.getStringValue("goodsId"),page,rows);
			 //查询商品销售记录总数
			 int count = goodsDao.getGoodsSalesRecordsCount(mode.getStringValue("goodsId"));
			 for(Map m :list){
				 if(m.get("buyerId")!=null ){
					 //查询不同购买人累计购买商品总数
					 int sumNum = goodsDao.getGoodsSumNumByBuyerId(mode.getStringValue("goodsId"),m.get("buyerId").toString());
					 m.put("sumNum",sumNum);
				 }
				 //对用户名加密
				 if(m.get("dealerName")!=null){
					 Matcher ma = p.matcher(m.get("dealerName").toString());
				        	if(ma.matches()==true){
				        		String dealerName = DESCoder.getNewTel(m.get("dealerName").toString());
				        		m.put("dealerName", dealerName);
				        	}else{
				        		StringBuffer ss = new StringBuffer(m.get("dealerName").toString()); 
								ss.replace(1, ss.length()-1, "***");
								m.put("dealerName", ss);
				    	    }	 
				 }
				 if(m.get("consignAddressInfo")!=null){
					 try {
						 //String s = m.get("consignAddressInfo").toString();
						 JSONObject json=JSONObject.parseObject(m.get("consignAddressInfo").toString());
						 String linkMan = json.get("consignee").toString();
						 m.put("linkMan", linkMan);
					} catch (Exception e) {
					}
						
				 }
				 //对联系人加密
				 if(m.get("linkMan")!=null){
					 StringBuffer s = new StringBuffer(m.get("linkMan").toString());
						switch (s.length()) {
						case 0:
							break;
						case 1:
							s.append("***");
							break;
						case 2:
							s.replace(1, 2, "***");
							break;
						case 3:
							s.replace(1, 2, "***");
							break;
						default:
							s.replace(1, s.length()-1, "***");
							break;
						} 
						m.put("linkMan",s);
				 }
				 
			 }
			 map.put("list", list);
			 map.put("count", count);
		}
		
		return map;


	}
	/**
     * 商城 热销排行
     */
	@Override
//	@Cacheable(value="goods",key="'hotGoods-'+#alias")
	public List selectHotGoods(String alias) throws Exception {
		//先查有销量的
		List list= goodsDao.selectHotGoods(alias);
		
		/*if(list==null || list.size()<8){
			//关联店铺查没有销量的推荐商品
			List list1 = goodsDao.selectHotGoods1(8-list.size());
			list.addAll(list1);
		}*/
		 return list;
	}

	@Override
	public Map<String, Object> searchFaceGoods(SearchGoods search) throws Exception {
		return solrService.searchFaceGoods(search);
	}

	@Override
	public Map<String, Object> searchPCFaceGoods(SearchGoods search) throws Exception {
		Map faceGoodsMap = solrService.searchFaceGoods(search);
		Map<String,Object> map = new HashMap<String,Object>();
		List list = new ArrayList();		
		if(faceGoodsMap != null && !faceGoodsMap.isEmpty()){
			List<GoodsInfo> goodsList = (List<GoodsInfo>)faceGoodsMap.get("goodsList");
			    DESCoder.instance();
				for(int i=0;i<goodsList.size();i++){
					Map m=new HashMap();
					//对商品型号和订货号进行加密
					String title = goodsList.get(i).getGoodsName();
					String model = goodsList.get(i).getModel();
					String buyNo = goodsList.get(i).getOrderNum();
					if(StringUtils.isNoneBlank(title)){
						m.put("title",title);//商品名称
					} else{
						m.put("title","");
					}
					if(StringUtils.isNoneBlank(model)){
						m.put("model",model);//商品型号
					} else{
						m.put("model","");
					}
					if(StringUtils.isNoneBlank(buyNo)){
						m.put("orderNum",buyNo);//订货号
					} else{
						m.put("orderNum","");
					}
					m.put("image",goodsList.get(i).getImage());//商品图片
					m.put("price",goodsList.get(i).getPrice());//供应商定义的商品价格 
					m.put("marketPrice",goodsList.get(i).getMarketPrice());
					m.put("salePrice",goodsList.get(i).getSalePrice());//modify 20160615
					m.put("saleId",goodsList.get(i).getId());//供应商商品ID
                    /**
                     * create by yr on 2018-11-16 增加goodsId
                     */
                    m.put("goodsId",goodsList.get(i).getGoodsId());//goodsId售卖商品的id
					m.put("batchQuantity",goodsList.get(i).getBatchQuantity());//批量
					m.put("orderQuantity",goodsList.get(i).getOrderQuantity());//起定量            返回增加起定量和批量 20160614
					list.add(i, m);
					
				}
				map.put("searchList", list);
				map.put("total", faceGoodsMap.get("total"));
				map.put("attrs", faceGoodsMap.get("attrArray"));//返回增加属性 20160614
		}
		return map;
	}

	@Override
	public Map<String, Object> getGoodsSeries(String id) throws Exception {
		String  spuId= goodsDao.findSpuIdById(id);
		if(spuId==null){
			return null;
		}else{
			return solrService.getGoodsSeries(spuId);
		}
	}

	@Override
	public List getBrandCertificate(String brandId) {
		// TODO Auto-generated method stub
		return goodsDao.getBrandCertificate(brandId);
	}

	@Override
	public void updateSolrData(String ids) throws Exception {
		HttpSolrServer solrServer = new HttpSolrServer("http://192.168.1.193:8080/solr/core2");
		List<MallGoodsInfo> goods = goodsDao.getGoodsView(ids);
		if(ids.split(",").length>50){
			throw new RuntimeException("数据太多请控制在50个或以内");
		}
		for (MallGoodsInfo mallGoodsInfo : goods) {
			List<Integer> attr= goodsDao.getGoodsViewAttr(mallGoodsInfo.getGoodsId());
			List<Integer> attrValue= goodsDao.getGoodsViewAttrValue(mallGoodsInfo.getGoodsId());
			if(attr!=null){
				mallGoodsInfo.setAttrId(attr);
			}
			if(attrValue!=null){
				mallGoodsInfo.setAttrValueId(attrValue);
			}
		}
		solrServer.addBeans(goods);
		solrServer.commit();
		
	}

	@Override
	public Map<String, Object> searchModelSelectionGoods(SearchGoods search) {
		Map<String, Object> saasGoodsMap = solrService.searchModelSelectionGoods(search);
		return saasGoodsMap;
	}

    @Override
    public List<Map<String, Object>> getSellerGoods(Integer goodsId) throws Exception {

       return goodsDao.getSellerGoods(goodsId);
    }
}
