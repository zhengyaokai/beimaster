package bmatser.service.impl.alibaba;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import bmatser.util.alibaba.CommonUtil;



/**
 * api调用的服务类
 */
public class ApiCallServiceImpl {

    /**
     * 调用api测试
     * @param urlHead 请求的url到openapi的部分，如http://gw.open.1688.com/openapi/
     * @param urlPath protocol/version/namespace/name/appKey
     * @param appSecretKey 测试的app密钥，如果为空表示不需要签名
     * @param params api请求参数map。如果api需要用户授权访问，那么必须完成授权流程，params中必须包含access_token参数
     * @return json格式的调用结果
     */
    public static String callApiTest(String urlHead, String urlPath, String appSecretKey, Map<String, String> params){
    	final HttpClient httpClient = HttpClients.createDefault();
    	final HttpPost method = new HttpPost(urlHead + urlPath);
        method.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if(params != null){
            for (Map.Entry<String, String> entry : params.entrySet()) {
            	formparams.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
            }
        }
        if(appSecretKey != null){
        	formparams.add(new BasicNameValuePair("_aop_signature",CommonUtil.signatureWithParamsAndUrlPath(urlPath, params, appSecretKey)));
        }
        String response = "";
        try{
        	HttpResponse res = httpClient.execute(method);
        	StatusLine saLine = res.getStatusLine();
            int status = saLine.getStatusCode();
            if(status >= 300 || status < 200){
                throw new RuntimeException("invoke api failed, urlPath:" + urlPath
                        + " status:" + status + " response:" + method.getEntity().toString());
            }
            response = CommonUtil.parserResponse(method);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            method.releaseConnection();
        }
        return response;
    }
    
}