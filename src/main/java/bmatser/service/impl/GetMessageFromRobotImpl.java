package bmatser.service.impl;

import bmatser.service.GetMessageFromRobot;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class GetMessageFromRobotImpl implements GetMessageFromRobot {
	
	//机器人唯一ID,APIKEY中式我的
    private static final String  APIKEY = "04d8158d840649448f115b787216f855";

    public Map<String, Object> getMessageFromRobot(String question) {
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            if(null==question){
                map.put("code","-1");
                map.put("answer",null);

                return  map ;
            }
            
            //询问的问题
            String INFO = URLEncoder.encode(question,"utf-8");

            String url = "http://www.tuling123.com/openapi/api?key="+APIKEY+"&info="+INFO;

            URL getUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();

            connection.connect();

            //取得输入流,并使用reader读取
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));

            StringBuffer sb = new StringBuffer();

            String line = "";

            while ((line=reader.readLine())!= null){

                sb.append(line);
            }
            map.put("answer",sb);
            map.put("code","1");
            reader.close();
            //断开连接
            connection.disconnect();

            System.out.println(sb);

        }catch (Exception e){
            map.put("code","-1");
            map.put("answer",null);
        }

        return map;
    }
}
