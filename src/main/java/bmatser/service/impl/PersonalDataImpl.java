package bmatser.service.impl;

import bmatser.dao.OrderGoodsMapper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerMapper;
import bmatser.dao.RegisterMapper;
import bmatser.pageModel.MyInfoPage;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.PageMode.Channel;
import bmatser.service.PersonalDataI;
import bmatser.service.SolrCacheDataI;
import bmatser.util.Constants;
import bmatser.util.DESCoder;
import bmatser.util.DateTypeHelper;
import bmatser.util.ObjectUtil;
@Service("personalDataService")
public class PersonalDataImpl implements PersonalDataI {
	
	private final  String EXCEPTION_CO = "公司名称不能为空";
	@Autowired
	private DealerMapper<MyInfoPage> dealerMapper;
	@Autowired
	private RegisterMapper registerMapper;
	@Autowired
	private RegisterMapper registerDao;
	@Autowired
	private SolrCacheDataI solrCache;

    @Autowired
    private OrderGoodsMapper orderGoodsDao;
	/**
	 * 查看个人资料
	 */
	@Override
	public Map findRegister(String dealerId) throws Exception {
		if(StringUtils.isBlank(dealerId)){
			throw new Exception("请登录");
		}
		Map map = dealerMapper.findLoginInfo(dealerId);
		if(map==null){
			throw new Exception("帐号不存在或密码错误");
		}
        /**
         * create by yr on 2018-10-22
         */
        String provinceId = map.get("provinceId")!=null?map.get("provinceId").toString():"";
        String cityId = map.get("cityId")!=null?map.get("cityId").toString():"";
        String areaId = map.get("areaId")!=null?map.get("areaId").toString():"";
        String provinceName =  orderGoodsDao.findNameById(provinceId);
        String CityName = orderGoodsDao.findNameById(cityId);
        String AreaName = orderGoodsDao.findNameById(areaId);
        map.put("provinceName",provinceName);
        map.put("CityName",CityName);
        map.put("AreaName",AreaName);


        if("4".equals(map.get("dealerType"))||"5".equals(map.get("dealerType"))){
			return map;
		}
		map.put("staffName", "");
		map.put("staffMobile", "");
		Map m = dealerMapper.getBusinessManagerInfo(dealerId);//增加查询专属商务经理信息
		if(m!=null&&m.size()>0){
			map.put("staffName", m.get("staffName"));
			map.put("staffMobile", m.get("staffMobile"));
			map.put("qq", m.get("qq"));
		}
		map.put("tel",m.get("userName"));
		return map;
	}
	/**
	 * @author felx
	 * create time : 2016/5/19
	 * 查询个人等级，等级积分，距离升级还需多少等级积分以及超过多少用户
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	public Map findDealerRankAndRankScore(String dealerId) throws Exception{
		if(StringUtils.isBlank(dealerId)){
			throw new Exception("请登录");
		}
		Map map = dealerMapper.findDealerRankAndRankScore(dealerId);
		if(map==null){
			throw new Exception("帐号不存在或密码错误");
		}
		//取人员的积分升序排名位置
		String integralPosition = map.get("rownum").toString();
		//查询dealer表总人数
		int count = dealerMapper.countDealer();
		//根据等级积分算人员超过多少用户
		double percent = Double.parseDouble(integralPosition)/Double.parseDouble(String.valueOf(count))*100;
		BigDecimal decimal = new BigDecimal(percent); 
		double percentDealer = decimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();//保留两位小数
		//超过多少用户的数值放入map
		map.put("percentDealer", percentDealer);
		//取人员的等级积分
		int rankScore = Integer.parseInt(map.get("rankScore").toString());
		//取配置文件中的等级积分
		String DRS = JFig.getInstance().getValue("rank_score", "DRS");
		String[] dealerRank = DRS.split(",");
		int l=dealerRank.length;
		//等级积分大于等于最大值还需0积分升级 modify 20160705 
		if(rankScore>=Integer.parseInt(dealerRank[l-1])){
			map.put("upgrade",0);
		}else{
			//根据个人等级积分和等级积分区间算人员还需多少积分升级 放入map
			for(int i=0;i<dealerRank.length;i++){
				
				if(rankScore>=0 && rankScore<=Integer.parseInt(dealerRank[i])){
					map.put("upgrade", Integer.parseInt(dealerRank[i])-rankScore+1);
					break;
				}
			}
		}
		
		return map;
	}
	/**
	 * 更新个人资料
	 * @throws Exception 
	 */
	@Override
	public void updateRegister(MyInfoPage info) throws Exception {
		if(StringUtils.isNotBlank(info.getDealerName())){
			if(StringUtils.isBlank(info.getProvinceId())){
				info.setProvinceId("0");
			}
			if(StringUtils.isBlank(info.getCityId())){
				info.setCityId("0");
			}
			if(StringUtils.isBlank(info.getAreaId())){
				info.setAreaId("0");
			}
			/**判断获取图片地址*/
			if(StringUtils.isBlank(info.getLogo())){
				info.setLogo("0.jpg");
			}else{
				String[] logo = info.getLogo().split(Constants.BASE_URL_NET);
				info.setLogo(logo[logo.length-1]);
			}
			/**换算日期*/
			if(StringUtils.isNotBlank(info.getBirthday())){
				info.setBirthday(DateTypeHelper.getTimestamp(info.getBirthday(), "yyyy/MM/dd").toString());
			}
			if(StringUtils.isNotBlank(info.getTelephone())){
				if(info.getTelephone().contains("*")){
					info.setTelephone(null);
					info.setTelephoneSecret(null);
				}else{
					DESCoder.instanceMobile();
					info.setTelephoneSecret(DESCoder.encoder(info.getTelephone()).trim());//对手机号码加密
					info.setTelephone(DESCoder.getNewTel(info.getTelephone()));//隐藏手机号码
				}				
			}else{
				info.setTelephone("");
				info.setTelephoneSecret("");
			}
			if(StringUtils.isNotBlank(info.getLinkTel())){
				if(info.getLinkTel().contains("*")){
					info.setLinkTel(null);
					info.setLinkTelSecret(null);
				}else{
					DESCoder.instanceMobile();
					info.setLinkTelSecret(DESCoder.encoder(info.getLinkTel()).trim());//对手机号码加密
					info.setLinkTel(DESCoder.getNewTel(info.getLinkTel()));//隐藏手机号码
				}				
			}else{
				info.setLinkTel("");
				info.setLinkTelSecret("");
			}
			dealerMapper.updatePerson(info);
		}else{
			throw new Exception(EXCEPTION_CO);
		}
		
	}
	@Override
	public Map<String, Object> getDealerCashBack(String dealerId) {
		return dealerMapper.getDealerCashBack(dealerId);
	}
	
