package bmatser.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.GroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import bmatser.dao.CategoryMapper;
import bmatser.exception.ApplicationException;
import bmatser.exception.GdbmroException;
import bmatser.exception.NotFoundException;
import bmatser.model.Gift;
import bmatser.model.SellerGoods;
import bmatser.pageModel.GoodsDetail;
import bmatser.pageModel.GoodsInfo;
import bmatser.pageModel.KeywordLogPage;
import bmatser.pageModel.MallGoodsInfo;
import bmatser.pageModel.MallKeywordLogPage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.SearchGoods;
import bmatser.service.BrandI;
import bmatser.service.GoodsI;
import bmatser.service.SysMessageI;
import bmatser.service.UserBlackListServiceI;
import bmatser.util.DateTypeHelper;
import bmatser.util.IpUtil;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
import bmatser.util.ResponseMsg;
import bmatser.util.SolrUtil;
import org.springframework.http.HttpStatus;

@Controller
@RequestMapping("goods")
@Api(value = "goods", description = "商品")
public class GoodsController {

    @Autowired
    private GoodsI goodsService;
    @Autowired
    private BrandI brandService;
    @Autowired
    private SysMessageI messageService;

    @Autowired
    private MemcachedClient memcachedClient;
    //	@Autowired
//	private MongoServiceI mongoService;
    @Autowired
    private UserBlackListServiceI UserBlackListI;
    @Autowired
    private CategoryMapper categoryDao;


