package bmatser.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zto.zop.ZopClient;
import com.zto.zop.ZopPublicRequest;
import bmatser.dao.OrderInfoMapper;
import bmatser.model.ExpressRequest;
import bmatser.model.ExpressResponse;
import bmatser.model.OrderInfo;
import bmatser.plugin.unionpay.sdk.SDKConstants;
import bmatser.service.ExpressI;
import bmatser.util.alibaba.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ProjectName: gdb_serviceApi
 * @Package: bmatser.util
 * @ClassName: ZtoExpressesImpl
 * @Description: 中通快递操作类
 * @Author: zhengyaokai
 * @CreateDate: 2018/8/2 15:47
 */
@Service("ztoExpressesService")
public class ZtoExpressesImpl implements ExpressI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZtoExpressesImpl.class);


    @Value("${express.zto.companyId}")
    private  String ztoCompanyId; //中通平台申请的CompanyId
    @Value("${express.zto.key}")
    private  String ztoKey;//中通平台申请的Key
    @Value("${express.zto.orderUrl}")
    private  String ztoOrderUrl;//中通平台下订单的URL
    @Value("${express.zto.cancelUrl}")
    private  String ztoCancelOrderUrl;//中通平台取消订单的URL
    @Value("${express.zto.partner}")
    private  String ztoPartner;//电子面单账号
    @Value("${express.zto.esOrderUrl}")
    private  String ztoEsOrderUrl;//中通平台电子面单URL
    @Value("${express.zto.searchUrl}")
    private  String ztoSearchUrl;//中通平台订单明细查询,根据中通订单号查询运单号或状态
    @Value("${express.zto.printUrl}")
    private  String ztoPrintUrl;//中通平台云打印(二维码)URL
    @Value("${express.zto.bagMarkUrl}")
    private  String ztoBagMarkUrl;//中通集包第大头笔获取

    /**
     * 中通下单
     * @param eRequest
     * @return
     */
    @Override
    public ExpressResponse expressOrder(ExpressRequest eRequest) {
        ExpressResponse ep = new ExpressResponse(eRequest.getExpressCompany(),eRequest.getOrderId(),eRequest.getRemark());
        ep.setStatus(SDKConstants.FAIL);
        try{
            Map<String, String> params = getZTOOrderRequest(eRequest);//预约寄付-订单创建
            String jsonReponse = wapperZtoClient(ztoOrderUrl,params,ztoCompanyId,ztoKey);
            if(StringUtils.isNotEmpty(jsonReponse)){
                ZtoReponse ztoReponse = JSONObject.parseObject(jsonReponse,ZtoReponse.class);
                if(ztoReponse!=null){
                    ep.setStatus(ztoReponse.isStatus()? SDKConstants.SUCCESS:SDKConstants.FAIL);
                    ep.setMsg(ztoReponse.getMessage()+"中通订单号（非运单号）");
                    if(StringUtils.isNotEmpty(ztoReponse.getResult()) && ztoReponse.isStatus()) {
                        ZtoReponse successReponse = JSONObject.parseObject(ztoReponse.getResult(),ZtoReponse.class);
                        ep.setExpressOrderId(successReponse.getOrderCode());
                        LOGGER.info("GDB订单["+eRequest.getOrderId()+"]中通快递下订单成功;订单号（非运单号）"+successReponse.getOrderCode());
                    }
                }else{
                    LOGGER.error("GDB订单["+eRequest.getOrderId()+"]中通快递下订单后没有返回信息!");
                }
            }
        }catch (Exception e){
            LOGGER.error("GDB订单["+eRequest.getOrderId()+"]中通快递下订单异常;异常原因:"+e.getMessage());
        }
        return ep;
    }

    /**
     * 中通单号查询
     * @param eRequest
     * @return
     */
    @Override
    public ExpressResponse expressQuery(ExpressRequest eRequest) {
        ExpressResponse ep = new ExpressResponse(eRequest.getExpressCompany(),eRequest.getOrderId(),eRequest.getRemark());
        ep.setStatus(SDKConstants.FAIL);
        try{
            if(StringUtils.isNotEmpty(eRequest.getLogisticsCode())){
                Map<String, String> params = getZhongongSearchByCodeRequest(eRequest);
                String jsonReponse = wapperZtoClient(ztoSearchUrl,params,ztoCompanyId,ztoKey);
                if(StringUtils.isNotEmpty(jsonReponse)){
                    Map<String,Object> searchReponseMap = JSONObject.parseObject(jsonReponse,Map.class);
                    if(searchReponseMap!=null){
                        if(searchReponseMap.containsKey("result")){
                            ep.setStatus((boolean)searchReponseMap.get("result")?SDKConstants.SUCCESS:SDKConstants.FAIL);
                        }else{
                            ep.setStatus(SDKConstants.FAIL);
                        }
                        String reponseMsg = searchReponseMap.containsKey("reason")? String.valueOf(searchReponseMap.get("reason")):"";
                        if(StringUtils.isNotEmpty(reponseMsg) && searchReponseMap.containsKey("message")){
                            reponseMsg = String.valueOf(searchReponseMap.get("message"));
                        }
                        ep.setMsg(reponseMsg);
                        if(SDKConstants.SUCCESS.equals(ep.getStatus()) && searchReponseMap.containsKey("data")) {
                            Map<String,Object> dataMap = JSONObject.parseObject(String.valueOf(searchReponseMap.get("data")),Map.class);
                            if(dataMap!=null && dataMap.containsKey("order_list")){
                                List<String> jsonArray = JSONArray.parseArray(String.valueOf(dataMap.get("order_list")), String.class);
                                if(jsonArray!=null && jsonArray.size()>0){
                                    String ztoMailnoStr = "";
                                    for(int i=0;i<jsonArray.size();i++){
                                        Map<String,Object> orderMap = JSONObject.parseObject(String.valueOf(jsonArray.get(i)),Map.class);
                                        if(orderMap!=null && orderMap.containsKey("mailNo")){
                                            String ztoItemMailno = String.valueOf(orderMap.get("mailNo"));
                                            if(i==jsonArray.size()-1){
                                                ztoMailnoStr = ztoMailnoStr + ztoItemMailno;
                                            }else{
                                                ztoMailnoStr = ztoMailnoStr + ztoItemMailno + ",";
                                            }
                                            LOGGER.info("GDB订单["+eRequest.getOrderId()+"]中通订单号("+eRequest.getLogisticsCode()+")快递查询运单号成功;运单号-"+(i+1)+":"+ztoMailnoStr);
                                        }
                                    }
                                    ep.setExpressOrderId(ztoMailnoStr);
                                }
                            }
                        }
                    }else{
                        LOGGER.error("GDB订单["+eRequest.getOrderId()+"]中通订单号("+eRequest.getLogisticsCode()+")，查询中通运单号没有返回信息!");
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("GDB订单["+eRequest.getOrderId()+"]中通订单号("+eRequest.getLogisticsCode()+")查询中通运单号异常;异常原因:"+e.getMessage());
        }
        return ep;
    }

    /**
     * 对于GDB的下单业务方法
     * @param eRequest
     * @return
     */
    @Override
    public ExpressResponse gdbExpressOrder(ExpressRequest eRequest) {
        return expressOrder(eRequest);
    }

    /**
     * 中通API 云打印-打印二维码
     * @param orderId
     * @return
     */
    @Override
    public String printWaybill(String orderId,Map<String,Object> map) {
        try {
            ExpressRequest eRequest = (ExpressRequest) map.get("req");

            Map<String,Object> params = new LinkedHashMap<String, Object>();
            params.put("partnerCode",orderId);//订单号(?运单号)
            params.put("printChannel","ZTO-WECHAT-SCANPRINT");//打印渠道(填写：ZOP）
            params.put("deviceId","GDBDEV");// 设备ID非台式云打印机必传
            params.put("printType","QRCODE_EPRINT");// 打印类型：1.远程打印（REMOTE_EPRINT）；2.二维码打印(QRCODE_EPRINT);
            params.put("printerId","GDBPr");// 打印机ID 如果是连接pc端的普通打印机，必传


            Map<String, Object> printParams = new LinkedHashMap<String, Object>();
            printParams.put("paramType","ELEC_MARK");//指定电子面单和指定大头笔信息（ELEC_MARK）
            printParams.put("mailNo",eRequest.getLogisticsCode());//运单号
          /*Map<String,String> bagMarkMap = ztoBagAddrMarkGetmark(eRequest);//查询中通集包地和大头笔
           if(bagMarkMap==null || bagMarkMap.isEmpty()){
                return "";
            }
            printParams.put("printMark",bagMarkMap.get("mark"));//大头笔
            printParams.put("printBagaddr",bagMarkMap.get("bagAddr"));//集包地*/

            printParams.put("printMark","230-");//大头笔
            printParams.put("printBagaddr","南京");//集包地

            params.put("printParam",printParams);//业务数据

            Map<String, Object> senderParams = new LinkedHashMap<String, Object>();//发件人信息
            senderParams.put("id",String.valueOf(eRequest.getSellerId()));//发件人在GDB平台中的ID号
            senderParams.put("name",eRequest.getDeliverLinkMan());//发件人姓名
            senderParams.put("company",eRequest.getDeliverCompany());//发件公司名
            senderParams.put("mobile",eRequest.getDeliverLinkTel());//发件人手机号码
            senderParams.put("prov",eRequest.getDeliverProvince());//发件人省
            senderParams.put("city",eRequest.getDeliverCity());//发件人市
            senderParams.put("county",eRequest.getDeliverArea());//发件人区
            senderParams.put("address",eRequest.getDeliverAddress());//发件人详细地址
            params.put("sender",senderParams);

            Map<String, Object> receiverParams = new LinkedHashMap<String, Object>();//收件人信息
            receiverParams.put("name",eRequest.getConsignConsignee());//收件人姓名
            receiverParams.put("company",eRequest.getConsignCompany());//收件公司名
            receiverParams.put("mobile",eRequest.getConsignMobile());//收件人手机号码
            receiverParams.put("prov",eRequest.getConsignProvince());//收件人省
            receiverParams.put("city",eRequest.getConsignCity());//收件人市
            receiverParams.put("county",eRequest.getConsignArea());//收件人区
            receiverParams.put("address",eRequest.getConsignAddress());//收件人详细地址
            params.put("receiver",receiverParams);

            Map<String, String> toParams = new HashMap<String, String>();
            toParams.put("request",JSONObject.toJSONString(params));

            //toParams.put("request","{\"partnerCode\": \"321442342342\",\"printChannel\": \"ZTO-WECHAT-SCANPRINT\",\"deviceId\":\"test\",\"printType\": \"QRCODE_EPRINT\",\"printerId\": \"rrwwwe\",\"printParam\": {\"paramType\": \"ELEC_MARK\",\"mailNo\": \"1324123424\",\"printMark\": \"230-\",\"printBagaddr\": \"南京\"},\"receiver\": {\"address\": \"haha\",\"city\": \"北京市\",\"county\": \"优衣\",\"mobile\": \"17887878987\",\"name\": \"海哥\",\"phone\": \"0231-89080980\",\"prov\": \"北京\"},\"sender\": {\"address\": \"华新镇\",\"city\": \"上海市\",\"county\": \"青浦区\",\"mobile\": \"115667656765\",\"name\": \"牛哥\",\"phone\": \"0231-8969876\",\"prov\": \"上海\"}}");
            String printReponse = wapperZtoClient(ztoPrintUrl,toParams,ztoCompanyId,ztoKey);
            LOGGER.info("GDB订单号["+orderId+"]中通A云打印二维码结果;\n"+printReponse);
            if(StringUtils.isNotEmpty(printReponse)){
                Map<String,String> printReponseMap = JSONObject.parseObject(printReponse,Map.class);
                if(printReponseMap!=null && printReponseMap.containsKey("result")){
                    Map<String,String> printResultMap = JSONObject.parseObject(String.valueOf(printReponseMap.get("result")),Map.class);
                    if(printResultMap!=null && printResultMap.containsKey("printString")){
                        return printResultMap.get("printString");
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("GDB订单号["+orderId+"]中通云打印二维码异常;异常原因:\n"+e.getMessage());
        }
        return "";
    }


    /**
     * 中通集包地大头笔获取
     * @param eRequest
     * @return
     */
    public Map<String,String> ztoBagAddrMarkGetmark(ExpressRequest eRequest){
        try{
            Map<String, String> dataParams = new LinkedHashMap<String, String>();
            dataParams.put("unionCode",eRequest.getLogisticsCode());//唯一标示，由调用方生成，建议使用运单号，如无运单号，也可以使用GUID/UUID
            dataParams.put("send_province",eRequest.getDeliverProvince());//发件省份
            dataParams.put("send_city",eRequest.getDeliverCity());//发件城市
            dataParams.put("send_district",eRequest.getDeliverArea());//发件区县
            dataParams.put("send_address",eRequest.getDeliverAddress());//发件详细地址
            dataParams.put("receive_province",eRequest.getConsignProvince());//收件省份
            dataParams.put("receive_city",eRequest.getConsignCity());//收件城市
            dataParams.put("receive_district",eRequest.getConsignArea());//收件区县
            dataParams.put("receive_address",eRequest.getConsignAddress());//收件详细地址

            Map<String, String> params = new LinkedHashMap<String, String>();
            params.put("company_id",ztoCompanyId);//合作商编码（在个人中心查看）
            params.put("msg_type","GETMARK");//接口服务子类(请传入：GETMARK)
            params.put("data",JSONObject.toJSONString(dataParams));

            String jsonReponse = wapperZtoClient(ztoBagMarkUrl,params,ztoCompanyId,ztoKey);
            LOGGER.info("GDB订单["+eRequest.getOrderId()+"]查询中通集包地和大头笔结果:\n"+jsonReponse);
            if(StringUtils.isNotEmpty(jsonReponse)){
                Map<String,Object> resultMap = JSONObject.parseObject(jsonReponse,Map.class);
                if(resultMap.containsKey("result")){
                    Map<String,String> bagAddrMap = JSONObject.parseObject(String.valueOf(resultMap.get("result")),Map.class);
                    return bagAddrMap;
                }
            }
        }catch (Exception e){
            LOGGER.info("GDB订单["+eRequest.getOrderId()+"]查询中通集包地和大头笔异常;异常信息:\n"+e.getMessage());
        }
        return  Collections.emptyMap();
    }



    /**
     * 对于GDB的取消订单业务方法
     * @param eRequest
     * @return
     */
    @Override
    public ExpressResponse cancelExpressOrder(ExpressRequest eRequest) {
        ExpressResponse ep = new ExpressResponse(eRequest.getExpressCompany(),eRequest.getOrderId(),eRequest.getRemark());
        ep.setStatus(SDKConstants.FAIL);
        try{
            Map<String, String> params = getZhongongCancelOrderRequest(eRequest);
            String jsonReponse = wapperZtoClient(ztoCancelOrderUrl,params,ztoCompanyId,ztoKey);
            if(StringUtils.isNotEmpty(jsonReponse)) {
                Map<String, Object> resultMap = JSONObject.parseObject(jsonReponse, Map.class);
                    if (resultMap != null && resultMap.containsKey("data")) {//API和调试返回的结果不一样
                        if (resultMap.get("data") != null && StringUtils.isNotEmpty(resultMap.get("data").toString())) {
                            List<String> dataList = JSONArray.parseArray(resultMap.get("data").toString(), String.class);
                            if (dataList != null && dataList.size() > 0) {
                                for (int i = 0; i < dataList.size(); i++) {
                                    Map<String, Object> cancelOrderMap = JSONObject.parseObject(String.valueOf(dataList.get(i)), Map.class);
                                    if (cancelOrderMap != null && cancelOrderMap.containsKey("result")) {
                                        if(cancelOrderMap.containsKey("reason") && StringUtils.isNotEmpty(String.valueOf(cancelOrderMap.get("reason")))){
                                            ep.setMsg(String.valueOf(cancelOrderMap.get("reason")));
                                        }
                                        if ((boolean) cancelOrderMap.get("result")) {//取消中通订单成功
                                            ep.setStatus(SDKConstants.SUCCESS);
                                            if(StringUtils.isEmpty(ep.getMsg())){
                                                ep.setMsg("中通快递取消订单成功;");
                                            }
                                            LOGGER.info("GDB订单[" + eRequest.getOrderId() + "]中通快递取消订单成功;"+ep.getMsg());
                                        } else {
                                            if(StringUtils.isEmpty(ep.getMsg())){
                                                ep.setMsg("中通快递取消订单失败;");
                                            }
                                            LOGGER.info("GDB订单[" + eRequest.getOrderId() + "]中通快递取消订单失败;"+ep.getMsg());
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }catch (Exception e){
            LOGGER.error("GDB订单["+eRequest.getOrderId()+"]中通快递取消订单异常;异常原因:"+e.getMessage());
        }
        return ep;
    }


    /**
     * @desc 利用中通的SDK去请求
     * @param ztoUrl
     * @param params
     * @param companyId
     * @param key
     * @return
     */
    public   String wapperZtoClient(String ztoUrl,Map<String, String> params,String companyId,String key){
        ZopClient client = new ZopClient(companyId,key);
        ZopPublicRequest request = new ZopPublicRequest();
        request.addParam(params);
        request.setUrl(ztoUrl);
        try {
            String reponseStr = client.execute(request);
            LOGGER.info("post to ztoClient reponse:"+reponseStr);
            return reponseStr;
        } catch (IOException e) {
           LOGGER.error("post to ztoClient error:"+e.getMessage());
        }
        return null;
    }

    /**
     *  根据请求参数包装成中通的下单请求对象Map
     * @param eRequest
     */
    public Map<String, String> getZhongongSearchByCodeRequest(ExpressRequest eRequest){
        Map<String,String> params = new LinkedHashMap<String, String>();
        params.put("company_id",ztoCompanyId);//合作商编码
        params.put("msg_type","SEARCHBYCODE");//接口服务子类(请传入SEARCHBYCODE)

        Map<String, Object> dataParams = new LinkedHashMap<String, Object>();
        JSONArray jArray = new JSONArray();
        jArray.add(eRequest.getLogisticsCode());
        dataParams.put("orderCode",jArray);//订单号,可以查多个用逗号分开
        dataParams.put("sendId",String.valueOf(eRequest.getSellerId()));//用户ID
        dataParams.put("partnerId",eRequest.getOrderId());//合作商单号

        params.put("data",JSONObject.toJSONString(dataParams));//业务数据
        return params;
    }

    /**
     *  根据请求参数包装成中通的[预约寄付]下单请求对象Map
     * @param eRequest
     */
    public Map<String, String> getZTOOrderRequest(ExpressRequest eRequest){
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("partnerCode",eRequest.getOrderId());//GDB订单号，为调用方生成，可以传订单号、主键、UUID等
        params.put("type",1);//订单类型 1 普通订单(线上),2线下联系订单 默认1

        Map<String, Object> senderParams = new LinkedHashMap<String, Object>();//发件人信息
        senderParams.put("id",String.valueOf(eRequest.getSellerId()));//发件人在GDB平台中的ID号
        senderParams.put("name",eRequest.getDeliverLinkMan());//发件人姓名
        senderParams.put("company",eRequest.getDeliverCompany());//发件公司名
        senderParams.put("mobile",eRequest.getDeliverLinkTel());//发件人手机号码
        senderParams.put("prov",eRequest.getDeliverProvince());//发件人省
        senderParams.put("city",eRequest.getDeliverCity());//发件人市
        senderParams.put("county",eRequest.getDeliverArea());//发件人区
        senderParams.put("address",eRequest.getDeliverAddress());//发件人详细地址
        params.put("sender",senderParams);

        Map<String, Object> receiverParams = new LinkedHashMap<String, Object>();//收件人信息
        receiverParams.put("name",eRequest.getConsignConsignee());//收件人姓名
        receiverParams.put("company",eRequest.getConsignCompany());//收件公司名
        receiverParams.put("mobile",eRequest.getConsignMobile());//收件人手机号码
        receiverParams.put("prov",eRequest.getConsignProvince());//收件人省
        receiverParams.put("city",eRequest.getConsignCity());//收件人市
        receiverParams.put("county",eRequest.getConsignArea());//收件人区
        receiverParams.put("address",eRequest.getConsignAddress());//收件人详细地址
        params.put("receiver",receiverParams);
        JSONArray jArray = new JSONArray();
        jArray.add(JSONObject.parse("{\"name\": \"工业品电子元器件\"}"));
        params.put("items",jArray);//货品信息
        params.put("quantity",eRequest.getParcelQuantity());//订单包裹内货物总数量
        params.put("remark",eRequest.getRemark());//订单备注

        Map<String, String> ztoBodyParams = new HashMap<String, String>();//收件人信息
        ztoBodyParams.put("orderGroup",JSONObject.toJSONString(params));
        return ztoBodyParams;
    }

    /**
     *  根据请求参数包装成中通的取消订单请求对象Map
     * @param eRequest
     */
    public Map<String, String> getZhongongCancelOrderRequest(ExpressRequest eRequest){
        Map<String,String> params = new LinkedHashMap<String, String>();
        params.put("company_id",ztoCompanyId);//合作商编码
        params.put("msg_type","UPDATE");//接口服务子类(UPDATE)

        Map<String, Object> dataItemParams = new LinkedHashMap<String, Object>();
        dataItemParams.put("orderCode",eRequest.getLogisticsCode());//订单号,可以查多个用逗号分开
        dataItemParams.put("fieldName","status");//用户ID
        dataItemParams.put("fieldValue","cancel");//合作商单号
        dataItemParams.put("reason",eRequest.getRemark());//更新(取消)原因
        JSONArray jArray = new JSONArray();
        jArray.add(dataItemParams);
        params.put("data",JSONObject.toJSONString(jArray));//业务数据
        return params;
    }


      /*
                //电子面单-获取单号(无密匙) [无法就近下单]
                Map<String, String> params = getZTOElectronicSurfacerderRequest(eRequest);
                String jsonReponse = wapperZtoClient(ztoEsOrderUrl,params,ztoCompanyId,ztoKey);
                Map<String, Object> resultParams = (Map<String, Object>) JSONObject.parse(jsonReponse);
                if(resultParams!=null && resultParams.containsKey("data")){
                    ZtoEsDataReponse ztoEsDataReponse = JSONObject.parseObject(resultParams.get("data").toString(),ZtoEsDataReponse.class);
                    if(ztoEsDataReponse!=null && StringUtils.isNotEmpty(ztoEsDataReponse.getBillCode())){
                        ep.setStatus(SDKConstants.SUCCESS);
                        ep.setMsg(ztoEsDataReponse.getMessage());
                        ep.setExpressOrderId(ztoEsDataReponse.getBillCode());
                        LOGGER.info("GDB订单["+eRequest.getOrderId()+"]中通快递下单成功;运单号（orderCode）"+ztoEsDataReponse.getBillCode());
                    }else{
                        LOGGER.error("GDB订单["+eRequest.getOrderId()+"]中通快递下单后没有返回信息!");
                    }
                }
      */


    /**
     *  根据请求参数包装成中通的[电子面单]下单请求对象Map
     * @param eRequest
     */
    public Map<String, String> getZTOElectronicSurfacerderRequest(ExpressRequest eRequest){
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("partner",ztoPartner);//商家ID，又称电子面单账号、电子面单账户、客户ID(正式环境由中通合作网点提供，测试环境使用test)
        params.put("id",eRequest.getOrderId());//订单号由合作商平台产生，具有平台唯一性。(测试的id一律使用xfs101100111011)
        params.put("typeid","1");//1:为电子面单类 （其他类型具体可调用获取类型接口）

        Map<String, Object> senderParams = new LinkedHashMap<String, Object>();//发件人信息
        senderParams.put("id",String.valueOf(eRequest.getSellerId()));//发件人在GDB平台中的ID号
        senderParams.put("name",eRequest.getDeliverLinkMan());//发件人姓名
        senderParams.put("company",eRequest.getDeliverCompany());//发件公司名
        senderParams.put("mobile",eRequest.getDeliverLinkTel());//发件人手机号码
        //发件人所在城市，必须逐级指定，用英文半角逗号分隔，目前至少需要指定到区县级，如能往下精确更好，如“上海市,上海市,青浦区,华
        senderParams.put("city",eRequest.getDeliverProvince()+","+eRequest.getDeliverCity()+","+eRequest.getDeliverArea());
        senderParams.put("address",eRequest.getDeliverAddress());//发件人详细地址
        params.put("sender",senderParams);

        Map<String, Object> receiverParams = new LinkedHashMap<String, Object>();//收件人信息
        receiverParams.put("name",eRequest.getConsignConsignee());//收件人姓名
        receiverParams.put("company",eRequest.getConsignCompany());//收件公司名
        receiverParams.put("mobile",eRequest.getConsignMobile());//收件人手机号码
        //收件人所在城市，逐级指定，用英文半角逗号分隔
        receiverParams.put("city",eRequest.getConsignProvince()+","+eRequest.getConsignCity()+","+eRequest.getConsignArea() );
        receiverParams.put("address",eRequest.getConsignAddress());//收件人详细地址
        params.put("receiver",receiverParams);
        params.put("quantity",String.valueOf(eRequest.getParcelQuantity()));//订单包裹内货物总数量
        params.put("remark",eRequest.getRemark());//订单备注

        Map<String, String> ztoBodyParams = new LinkedHashMap<String, String>();//收件人信息
        ztoBodyParams.put("company_id",ztoCompanyId);//合作商编码（在个人中心查看）
        ztoBodyParams.put("msg_type","submitAgent");//接口服务子类(请传入：submitAgent)
        ztoBodyParams.put("data",JSONObject.toJSONString(params));
        return ztoBodyParams;
    }


    /**
     * 包装中通 预寄下单-创建订单 接口返回对象
     */
    static class ZtoReponse{
        private String result; //{"orderCode": "180108000002008102"} 中通生成的订单号
        private String message;//消息
        private String statusCode;//状态码
        private boolean status;//是否成功
        private String  orderCode;//成功时，中通生成的订单号

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }



        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

    /**
     * 包装中通 电子面单-创建订单 接口返回对象
     */
    static class ZtoEsDataReponse{
        private String orderId; //订单ID
        private String billCode;//单号
        private boolean update;//是否更新
        private String siteCode;//网点编号
        private String  siteName;//网点名称
        private String  message;//提示信息

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getBillCode() {
            return billCode;
        }

        public void setBillCode(String billCode) {
            this.billCode = billCode;
        }

        public boolean isUpdate() {
            return update;
        }

        public void setUpdate(boolean update) {
            this.update = update;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }



    public static void main(String[] args) throws NoSuchAlgorithmException {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put("partnerCode","201808131451Zyk");//GDB订单号，为调用方生成，可以传订单号、主键、UUID等
        params.put("type",1);//订单类型 1 普通订单(线上),2线下联系订单 默认1
        Map<String, Object> senderParams = new LinkedHashMap<String, Object>();//发件人信息
        senderParams.put("id","111256");//发件人在GDB平台中的ID号
        senderParams.put("name","曾妮");//发件人姓名
        senderParams.put("company","江苏工电宝信息科技有限公司");//发件公司名
        senderParams.put("mobile","xxxxx");//发件人手机号码
        senderParams.put("prov","江苏省");//发件人省
        senderParams.put("city","苏州市");//发件人市
        senderParams.put("county","吴中区");//发件人区
        senderParams.put("address","吴中大道亿达软件新城B2栋306室");//发件人详细地址
        params.put("sender",senderParams);
        Map<String, Object> receiverParams = new LinkedHashMap<String, Object>();//收件人信息
        receiverParams.put("name","李宁");//收件人姓名
        receiverParams.put("company","");//收件公司名
        receiverParams.put("mobile","xxxxx");//收件人手机号码
        receiverParams.put("prov","广东省");//收件人省
        receiverParams.put("city","深圳市");//收件人市
        receiverParams.put("county","龙岗区");//收件人区
        receiverParams.put("address","坂田街道 上雪路4号 熙俊大厦");//收件人详细地址
        params.put("receiver",receiverParams);
        JSONArray jArray = new JSONArray();
        jArray.add(JSONObject.parse("{\"name\": \"工业品电子元器件\"}"));
        params.put("items",jArray);//货品信息
        params.put("quantity","1");//订单包裹内货物总数量
        params.put("remark","今天要发，尽快取件!");//订单备注
        Map<String, String> ztoBodyParams = new HashMap<String, String>();//收件人信息
        ztoBodyParams.put("orderGroup",JSONObject.toJSONString(params));
        //String jsonReponse = wapperZtoClient("http://japi.zto.cn/OpenOrderCreate",ztoBodyParams,"f96b0c4ecb4340f089f00340774c5175","97c578968516");
        //System.out.println(jsonReponse);
    }








}
