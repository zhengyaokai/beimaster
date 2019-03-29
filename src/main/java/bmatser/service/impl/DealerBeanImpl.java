package bmatser.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.DealerBeanMapper;
import bmatser.model.DealerBean;
import bmatser.service.DealerBeanI;
@Service("beanService")
public class DealerBeanImpl implements DealerBeanI{
	@Autowired
	private DealerBeanMapper beanDao;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map getDealerBeanList(Integer dealerId, int page, int rows) {
		Map m = new HashMap();
		//查询用户可用工币
		Long available = beanDao.getBean(dealerId);
		if(available==null){
			m.put("available", 0);
		}else{
			m.put("available", available);
		}
		//查询用户冻结工币
		Long freeze = beanDao.getFreezeBean(dealerId);
		if(freeze==null){
			m.put("freeze", 0);
		}else{
			m.put("freeze", freeze);
		}
		//查询用户工币列表
		List list = beanDao.getDealerBeanList(dealerId,page,rows);
		int count = beanDao.getDealerBeanCount(dealerId);
		m.put("list", list);
		m.put("count", count);
		return m;
	}

	@Override
	public Map getFreezeDealerBeanList(Integer dealerId, int page, int rows) {
		Map m = new HashMap();
		Calendar calendar = Calendar.getInstance();
		//查询用户冻结工币列表
		List<DealerBean> list = beanDao.getFreezeDealerBeanList(dealerId,page,rows);
		if(list!=null && list.size()!=0){
			for(DealerBean dealerBean : list){
				if(dealerBean.getConsignTime()!=null&&"6".equals(dealerBean.getOrderStatus())){
					/*dealerBean.setConsignTime(null);
					calendar.add(Calendar.DAY_OF_MONTH,-7);
					Date date=calendar.getTime();*/
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					/*System.out.println(sf.format(date));
					long time =date.getTime();
					if(dealerBean.getConsignTime().getTime()-time<0){*/
						Date d =new Date(dealerBean.getConsignTime().getTime());
						calendar.setTime(d);
						calendar.add(Calendar.DAY_OF_MONTH,7);
						System.out.println(sf.format(calendar.getTime()));
						dealerBean.setConsignTime(new Timestamp(calendar.getTime().getTime()));
					/*}*/
					
				}else{
					dealerBean.setConsignTime(null);
				}
				/*if("5".equals(dealerBean.getStatus())){
					dealerBean.setRemark("订单未付款，取消");
				}*/
			}
		}
		
		int count = beanDao.getFreezeDealerBeanCount(dealerId);
		m.put("list", list);
		m.put("count", count);
		return m;
	}

}
