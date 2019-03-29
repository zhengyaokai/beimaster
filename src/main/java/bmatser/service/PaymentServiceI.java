package bmatser.service;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;

import cn.beecloud.BCEumeration.PAY_CHANNEL;

import bmatser.model.LoginInfoUtil;
import bmatser.model.PayInfo;
import bmatser.pageModel.PageMode;
import bmatser.util.LoginInfo;


public interface PaymentServiceI {

/*	String b2bPaymentCost(PageMode model) throws Exception;

	void getB2bCostStatus(String orderId, String txnTime) throws Exception;*/

	void updateIsBalance(boolean isbalance, String orderId);

	void saveOrderByBalance(String orderId);

	/**
	 * balance payment
	 * 余额支付
	 * @param model
	 * @param login
	 * @return
	 * @throws Exception
	 */
	int toPayByOffline(PageMode model, LoginInfo login) throws Exception;

	String toBeecloudPay(String orderId, BigDecimal amount, String paymentPluginId,Map<String, Object> param) throws Exception;
	/**
	 * 威富通支付宝，微信扫码支付
	 * add 20170209
	 */
	String toWFTPay(String orderId, BigDecimal amount, String paymentPluginId,HttpServletRequest request) throws Exception;
	/**
	 *	@author jxw
	 *	@Date 2016-12-16
	 *	@description  返回付款信息
	 * @param orderId 订单号
	 * @param loginInfo 登录信息
	 * @param isUse 是否使用余额
	 * @return
	 */
	PayInfo toAppPay(String orderId, LoginInfoUtil loginInfo, String isUse);

	/**
	 * 
	 *	@author jxw
	 *	@Date 2016-12-16
	 *	@description 手机端获取最终付款金额
	 * @param orderId 订单号
	 * @param loginInfo 登录信息
	 * @param isUse 是否使用余额
	 * @return
	 */
	BigDecimal toAppPayByBalance(String orderId, LoginInfoUtil loginInfo,
			String isUse);

}
