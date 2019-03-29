package bmatser.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sf.openapi.common.entity.AppInfo;
import com.sf.openapi.common.entity.HeadMessageReq;
import com.sf.openapi.common.entity.MessageReq;
import com.sf.openapi.common.entity.MessageResp;
import com.sf.openapi.common.utils.SFOpenClient;
import com.sf.openapi.express.sample.order.dto.*;
import com.sf.openapi.express.sample.order.tools.OrderTools;
import com.sf.openapi.express.sample.security.dto.TokenReqDto;
import com.sf.openapi.express.sample.security.dto.TokenRespDto;
import com.sf.openapi.express.sample.security.tools.SecurityTools;
import bmatser.plugin.unionpay.sdk.SDKConstants;
import bmatser.service.ExpressI;
import bmatser.model.ExpressRequest;
import bmatser.model.ExpressResponse;
import bmatser.util.SfOrderResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.util
 * @ClassName: SfExpressesImpl
 * @Description: 顺丰快递操作类
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/2 15:47
 */
@Service("sfExpressesService")
public class SfExpressesImpl implements ExpressI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SfExpressesImpl.class);


    @Value("${express.sf.appId}")
    private  String sfAppId; //顺丰开发平台申请的appId
    @Value("${express.sf.appKey}")
    private  String sfAppKey;//顺丰开发平台申请的appKey
    @Value("${express.sf.custId}")
    private  String custId;//顺丰开发平台申请的寄方月结账号
    @Value("${express.sf.tokenUrl}")
    private  String applyTokenUrl;//顺丰开发平台获取访问令牌的URL
    @Value("${express.sf.orderUrl}")
    private  String orderUrl;//顺丰开发平台快速下单的URL
    @Value("${express.sf.placeHoder}")
    private  String orderUrlPlaceHoder;//用来替换URL中的accessToken的占位符
    @Value("${express.sf.queryUrl}")
    private  String orderQueryUrl;//顺丰开发平台根据开发者订单查询运单的URL
    @Value("${express.sf.expressType}")
    private short expressType; //顺丰下单常用快递类型(1顺丰标快；2顺丰特惠；3电商特惠...其它)
    @Value("${express.sf.payMethod}")
    private short payMethod; //顺丰付款方式(Number(1))


    @Override
    public ExpressResponse expressOrder(ExpressRequest eRequest) {
        ExpressResponse ep = new ExpressResponse(eRequest.getExpressCompany(),eRequest.getOrderId(),eRequest.getRemark());
        try{
            AppInfo appInfo = getSfAccessToken();
            if(StringUtils.isNotEmpty(appInfo.getAccessToken())){
                String currentOrderUrl = orderUrl.replace(orderUrlPlaceHoder,appInfo.getAccessToken());
                MessageReq<OrderReqDto> req  = getSfOrderMessageReq();
                OrderReqDto orTo = getShunFengRequest(eRequest);
                orTo.setCustId(custId);
                orTo.setExpressType(expressType);
                orTo.setPayMethod(payMethod);
                req.setBody(orTo);
                MessageResp<SfOrderResponse> resp = sfOrder(currentOrderUrl,appInfo,req);
                if(resp.getBody()!=null && "01".equals(resp.getBody().getRemarkCode()) ){
                    ep.setStatus(SDKConstants.SUCCESS);
                    ep.setLogisticsRemark(eRequest.getRemark());
                    LOGGER.info("GDB订单"+ep.getOrderId()+"下顺丰运单成功;RemarkCode="+resp.getBody().getRemarkCode());
                }else{
                    ep.setStatus(SDKConstants.FAIL);
                    LOGGER.info("GDB订单"+ep.getOrderId()+"下顺丰运单失败;没有返回主体或");
                }
                if(resp.getHead()!=null ){
                    ep.setMsg(resp.getHead().getMessage());
                    LOGGER.info("GDB订单"+eRequest.getOrderId()+"下顺丰运单失败返回信息:"+ep.getMsg());
                }
            }
        }catch (Exception e){
            LOGGER.error("GDB订单"+ep.getOrderId()+"下顺丰运单异常;异常原因:"+e.getMessage());
        }
        return ep;
    }

    /**
     * 顺丰单号查询
     * @param eRequest
     * @return
     */
    @Override
    public ExpressResponse  expressQuery(ExpressRequest eRequest) {
        ExpressResponse ep = new ExpressResponse(eRequest.getExpressCompany(),eRequest.getOrderId(),eRequest.getRemark());
        try{
            AppInfo appInfo = getSfAccessToken();
            if(StringUtils.isNotEmpty(appInfo.getAccessToken())){
                MessageReq<OrderQueryReqDto> req = getSfOrderQueryMessageReq();
                OrderQueryReqDto oqr = new OrderQueryReqDto();
                oqr.setOrderId(eRequest.getOrderId());
                req.setBody(oqr);
                String currentQueryUrl = orderQueryUrl.replace(orderUrlPlaceHoder,appInfo.getAccessToken());
                MessageResp<OrderQueryRespDto> resp = OrderTools.orderQuery(currentQueryUrl,appInfo,req);
                if(resp.getBody()!=null && StringUtils.isNotEmpty(resp.getBody().getMailNo())){
                    LOGGER.info("GDB订单"+eRequest.getOrderId()+"查询顺丰运单号成功;运单号（mailNo）"+resp.getBody().getMailNo());
                    ep.setExpressOrderId(resp.getBody().getMailNo());
                    ep.setStatus(SDKConstants.SUCCESS);
                }else{
                    LOGGER.info("GDB订单"+eRequest.getOrderId()+"查询顺丰运单号失败;");
                }
                if(resp.getHead()!=null ){
                    ep.setMsg(resp.getHead().getMessage());
                    LOGGER.info("GDB订单"+eRequest.getOrderId()+"查询顺丰运单返回信息:"+ep.getMsg());
                }
            }
        }catch (Exception e){
            LOGGER.error("GDB订单"+ep.getOrderId()+"查询顺丰运单号异常;异常原因:"+e.getMessage());
        }
        return ep;
    }

    @Override
    public ExpressResponse cancelExpressOrder(ExpressRequest eRequest) {
        return null;
    }

    @Override
    public ExpressResponse gdbExpressOrder(ExpressRequest eRequest) {
        ExpressResponse response =  expressQuery(eRequest);
        if(SDKConstants.SUCCESS.equals(response.getStatus()) && org.apache.commons.lang.StringUtils.isNotEmpty(response.getExpressOrderId())){
            response.setMsg("此工电宝订单已有对应顺丰运单,请勿重复下单！");
        }else{
            response = expressOrder(eRequest);
            if(SDKConstants.SUCCESS.equals(response.getStatus())){
                response =  expressQuery(eRequest);
            }
        }
        return response;
    }

    @Override
    public String printWaybill(String orderId,Map<String,Object> map) {
        return null;
    }


    /**
     * 获取accessToken 所需要的MessageReq
     * TransType(301)
     * @return
     */
    public MessageReq<TokenReqDto> getTokenQueryMessageReq(){
        MessageReq<TokenReqDto> accessTokenReq = new MessageReq<TokenReqDto>();
        HeadMessageReq header = new HeadMessageReq();
        header.setTransMessageId("201408192052000001");
        header.setTransType(301);
        accessTokenReq.setHead(header);
        return accessTokenReq;
    }
    /**
     * 顺丰下单 所需要的MessageReq
     * TransType(200)
     * @return
     */
    public MessageReq<OrderReqDto> getSfOrderMessageReq(){
        MessageReq<OrderReqDto> req  = new MessageReq<OrderReqDto>();
        HeadMessageReq header = new HeadMessageReq();
        header.setTransMessageId("201409040916141677");
        header.setTransType(200);
        req.setHead(header);
        return req;
    }

    /**
     * 顺丰查单 所需要的MessageReq
     * TransType(203)
     * @return
     */
    public MessageReq<OrderQueryReqDto>  getSfOrderQueryMessageReq(){
        MessageReq<OrderQueryReqDto>  req  = new MessageReq<OrderQueryReqDto>();
        HeadMessageReq header = new HeadMessageReq();
        header.setTransMessageId("201409040916141677");
        header.setTransType(203);
        req.setHead(header);
        return req;
    }


    /**
     * 顺丰速运类 接口的accessToken 获取
     */
    public  AppInfo getSfAccessToken() {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppId(sfAppId);
        appInfo.setAppKey(sfAppKey);
        try {
            MessageReq<TokenReqDto> accessTokenReq = getTokenQueryMessageReq();
            MessageResp<TokenRespDto> mesTokenRespDto = null;
            mesTokenRespDto = SecurityTools.applyAccessToken(applyTokenUrl,appInfo,accessTokenReq);
            if(mesTokenRespDto!=null && mesTokenRespDto.getBody()!=null){
                TokenRespDto tokenRespon = mesTokenRespDto.getBody();
                appInfo.setAccessToken(tokenRespon.getAccessToken());
                appInfo.setRefreshToken(tokenRespon.getRefreshToken());
            }
            LOGGER.info("申请顺丰接口的访问令牌成功;令牌号:"+appInfo.getAccessToken());
        } catch (Exception e) {
            LOGGER.error("申请顺丰接口的访问令牌失败;异常原因:"+e.getMessage());
        }
        return appInfo;
    }

    /**
     * 顺丰下单方法重写，因为显示API成功返回remarkCode，OrderRespDto 对象里没有，也没ignore
     * @param url
     * @param appInfo
     * @param req
     * @return
     * @throws Exception
     */
    public static MessageResp<SfOrderResponse> sfOrder(String url, AppInfo appInfo, MessageReq<OrderReqDto> req) throws Exception {
        Map<String, String> paramMap = new HashMap();
        paramMap.put("sf_appid", appInfo.getAppId());
        paramMap.put("sf_appkey", appInfo.getAppKey());
        paramMap.put("access_token", appInfo.getAccessToken());
        SFOpenClient client = SFOpenClient.getInstance();
        return (MessageResp)client.doPost(url, req, new TypeReference<MessageResp<SfOrderResponse>>() {
        }, paramMap);
    }


    /**
     *  根据请求参数包装成顺丰的请求对象(Object 替换成 顺丰的SDK里具体类)
     * @param eRequest
     */

    public OrderReqDto getShunFengRequest(ExpressRequest eRequest){
        OrderReqDto ord = new OrderReqDto();
        ord.setOrderId(eRequest.getOrderId());
        ord.setRemark(eRequest.getRemark());

        //顺丰发货人信息
        DeliverConsigneeInfoDto deliverInfo = new DeliverConsigneeInfoDto();
        deliverInfo.setCompany(eRequest.getDeliverCompany());
        deliverInfo.setTel(eRequest.getDeliverTel());
        //deliverInfo.setCountry(); 顺丰sdk默认 中国
        deliverInfo.setProvince(eRequest.getDeliverProvince());
        deliverInfo.setCity(eRequest.getDeliverCity());
        deliverInfo.setAddress(eRequest.getDeliverAddress());
        deliverInfo.setContact(eRequest.getDeliverLinkMan());
        deliverInfo.setMobile(eRequest.getDeliverLinkTel());
        ord.setDeliverInfo(deliverInfo);
        //顺丰收货人信息
        DeliverConsigneeInfoDto consigneeInfo = new DeliverConsigneeInfoDto();
        consigneeInfo.setCompany(eRequest.getConsignCompany());
        consigneeInfo.setContact(eRequest.getConsignConsignee());
        consigneeInfo.setTel(eRequest.getConsignLinkTel());
        consigneeInfo.setProvince(eRequest.getConsignProvince());
        consigneeInfo.setCity(eRequest.getConsignCity());
        consigneeInfo.setAddress(eRequest.getConsignAddress());
        consigneeInfo.setMobile(eRequest.getConsignMobile());
        ord.setConsigneeInfo(consigneeInfo);
        //顺丰包裹信息
        CargoInfoDto cargoInfo = new CargoInfoDto();
        cargoInfo.setParcelQuantity(eRequest.getParcelQuantity());
        cargoInfo.setCargo(eRequest.getCargo());
        ord.setCargoInfo(cargoInfo);
        return ord;
    }








}