	@Override
	public Map<String, Object> getDealerCashBackDetail(String dealerId, int page,
			int rows,String startTime,String endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", dealerMapper.getDealerCashBackDetail(dealerId,page,rows,startTime,endTime));
		map.put("count", dealerMapper.getDealerCashBackDetailCount(dealerId,startTime,endTime));
		return map;
	}
	@Override
	public void toCashBack(PageMode model) throws Exception {
		String dealerId = model.getDealerId(Channel.SAAS);
		if(model.contains("cardId","amount")){//cardId 银行卡Id amount 返现金额
			throw new RuntimeException("参数不存在");
		}
		String cardId = model.getStringValue("cardId");
		BigDecimal amount = model.getBigDecimalValue("amount");
		Map<String, Object> data = ObjectUtil.isReturn(
						dealerMapper.getDealerCashBack(dealerId), 
						"用户返现信息不存在"
					);
		BigDecimal cashback = new BigDecimal(data.get("cashback").toString());
		if(cashback.compareTo(amount)<0){
			throw new RuntimeException("返现金额超出余额");
		}
		int isExist = dealerMapper.isExistBankCard(dealerId,cardId);
		if(isExist<1){
			throw new RuntimeException("银行卡错误");
		}
		dealerMapper.addWithdrawals(dealerId, cardId, amount);
	}
	
	
	@Override
	public List<Map<String, Object>> getBankCard(String dealerId) {
		List<Map<String, Object>> data = dealerMapper.getBankCard(dealerId);
		for (Map<String, Object> map : data) {
			String card = map.get("card")!=null?map.get("card").toString():"";
			if(card.length()>8){
				map.put("card", card.substring(0,4) +" **** **** " +card.substring(card.length()-4, card.length()));
			}
		}
		return data;
	}
	@Override
	public List<Map<String, Object>> getApplyDetail(String dealerId) {
		// TODO Auto-generated method stub
		return dealerMapper.getApplyDetail(dealerId);
	}

	/**
	 * @author felx
	 * @describe 年度账单
	 * @param date 时间
	 * @param dealerId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAnnualBill(String date, String dealerId) {
		List<Map<String, Object>> list = dealerMapper.getAnnualBill(date,dealerId);
		BigDecimal amount = BigDecimal.ZERO;
		for (Map<String, Object> map : list) {
			Integer brandId = Integer.parseInt(map.get("brandId").toString());
			Map brandMap = solrCache.findBrand();
			String brand = brandMap.get(brandId).toString();
			map.put("brandName", brand);
			amount = amount.add(new BigDecimal(map.get("price").toString()));
		}
		for (Map<String, Object> map : list) {
			BigDecimal price = new BigDecimal(map.get("price").toString());
			String scale = price.divide(amount,6).multiply(new BigDecimal(100)).setScale(4).toString() + "%";
			map.put("scale", scale);
		}
		return list;
	}
}
