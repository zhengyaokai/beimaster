package bmatser.util;
 
import com.alibaba.fastjson.JSON;
//import lombok.Data;
 
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


 
/**
 * @author felx
 * @date 2018/08/20
 */
//@Data
public class JsonResult implements Serializable{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 8163076179036323521L;
	private int code;   //返回码 非0即失败
    private String msg; //消息提示
    private Object data; //返回的数据
 
    public JsonResult(){};
 
    public JsonResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
 
    public static String success() {
        return success(new HashMap<String,Object>(0));
    }
    public static String success(Map<String, Object> data) {
        return JSON.toJSONString(new JsonResult(0, "解析成功", data));
    }
 
    public static String failed() {
        return failed("解析失败");
    }
    public static String failed(String msg) {
        return failed(-1, msg);
    }
    public static String failed(int code, String msg) {
        return JSON.toJSONString(new JsonResult(code, msg,null));
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
 
}