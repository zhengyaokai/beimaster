package bmatser.util;

import java.io.IOException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.codehaus.jackson.map.ObjectMapper;
/**
 * 实体类和JSON对象之间相互转化（依赖包jackson-all-1.7.6.jar、jsoup-1.5.2.jar）
 * @author wck
 *
 */
public class JSONUtil {
    /**
     * 将json转化为实体POJO
     * @param jsonStr
     * @param obj
     * @return
     */
    public static<T> Object JSONToObj(String jsonStr,Class<T> obj) {
        T t = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            t = objectMapper.readValue(jsonStr,
                    obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
     
    /**
     * 将实体POJO转化为JSON
     * @param obj
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static<T> JSONObject objectToJson(T obj) throws JSONException {
        ObjectMapper mapper = new ObjectMapper();  
        
        // Convert object to JSON string  
        JSONObject object = JSONObject.fromObject(obj);
        return object;
    }
     
     
    public static void main(String[] args) throws JSONException, IOException {
        JSONObject obj = null;
        obj = new JSONObject();
        obj.put("name", "213");
        obj.put("age", 27);
        
    }
}