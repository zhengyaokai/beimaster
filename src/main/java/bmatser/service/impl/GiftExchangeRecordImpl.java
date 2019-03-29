package bmatser.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.BuyerConsignAddressMapper;
import bmatser.dao.CrmCustomerCategoryMapper;
import bmatser.dao.DealerBusinessLicenseMapper;
import bmatser.dao.DealerMapper;
import bmatser.dao.DealerScoreMapper;
import bmatser.dao.GiftExchangeRecordMapper;
import bmatser.dao.GiftMapper;
import bmatser.dao.LogisticsCompanyMapper;
import bmatser.dao.RegionMapper;
import bmatser.dao.RegisterMapper;
import bmatser.dao.ShoppingCartMapper;
import bmatser.model.BuyerConsignAddress;
import bmatser.model.CrmCustomerCategory;
import bmatser.model.Dealer;
import bmatser.model.GiftExchangeRecord;
import bmatser.model.LogisticsCompany;
import bmatser.model.Register;
import bmatser.service.DealerI;
import bmatser.service.GiftExchangeRecordI;
import bmatser.util.DESCoder;
import bmatser.util.Delivery;
import bmatser.util.LoginInfo;
import bmatser.util.PaymentIDProduce;

/**
 * 礼品兑换记录
 * @author felx
 * @date 2016.03.09 
 */
@Service("giftExchangeRecord")
public class GiftExchangeRecordImpl implements GiftExchangeRecordI {
	@Autowired
	private GiftExchangeRecordMapper giftExchangeRecordDao;
	@Autowired
	private DealerMapper dealerDao;
	@Autowired
	private RegisterMapper  RegisterDao;
	@Autowired
	private BuyerConsignAddressMapper buyerConsignAddressDao;
	@Autowired
	private RegionMapper RegionDao;
	@Autowired
	private GiftMapper giftMapperDao;
	@Autowired
	private DealerScoreMapper dealerScoreDao;
	@Autowired
	private LogisticsCompanyMapper LogisticsCompanyI;
	private static Map saveTree=new LinkedHashMap(){
		{put("id","0");put("buyer_id","1");put("country_id","2");put("province_id","3");put("city_id","4");put("area_id","5");
		put("address","6");put("post_code","7");put("consignee","8");put("telephone","9");put("mobile","10");put("email","11");
		put("is_default","12");put("create_time","13");put("status","14");put("province_id_name","15");put("city_id_name","16");
		put("area_id_name","17");}};
	
	/**
	 * 查询礼品兑换数据
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List selectCreExchange(Map mp) {
		List<Map<String,Object>> lt=new ArrayList();
		if(mp.size()>0||null!=mp){
			lt=giftExchangeRecordDao.selectCreExchange(mp);
			for(Map m :lt){
				m.put("logisticsUrl", "");
				if(m.get("sellerLogisticsId")!=null && !"".equals(m.get("sellerLogisticsId").toString())){
					LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(m.get("sellerLogisticsId").toString()));
					try {
						m.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),m.get("logisticsCode").toString()));//快递公司编码,快递单号
					} catch (Exception e) {
						m.put("logisticsUrl", "");
					}
				}//增加返回物流信息
				if(m.get("mobileSecret")!=null && !"".equals(m.get("mobileSecret").toString())){
					try {
						DESCoder.instanceMobile();
						String mobile = DESCoder.decoder(m.get("mobileSecret").toString()).trim();//对手机号码解密
						m.put("mobileSecret", mobile);
					} catch (Exception e) {
						m.put("mobileSecret", "");
					}
				}
			}
		}
		return lt;
	}
	/**
	 * 查询礼品兑换总记录数
	 */
	@Override
	public Integer count(Map mp) {
		return giftExchangeRecordDao.count(mp);
	}
	
