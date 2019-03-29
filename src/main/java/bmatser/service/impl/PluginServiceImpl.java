/*

 * Support: http://www.huqiuhsc.com
 * License: http://www.huqiuhsc.com/license
 */
package bmatser.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import bmatser.plugin.PaymentPlugin;
import bmatser.service.PluginService;

/**
 * Service - 插件
 * 
 * @author www.flycent.com.cn
 * @version 3.0
 */
@Service("pluginServiceImpl")
public class PluginServiceImpl implements PluginService {

	@Resource
	private Map<String, PaymentPlugin> paymentPluginMap = new HashMap<String, PaymentPlugin>();


	public PaymentPlugin getPaymentPlugin(String id) {
		return paymentPluginMap.get(id);
	}


}