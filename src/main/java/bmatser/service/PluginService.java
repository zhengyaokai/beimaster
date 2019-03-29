/*

 * Support: http://www.huqiuhsc.com
 * License: http://www.huqiuhsc.com/license
 */
package bmatser.service;

import bmatser.plugin.PaymentPlugin;

/**
 * Service - 插件
 * 
 * @author www.flycent.com.cn
 * @version 3.0
 */
public interface PluginService {


	/**
	 * 获取支付插件
	 * 
	 * @param id
	 *            ID
	 * @return 支付插件
	 */
	PaymentPlugin getPaymentPlugin(String id);


}