	/**
	 * APP查询 礼品兑换记录详情
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map selectAppGiftInfo(String dealerId, String id) {
		Map m = giftExchangeRecordDao.selectAppGiftInfo(dealerId,id);
		if(m!=null){
			if(m.get("sellerLogisticsId")!=null && !"".equals(m.get("sellerLogisticsId").toString())){
				LogisticsCompany  logistics= LogisticsCompanyI.fingById(Integer.parseInt(m.get("sellerLogisticsId").toString()));
				try {
					m.put("logisticsUrl", Delivery.search(logistics.getLogisticsCode(),m.get("logisticsCode").toString()));//快递公司编码,快递单号
				} catch (Exception e) {
					m.put("logisticsUrl", "");
				}
			}//增加返回物流信息
			if(m.get("mobileSecret")!=null && !"".equals(m.get("mobileSecret").toString())){
				try {
					DESCoder.instanceMobile();
					String mobile = DESCoder.decoder(m.get("mobileSecret").toString()).trim();//对手机号码解密
					m.put("mobileSecret", mobile);
				} catch (Exception e) {
					m.put("mobileSecret", "");
				}
			}
		}
		return m;
	}  
	
	/**
	 * 根据礼品所需要的积分对dealer表中的积分字段进行扣减
	 * @param Integer dealerId
	 * @return 
	 */
	@Override
	public synchronized Long updateScoreById(HttpServletRequest request,Map mp,GiftExchangeRecord gr) throws Exception{
		int m=0;
		if(StringUtils.isNotBlank(request.getParameter("giftId"))){
			Integer getScore=giftMapperDao.selectScoreById(Integer.parseInt(request.getParameter("giftId")));
			if(null==getScore||getScore==0){
				throw new RuntimeException("没有积分");
			}
			int num = Integer.valueOf(request.getParameter("num").toString());
			if(StringUtils.isBlank(request.getParameter("num"))||num<=0){
				throw new RuntimeException("数量为空");
			}
			int surplusNum = giftMapperDao.selectSurplusNumById(Integer.parseInt(request.getParameter("giftId")));
			if(surplusNum-num<0){
				throw new RuntimeException("库存不足");
			}
			//1
			mp.put("score", getScore*num);
			if(StringUtils.isNotBlank(request.getParameter("giftId"))){
				gr.setGiftId(Integer.parseInt(request.getParameter("giftId")));
			}
			gr.setNum(num);
			gr.setScore(getScore*num);
		}else{
			throw new RuntimeException("积分兑换失败");
		}
	    //2
		Integer scoreRemainVal=dealerDao.selectScoreRemain(Integer.parseInt(mp.get("dealerId").toString()));
		if(null==scoreRemainVal||scoreRemainVal==0){
			throw new RuntimeException("没有积分");
		}
		if(scoreRemainVal<gr.getScore()){
			throw new RuntimeException("积分不够");
		}
		m=dealerDao.updateScoreById(mp);
		gr.setScoreRemain(scoreRemainVal);
		gr.setDealerId(Integer.parseInt(mp.get("dealerId").toString()));
		//3
		Register reg=RegisterDao.selectIdByDealerId(Integer.parseInt(mp.get("dealerId").toString()));	
		if(m==0||null==reg.getId()){
			throw new RuntimeException("积分兑换失败");
		}
	    gr.setUserId(reg.getId());
	    DESCoder.instanceMobile();
//		    gr.setMobile(reg.getMobile());
	    gr.setMobile(DESCoder.decoder(reg.getMobileSecret()).trim());
	    //String exchangeTime=giftMapperDao.selectExchangeTime(Integer.parseInt(request.getParameter("giftId"))); 
	    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String dateValue=df.format(new Date());
	    gr.setExchangeTime(new Timestamp(df.parse(dateValue).getTime()));
		if(StringUtils.isNotBlank(request.getParameter("consignAddressId"))){
			gr.setConsignAddressId(Integer.parseInt(request.getParameter("consignAddressId")));
			Map buyerCon=new TreeMap();
			buyerCon=buyerConsignAddressDao.selectAddress(Integer.parseInt(request.getParameter("consignAddressId")));
			Map mpRegion=new LinkedHashMap();
			mpRegion=RegionDao.selectRegion(buyerCon);
			Map saveBuyerCon=new LinkedHashMap();
			for(Object  key:buyerCon.keySet()){
				chinaToUnicode(key,buyerCon.get(key),saveBuyerCon);
			}
			for(Object  keyVal:mpRegion.keySet()){
				chinaToUnicode(keyVal,mpRegion.get(keyVal),saveBuyerCon);
			}
			Map saveBuyerConTree=new LinkedHashMap();
			for(Object  keyComTree:saveTree.keySet()){
				for(Object  keyCom:saveBuyerCon.keySet()){
					if(keyComTree.equals(keyCom)){
						saveBuyerConTree.put(keyCom, saveBuyerCon.get(keyCom));
					}
				}
			}
			JSONObject je=new JSONObject();
			gr.setConsignAddressInfo(je.fromObject(saveBuyerConTree).toString());
		}else{
			throw new RuntimeException("积分兑换失败");
		}
		Long id = PaymentIDProduce.getPaymentID();
		gr.setId(id);//按照订单ID生成规则生成 礼品兑换记录表 id modify 20161019
		//4
		Integer maxId=giftExchangeRecordDao.insert(gr);
		//5 对gift表剩余数量扣减
		giftMapperDao.updateSurplusNum(gr);
		int d=0;
		if(null!=maxId&&maxId>0){
			d= dealerScoreDao.addDealerScore(gr.getDealerId(), gr.getScore()>0?-gr.getScore():gr.getScore(),gr.getId());
		}
		else{
			throw new RuntimeException("积分兑换失败");
		}
		if(d==0){
			throw new RuntimeException("积分兑换失败");
		}
		request.getSession().setAttribute("score", scoreRemainVal-gr.getScore());
		return gr.getId();
	}
	
    /** 
     * 把中文转成Unicode码 
     * @param str 
     * @return 
     */  
    public String chinaToUnicode(Object  key,Object val,Map saveBuyerCon){  
        String result="";  
        if(null!=val){
        	for (int i = 0; i < val.toString().length(); i++){  
                int chr1 = (char) val.toString().charAt(i);  
                if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)  
                    result+="\\u" + Integer.toHexString(chr1);  
                }else{  
                    result+=val.toString().charAt(i);  
                }  
            }
        }
        saveBuyerCon.put(key, result);
        return result;  
    }
      
       }