    @RequestMapping(value = "/hot/{page}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "热门商品", response = SellerGoods.class)
    public ResponseMsg findHotGoods(@ApiParam(value = "页数") @PathVariable int page) {
        int rows = 20;
        String date = DateTypeHelper.getDateString(DateTypeHelper.dateAdd(new Date(), -3, "M"), DateTypeHelper.DEFAULT_DATE_PATTERN);
        ResponseMsg msg = new ResponseMsg();
        try {
            page = page > 10 ? 9 : page;
            msg.setData(goodsService.findHotGoods(date, page * rows, rows));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/selllist/{page}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询直发区商品", response = SellerGoods.class)
    public ResponseMsg findSellGoodsByKeyword(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                              @ApiParam(value = "页数") @PathVariable int page,
                                              @ApiParam(value = "分类ID") @RequestParam(required = false) Integer cateId,
                                              @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                              @ApiParam(value = "单价排序字段") @RequestParam(required = false) String priceOrder,
                                              @ApiParam(value = "库存排序字段") @RequestParam(required = false) String stockOrder) {
        int rows = 20;
        ResponseMsg msg = new ResponseMsg();
        try {
            page = page > 10 ? 9 : page;
            msg.setData(goodsService.findSellGoodsByKeyword(keyword, cateId, brandId, priceOrder, stockOrder, page * rows, rows));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/baselist/{page}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询面价查询区商品", response = SellerGoods.class)
    public ResponseMsg findBaseGoodsByKeyword(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                              @ApiParam(value = "页数") @PathVariable int page,
                                              @ApiParam(value = "分类ID") @RequestParam(required = false) Integer cateId,
                                              @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId) {
        int rows = 20;
        ResponseMsg msg = new ResponseMsg();
        try {
            page = page > 10 ? 9 : page;
            msg.setData(goodsService.findBaseGoodsByKeyword(keyword, cateId, brandId, page * rows, rows));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/sellcate/{page}")
    @ResponseBody
    @ApiOperation(value = "获取直发区商品分类")
    public ResponseMsg findSellGoodsCategory(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                             @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                             @ApiParam(value = "页数") @PathVariable int page) {
        int rows = 20;
        ResponseMsg msg = new ResponseMsg();
        try {
            page = page > 10 ? 9 : page;
            msg.setData(goodsService.findSellGoodsCategory(keyword, brandId, page * rows, rows));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/goodscate/{page}")
    @ResponseBody
    @ApiOperation(value = "获取商品分类")
    public ResponseMsg findGoodsCategory(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                         @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                         @ApiParam(value = "页数") @PathVariable int page) {
        int rows = 20;
        ResponseMsg msg = new ResponseMsg();
        try {
            page = page > 10 ? 9 : page;
            msg.setData(goodsService.findGoodsCategory(keyword, brandId, page * rows, rows));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/basecate/{page}")
    @ResponseBody
    @ApiOperation(value = "获取面价查询区商品分类")
    public ResponseMsg findBaseGoodsCategory(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                             @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                             @ApiParam(value = "页数") @PathVariable int page) {
        int rows = 20;
        ResponseMsg msg = new ResponseMsg();
        try {
            page = page > 10 ? 9 : page;
            msg.setData(goodsService.findBaseGoodsCategory(keyword, brandId, page * rows, rows));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/detail/{sellerGoodsId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取商品详情(APP)", response = GoodsDetail.class)
    public ResponseMsg getGoodsDetail(@ApiParam(value = "供应商商品ID") @RequestParam String sellerGoodsId, HttpServletRequest request, HttpServletResponse response) {
        ResponseMsg msg = new ResponseMsg();
        try {
            msg.setData(goodsService.getGoodsDetail(sellerGoodsId));
//			if(null != map && null != map.get("title")){
//				memcachedClient.set("goodTitle"+sellerGoodsId, 0,map.get("title"));//商品标题方法缓存
//			}
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/attrs/{goodsId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "获取商品属性")
    public ResponseMsg getGoodsAttr(@ApiParam(value = "商品ID") @RequestParam Long goodsId) {
        ResponseMsg msg = new ResponseMsg();
        try {
            msg.setData(goodsService.getGoodsAttr(goodsId));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    /**
     * 商城搜索
     *
     * @param keyword      关键词
     * @param dealerId     零售商ID
     * @param brandId      品牌ID
     * @param categoryId   分类ID
     * @param attrValueIds 属性ID
     * @param sortFields   排序参数
     * @param sortFlags    正序或倒序
     * @param page         页
     * @param rows         行
     * @return
     */
    @RequestMapping(value = "/get_list/mall/{page}/{rows}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "商城搜索")
    public Map<String, Object> searchMallGoods(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                               @ApiParam(value = "零售商ID") @RequestParam(required = false) Integer dealerId,
                                               @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                               @ApiParam(value = "分类ID") @RequestParam(required = false) Integer categoryId,
                                               @ApiParam(value = "属性ID") @RequestParam(required = false) String attrValueIds,
                                               @ApiParam(value = "排序参数") @RequestParam(required = false) String sortFields,
                                               @ApiParam(value = "正序或倒序") @RequestParam(required = false) String sortFlags,
                                               @ApiParam(value = "别名") @RequestParam(required = false) String alias,
                                               @ApiParam(value = "页数") @PathVariable int page,
                                               @ApiParam(value = "行数") @PathVariable int rows, HttpServletRequest request) {
        try {
            attrValueIds = StringUtils.isNotBlank(attrValueIds) ? URLDecoder.decode(attrValueIds, "UTF-8") : attrValueIds;
//			sortFields = StringUtils.isNotBlank(sortFields)?URLDecoder.decode(sortFields, "UTF-8"):"salesVolume";
//			sortFlags = StringUtils.isNotBlank(sortFlags)?URLDecoder.decode(sortFlags, "UTF-8"):"1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer[] attrValueIdInt = null;
        page = page > 10 ? 9 : page;
        if (StringUtils.isNoneBlank(attrValueIds)) {
            String[] attrValueIdStr = attrValueIds.split(",");
            int size = attrValueIdStr.length;
            attrValueIdInt = new Integer[size];
            for (int i = 0; i < size; i++) {
                attrValueIdInt[i] = Integer.parseInt(attrValueIdStr[i].trim());
            }
        }
        if (null != keyword) {
            try {
                keyword = URLDecoder.decode(keyword, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map map = goodsService.searchMallGoods(keyword, dealerId, brandId, categoryId, attrValueIdInt, sortFields, sortFlags, page, rows, alias);

        //保存mongoDB
        LoginInfo loginInfo = MemberTools.isLogin(request);
        MallKeywordLogPage mallKeywordLogPage = new MallKeywordLogPage();
        if (loginInfo != null && loginInfo.getLoginId() != null) {
            mallKeywordLogPage.setLoginId(loginInfo.getLoginId());
            mallKeywordLogPage.setLoginName(loginInfo.getUsername());
        }
        mallKeywordLogPage.setSearchTime(DateTypeHelper.getCurrentDateTimeString());
        if (null != keyword) {
            try {
                keyword = URLDecoder.decode(keyword, "UTF-8");
                mallKeywordLogPage.setKeyword(keyword);
                mallKeywordLogPage.setSearchType("1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (null != categoryId) {
                mallKeywordLogPage.setKeyword(categoryId.toString());
                mallKeywordLogPage.setSearchType("2");
            } else if (null != brandId) {
                mallKeywordLogPage.setKeyword(brandId.toString());
                mallKeywordLogPage.setSearchType("3");
            }
        }
        mallKeywordLogPage.setIp(IpUtil.getIpAddr(request));
        if (null != map) {
            String total = map.get("total") == null ? ")" : map.get("total").toString();
            mallKeywordLogPage.setResultCount(total);
        }
//		mongoService.save(mallKeywordLogPage);


        return map;
    }

    /**
     * 商城搜索
     *
     * @param keyword      关键词
     * @param dealerId     零售商ID
     * @param brandId      品牌ID
     * @param categoryId   分类ID
     * @param attrValueIds 属性ID
     * @param sortFields   排序参数
     * @param sortFlags    正序或倒序
     * @param page         页
     * @param rows         行
     * @return
     */
    @RequestMapping(value = "/get_list/appMall/{page}/{rows}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "商城搜索")
    public ResponseMsg searchAppMallGoods(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                          @ApiParam(value = "零售商ID") @RequestParam(required = false) Integer dealerId,
                                          @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                          @ApiParam(value = "分类ID") @RequestParam(required = false) Integer categoryId,
                                          @ApiParam(value = "属性ID") @RequestParam(required = false) String attrValueIds,
                                          @ApiParam(value = "排序参数") @RequestParam(required = false) String sortFields,
                                          @ApiParam(value = "正序或倒序") @RequestParam(required = false) String sortFlags,
                                          @ApiParam(value = "别名") @RequestParam(required = false) String alias,
                                          @ApiParam(value = "页数") @PathVariable int page,
                                          @ApiParam(value = "行数") @PathVariable int rows, HttpServletRequest request) {
        ResponseMsg msg = new ResponseMsg();
        try {
            attrValueIds = StringUtils.isNotBlank(attrValueIds) ? URLDecoder.decode(attrValueIds, "UTF-8") : attrValueIds;
//			sortFields = StringUtils.isNotBlank(sortFields)?URLDecoder.decode(sortFields, "UTF-8"):"salesVolume";
//			sortFlags = StringUtils.isNotBlank(sortFlags)?URLDecoder.decode(sortFlags, "UTF-8"):"1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        Integer[] attrValueIdInt = null;
        page = page > 10 ? 9 : page;
        if (StringUtils.isNoneBlank(attrValueIds)) {
            String[] attrValueIdStr = attrValueIds.split(",");
            int size = attrValueIdStr.length;
            attrValueIdInt = new Integer[size];
            for (int i = 0; i < size; i++) {
                attrValueIdInt[i] = Integer.parseInt(attrValueIdStr[i].trim());
            }
        }
        if (null != keyword) {
            try {
                keyword = URLDecoder.decode(keyword, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map map = null;
        try {
            map = goodsService.searchAppMallGoods(keyword, dealerId, brandId, categoryId, attrValueIdInt, sortFields, sortFlags, page, rows, alias);
            msg.setData(map);
        } catch (Exception e) {
            msg.setError(e);
        }

        //保存mongoDB
        LoginInfo loginInfo = MemberTools.isLogin(request);
        MallKeywordLogPage mallKeywordLogPage = new MallKeywordLogPage();
        if (loginInfo != null && loginInfo.getLoginId() != null) {
            mallKeywordLogPage.setLoginId(loginInfo.getLoginId());
            mallKeywordLogPage.setLoginName(loginInfo.getUsername());
        }
        mallKeywordLogPage.setSearchTime(DateTypeHelper.getCurrentDateTimeString());
        if (null != keyword) {
            try {
                keyword = URLDecoder.decode(keyword, "UTF-8");
                mallKeywordLogPage.setKeyword(keyword);
                mallKeywordLogPage.setSearchType("1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (null != categoryId) {
                mallKeywordLogPage.setKeyword(categoryId.toString());
                mallKeywordLogPage.setSearchType("2");
            } else if (null != brandId) {
                mallKeywordLogPage.setKeyword(brandId.toString());
                mallKeywordLogPage.setSearchType("3");
            }
        }
        mallKeywordLogPage.setIp(IpUtil.getIpAddr(request));
        if (null != map) {
            String total = map.get("total") == null ? ")" : map.get("total").toString();
            mallKeywordLogPage.setResultCount(total);
        }
//		mongoService.save(mallKeywordLogPage);


        return msg;
    }

    @RequestMapping(value = "/hotSearch", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "热门搜索")
    public ResponseMsg getHot(HttpServletRequest request) {
        ResponseMsg msg = new ResponseMsg();
        String date = DateTypeHelper.getDateString(DateTypeHelper.dateAdd(new Date(), -3, "M"), DateTypeHelper.DEFAULT_DATE_PATTERN);
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("hot", goodsService.findHotGoods(date, 0 * 20, 20));
            map.put("hotBrand", brandService.findBrandsByMark(1));
            map.put("message", messageService.findDataMessages());
            map.put("keyword", messageService.findHotKeyWord());
            msg.setData(map);
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;

    }

    /**
     * Saas商品查询
     *
     * @param search
     * @param page
     * @param rows
     * @param type
     * @param request
     * @return
     */
    @RequestMapping(value = "/get_list/{type}/{page}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Saas商品查询", response = GoodsDetail.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMsg searchSaasGoods(
            @ModelAttribute SearchGoods search,
            @ApiParam(value = "页数") @PathVariable int page,
            @ApiParam(value = "pc,goods_list,face,pcface") @PathVariable("type") String type, HttpServletRequest request) throws Exception {
        ResponseMsg msg = new ResponseMsg();
        PageMode pageModel = new PageMode(request);
        search.setPathVariable(type, page, 10).decode();
        try {
            UserBlackListI.checkUserAccess(pageModel);
        } catch (GdbmroException e) {
            msg.setMsg(e.getMessage());
            msg.setCode(e.getCode());
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UserBlackListI.saveSearchCount(pageModel);
        }

        //FIXME 分类暂时先换成分类名称查询,后期前端传categoryId即可
        String cateName = null;
        if (StringUtils.isNotBlank(search.getCategoryName()) && search.getCategoryId() == null) {
            try {
                cateName = URLDecoder.decode(search.getCategoryName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Integer cateId = categoryDao.findCategoryNoByCateName(cateName);
            search.setCategoryId(cateId);
        }


        LoginInfo loginInfo = MemberTools.isSaasLogin(request);
        if (StringUtils.isBlank(search.getChannel())) {
            search.setChannel("pc");
        }
        //商品搜索 微信saas取dealerId
        if ("pc".equals(search.getChannel()) || "saas".equals(search.getChannel())) {
            if (loginInfo != null) {
                Integer dealerId = Integer.parseInt(loginInfo.getDealerId());
                search.setDealerId(dealerId);
            }
        }
        try {
            switch (type) {
                //网站商品列表
                case "goods_list":
                    msg.setData(goodsService.searchSaasGoods(search));
                    break;

                //面价查询数据接口返回的数据中将没用的数据去掉
                case "brand_selection.action":
                    msg.setData(goodsService.searchPCFaceGoods(search));
                    break;
                case "model_selection.action":
                    msg.setData(goodsService.searchModelSelectionGoods(search));
                    break;
            }
        } catch (Exception e) {
//			throw new NotFoundException();
            msg.setError(e);
        } finally {
            try {
                saveKeywordLog(pageModel.put("type", type), msg, loginInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }


    @RequestMapping(value = "/get_list/{type}/{page}/forIos", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiOperation(value = "Saas商品查询", response = GoodsDetail.class, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMsg searchSaasGoodsForIos(
            @ModelAttribute SearchGoods search,
            @ApiParam(value = "页数") @PathVariable int page,
            @ApiParam(value = "pc,goods_list,face,pcface") @PathVariable("type") String type, HttpServletRequest request) throws Exception {
        ResponseMsg msg = new ResponseMsg();
        PageMode pageModel = new PageMode(request);
        search.setPathVariable(type, page, 10).decode();
        try {
            UserBlackListI.checkUserAccess(pageModel);
        } catch (GdbmroException e) {
            msg.setMsg(e.getMessage());
            msg.setCode(e.getCode());
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            UserBlackListI.saveSearchCount(pageModel);
        }

        //FIXME 分类暂时先换成分类名称查询,后期前端传categoryId即可
        String cateName = null;
        if (StringUtils.isNotBlank(search.getCategoryName()) && search.getCategoryId() == null) {
            try {
                cateName = URLDecoder.decode(search.getCategoryName(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Integer cateId = categoryDao.findCategoryNoByCateName(cateName);
            search.setCategoryId(cateId);
        }


        LoginInfo loginInfo = MemberTools.isSaasLogin(request);
        if (StringUtils.isBlank(search.getChannel())) {
            search.setChannel("pc");
        }
        //商品搜索 微信saas取dealerId
        if ("pc".equals(search.getChannel()) || "saas".equals(search.getChannel())) {
            if (loginInfo != null) {
                Integer dealerId = Integer.parseInt(loginInfo.getDealerId());
                search.setDealerId(dealerId);
            }
        }
        try {
            switch (type) {
                //网站商品列表
                case "goods_list":
                    Map<String, Object> listMap = goodsService.searchSaasGoods(search);
//                    Map<String,Object> map = goodsService.searchSaasGoods(search);
                    List<Map.Entry> entityList = Lists.newArrayList();
                    if (listMap.get("attrs") != null) {
                        entityList = (List<Map.Entry>) listMap.get("attrs");
                        if (entityList.size() > 0 && entityList != null) {
                            for (Map.Entry e : entityList) {
                                if (e.getKey().equals("系列")) {
                                    msg.setData(e);
                                }
                            }
                        } else {
                            msg.setData("暂无分类");
                        }
                    } else {
                        msg.setData("暂无属性");
                    }
                    break;

                //面价查询数据接口返回的数据中将没用的数据去掉
                case "brand_selection.action":
                    msg.setData(goodsService.searchPCFaceGoods(search));
                    break;
                case "model_selection.action":
                    Map<String, Object> map = goodsService.searchModelSelectionGoods(search);
                    List<Map.Entry> list = Lists.newArrayList();
                    if (map.get("params") != null) {
                        list = (List<Map.Entry>) map.get("params");
                        for (Map.Entry e : list) {
                            if (e.getKey().equals("系列")) {
                                msg.setData(e);
                            }
                        }
                    }
                    break;
            }
        } catch (Exception e) {
//			throw new NotFoundException();
            msg.setError(e);
        } finally {
            try {
                saveKeywordLog(pageModel.put("type", type), msg, loginInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }


    /**
     * 快速下单查询
     *
     * @param search
     * @param request
     * @return
     */
    @RequestMapping(value = "/goods_search/quick", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg quickSearchSaasGoods(
            @RequestBody SearchGoods search, HttpServletRequest request) {
        List<Object> resultList = Lists.newArrayList();
        String[] keys = search.getKeys();
        Integer nums[] = search.getNums();
        search.setChannel("pc");
        search.setRows(10);
        search.setPage(1);
        ResponseMsg msg = new ResponseMsg();
        LoginInfo loginInfo = MemberTools.isSaasLogin(request);
        //商品搜索 微信saas取dealerId
        if (loginInfo != null) {
            Integer dealerId = Integer.parseInt(loginInfo.getDealerId());
            search.setDealerId(dealerId);
        }
        for (int i = 0; i < keys.length; i++) {
            try {
                search.setKeyword(keys[i]);
                Map<String, Object> map = goodsService.searchSaasGoods(search);
                if (map != null) {
                    map.put("key_num", nums[i]);
                }
                resultList.add(map);
            } catch (Exception e) {
                msg.setError(e);
            } finally {
            }
        }
        msg.setData(resultList);
        return msg;


    }


    /**
     * 保存搜素记录
     *
     * @param pageModel
     * @param msg
     * @param loginInfo
     */
    private void saveKeywordLog(PageMode pageModel, ResponseMsg msg, LoginInfo loginInfo) {
        //保存mongoDB
        if (loginInfo != null && loginInfo.getLoginId() != null) {
            KeywordLogPage keywordLogPage = new KeywordLogPage();
            keywordLogPage.setLoginId(loginInfo.getLoginId());
            keywordLogPage.setLoginName(loginInfo.getUsername());
            keywordLogPage.setSearchTime(DateTypeHelper.getCurrentDateTimeString());
            if (pageModel.contains("keyword")) {
                try {
                    String keyword = pageModel.decode("keyword");
                    keywordLogPage.setKeyword(keyword);
                    keywordLogPage.setSearchType("1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (pageModel.contains("categoryId")) {
                    keywordLogPage.setKeyword(pageModel.getStringValue("categoryId"));
                    keywordLogPage.setSearchType("2");
                } else if (pageModel.contains("brandId")) {
                    keywordLogPage.setKeyword(pageModel.getStringValue("brandId"));
                    keywordLogPage.setSearchType("3");
                }
            }
            keywordLogPage.setIp(pageModel.getIp());
            keywordLogPage.setType(pageModel.getStringValue("type"));//modify 20160615 区分查询类型 保存日志到mongoDB
            if (null != msg.getData()) {
                Map<String, Object> data = (Map<String, Object>) msg.getData();
                String total = data.get("total") == null ? ")" : data.get("total").toString();
                keywordLogPage.setResultCount(total);
            }
//			mongoService.save(keywordLogPage);
        }
    }

    @RequestMapping(value = "/get_list/mob/{page}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "手机商品查询", response = GoodsDetail.class)
    public ResponseMsg searchMobGoods(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                      @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                      @ApiParam(value = "分类ID") @RequestParam(required = false) Integer categoryId,
                                      @ApiParam(value = "排序参数") @RequestParam(required = false) String sortFields,
                                      @ApiParam(value = "正序或倒序") @RequestParam(required = false) String sortFlags,
                                      @ApiParam(value = "页数") @PathVariable int page, HttpServletRequest request) {
        ResponseMsg msg = new ResponseMsg();
        request.getCookies();
        try {
//			page=page > 10?9 : page;
            page = page > 5 ? 5 : page;
            if (null != keyword) {
                keyword = URLDecoder.decode(keyword, "UTF-8");
            }
            msg.setData(goodsService.searchMobGoods(keyword, brandId, categoryId, sortFields, sortFlags, page * 10, 10));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/searchPrice/mob/{page}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "面价 手机", response = GoodsDetail.class)
    public ResponseMsg searchMobGoodsPrice(@ApiParam(value = "关键词") @RequestParam(required = false) String keyword,
                                           @ApiParam(value = "品牌ID") @RequestParam(required = false) Integer brandId,
                                           @ApiParam(value = "分类ID") @RequestParam(required = false) Integer categoryId,
                                           @ApiParam(value = "排序参数") @RequestParam(required = false) String sortFields,
                                           @ApiParam(value = "正序或倒序") @RequestParam(required = false) String sortFlags,
                                           @ApiParam(value = "页数") @PathVariable int page) {
        ResponseMsg msg = new ResponseMsg();
        try {
//			page=page > 10?9 : page;
            page = page > 5 ? 5 : page;
            if (null != keyword) {
                keyword = URLDecoder.decode(keyword, "UTF-8");
            }
            msg.setData(goodsService.searchMobGoodsPrice(keyword, brandId, categoryId, sortFields, sortFlags, page * 10, 10));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    /**
     * Saas商品查询(通用)
     * @param keyword
     * @param dealerId
     * @param brandId
     * @param categoryId
     * @param attrValueIds
     * @param sortFields
     * @param sortFlags
     * @param page
     * @param rows
     * @param advantage
     * @return
     */
    /*private ResponseMsg getGoodsListBySearch(String keyword, Integer dealerId,
            Integer brandId, Integer categoryId, String attrValueIds,
			String sortFields, String sortFlags, int page, int rows,
			String type,String channel, String advantage) {
		try {
			attrValueIds = StringUtils.isNotBlank(attrValueIds)?URLDecoder.decode(attrValueIds, "UTF-8"): attrValueIds;
			switch (type) {
			case "saas":
				sortFields = StringUtils.isNotBlank(sortFields)?URLDecoder.decode(sortFields, "UTF-8"):"salesVolume";
				sortFlags = StringUtils.isNotBlank(sortFlags)?URLDecoder.decode(sortFlags, "UTF-8"):"1";
				break;
			case "face":
				sortFields = StringUtils.isNotBlank(sortFields)?URLDecoder.decode(sortFields, "UTF-8"):"image";
				sortFlags = StringUtils.isNotBlank(sortFlags)?URLDecoder.decode(sortFlags, "UTF-8"):"1";
				break;
			case "pcface":
				sortFields = StringUtils.isNotBlank(sortFields)?URLDecoder.decode(sortFields, "UTF-8"):"salesVolume,image";
				sortFlags = StringUtils.isNotBlank(sortFlags)?URLDecoder.decode(sortFlags, "UTF-8"):"1,1";
				break;	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Integer[] attrValueIdInt = null;
		ResponseMsg msg = new ResponseMsg();
//		page=page > 10?9 : page;
		page=page > 5?5 : page;
		if(StringUtils.isNoneBlank(attrValueIds)){
			String[] attrValueIdStr = attrValueIds.split(",");
			attrValueIdInt = new Integer[attrValueIdStr.length];
			for(int i = 0,size=attrValueIdStr.length; i < size; i++){
				attrValueIdInt[i] = Integer.parseInt(attrValueIdStr[i].trim());
			}
		}
		if(null != keyword){
			try{
				keyword = URLDecoder.decode(keyword, "UTF-8");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			switch (type) {
			case "saas":
				msg.setData(goodsService.searchSaasGoods(keyword, dealerId, brandId, categoryId, attrValueIdInt, sortFields, sortFlags, page, rows,channel,advantage));
				break;
			case "face":
				msg.setData(goodsService.searchFaceGoods(keyword, dealerId, brandId, categoryId, attrValueIdInt, sortFields, sortFlags, page, rows,channel));
				break;
			//面价查询数据接口返回的数据中将没用的数据去掉 20160603
			case "pcface":
				msg.setData(goodsService.searchPCFaceGoods(keyword, dealerId, brandId, categoryId, attrValueIdInt, sortFields, sortFlags, page, rows));
				break;	
			}
			
			msg.setCode(0);
		} catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}*/

    /**
     * 输入框联想提示
     *
     * @author felx
     */
    @RequestMapping(value = "/down_list_option", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "输入框联想提示")
    public ResponseMsg suggestGoods(@ApiParam(value = "关键词") @RequestParam String keyword) {
        ResponseMsg msg = new ResponseMsg();
        try {
            if (null != keyword) {
                keyword = URLDecoder.decode(keyword, "UTF-8");
            }
            msg.setData(goodsService.suggestGoods(keyword));
            msg.setCode(0);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }


    /**
     * 商品详情
     *
     * @param sellerGoodsId
     * @param request
     * @return
     */
    @RequestMapping(value = {
            "/web_item/info/{sellerGoodsId}", //PC端
            "/app/info/{sellerGoodsId}",//手机端
            "/mall/info/{sellerGoodsId}"//商城
            //,"/test/detail/{sellerGoodsId}"//商城
    }, method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "商品详情", response = GoodsDetail.class)
    public ResponseMsg getGoodsDetail(@ApiParam(value = "sellerGoodsId") @PathVariable String sellerGoodsId, HttpServletRequest request) {
        ResponseMsg msg = new ResponseMsg();
        PageMode model = new PageMode(request);
        try {
            Object o = null;
            switch (model.channel()) {
                case "web_item":
                    o = goodsService.getGoodsDetailBySaas(sellerGoodsId, model.getLogin().getDealerId());
                    break;
                case "app":
//				o = goodsService.getGoodsDetailByApp(sellerGoodsId,model.getLogin().getDealerId(),model);
                    break;
                case "mall":
                    o = goodsService.getGoodsDetailByMall(sellerGoodsId);
                    break;
                default:
                    //goodsService.getGoodsDetailByMall(sellerGoodsId)
                    return msg.setError("数据不存在");
            }
            msg.setData(o);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    /**
     * 根据goodsId (去除手工单)查询商品销售记录
     */
    @RequestMapping(value = "/{channel}/selectGoodsSalesRecords/{page}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "根据goodsId (去除手工单)查询商品销售记录")
    public ResponseMsg selectGoodsSalesRecords(HttpServletRequest request, @ApiParam(value = "页数") @PathVariable int page) {
        ResponseMsg msg = new ResponseMsg();
        PageMode mode = new PageMode(request);
        int rows = 20;
		/*if(!mode.isLogin(mode.channel())){
			return msg.setError("请登录!");
		}*/
        try {
            page = page > 10 ? 9 : page;
            msg.setData(goodsService.selectGoodsSalesRecords(mode, (page - 1) * rows, rows));
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    /**
     * 商城 热销排行
     */
    @RequestMapping(value = "/{channel}/selectHotGoods", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "商城 热销排行")
    public ResponseMsg selectHotGoods(HttpServletRequest request) {
        ResponseMsg msg = new ResponseMsg();
        PageMode mode = new PageMode(request);
        if (!mode.contains("alias")) {
            return msg.setError("参数错误");
        }
        try {
            msg.setData(goodsService.selectHotGoods(mode.getStringValue("alias")));
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/goodsSeries", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询商品SPU ID")
    public ResponseMsg getGoodsSeries(HttpServletRequest request, @ApiParam(value = "sellerGoodsId") @RequestParam String id) {
        ResponseMsg msg = new ResponseMsg();
        try {
            msg.setData(goodsService.getGoodsSeries(id));
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;

    }

    @RequestMapping(value = "/brandCertificate", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "查询品牌授权证书")
    public ResponseMsg getBrandCertificate(HttpServletRequest request, @ApiParam(value = "品牌Id") @RequestParam String brandId) {
        ResponseMsg msg = new ResponseMsg();
        try {
            msg.setData(goodsService.getBrandCertificate(brandId));
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    @RequestMapping(value = "/solr", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新solr索引")
    public ResponseMsg updateSolrData(HttpServletRequest request, @ApiParam(value = "商品Id") @RequestParam String ids) {
        ResponseMsg msg = new ResponseMsg();
        try {
            goodsService.updateSolrData(ids);
        } catch (Exception e) {
            msg.setError(e);
        }
        return msg;
    }

    public static void main(String[] args) throws SolrServerException, IOException {
        SolrUtil solr = new SolrUtil("http://192.168.1.193:8080/solr/core2");
        HttpSolrServer solrServer = new HttpSolrServer("http://192.168.1.193:8080/solr/core2");
        MallGoodsInfo goods = new MallGoodsInfo();
        goods.setId("46870");
        goods.setBatchQuantity(5);
        solrServer.addBean(goods);
        solrServer.commit();
		
/*		solr.setRows(10);
		solr.setParam(GroupParams.GROUP, "true");
		solr.setParam(GroupParams.GROUP_FIELD, "id");
		solr.setParam(GroupParams.GROUP_LIMIT, "0");
		QueryResponse response = solr.getResponse();
		GroupResponse groupResponse = response.getGroupResponse();
		SolrDocumentList solrList = response.getResults();
		Long total = solrList.getNumFound();
		System.out.println(total);
		if(groupResponse!=null){
			 List<GroupCommand> groupList = groupResponse.getValues();  
			    for(GroupCommand groupCommand : groupList) {  
			        List<Group> groups = groupCommand.getValues();  
			        for(Group group : groups) {  
			        	System.out.println(group.getGroupValue()+","+(int)group.getResult().getNumFound()+","+group.getResult());
			           // info.put(group.getGroupValue(), (int)group.getResult().getNumFound());  
			        }  
			    }  
		}*/
    }

    @RequestMapping(value = "/getSellerGoodsById", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMsg getSellerGoodsById(@RequestParam("gId") Integer gId) {
        ResponseMsg msg = new ResponseMsg();
        try {
            List<Map<String, Object>> list = goodsService.getSellerGoods(gId);
            if (list.size() < 1) {
                msg.setData("该商品不在售卖中,请联系客服！");
            } else {
                msg.setData(list);
            }
            msg.setCode(0);
        } catch (Exception e) {
            msg.setCode(-1);
            msg.setData(e.getMessage());
        }
        return msg;
    }
}
