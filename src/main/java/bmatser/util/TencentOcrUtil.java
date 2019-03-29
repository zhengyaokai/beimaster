package bmatser.util;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.image.ImageClient;
import com.qcloud.image.request.GeneralOcrRequest;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TencentOcrUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(TencentOcrUtil.class);

	private static final String appId ="1257321064";
	private static final String secretId ="AKIDJTP7buNsVt3xbzR8qIVSAcT8YjeK8vrX";
	private static final String secretKey ="ZtxlKelfLp66sOedMp2zg7IDsHQM2TeZ";


	public static String generalImgOcr(File file) {
		try {
			ImageClient imageClient = new ImageClient(appId, secretId, secretKey);
			System.out.println("================================================================");
			GeneralOcrRequest gOcrRequest = new GeneralOcrRequest("bucketName", file);
			String ret = imageClient.generalOcr(gOcrRequest);
			System.out.println("ocrGeneral:" + ret);
			System.out.println("================================================================");
			if(StringUtils.isNotEmpty(ret)){
				Map<String,Object> ocrResultMap = JSONObject.parseObject(ret, Map.class);
				if( "0".equals(String.valueOf(ocrResultMap.get("code"))) ){
					if(ocrResultMap.get("data")!=null && StringUtils.isNotEmpty(ocrResultMap.get("data").toString())){
						Map<String,Object> dataResultMap = JSONObject.parseObject(ocrResultMap.get("data").toString(),Map.class);
						if(dataResultMap.get("items")!=null && StringUtils.isNotEmpty(dataResultMap.get("items").toString())) {
							List<TxOcrDataItem> dataResultList = JSONObject.parseArray(dataResultMap.get("items").toString(),TxOcrDataItem.class);
							int previousPositionY = -1;
							StringBuffer sbuffer = new StringBuffer();
							List<String> textList = new ArrayList<String>();
							for(int i=0; i<dataResultList.size(); i++){
								TxOcrDataItem txItem = dataResultList.get(i);
								if(txItem.getItemcoord()!=null && txItem.getItemcoord().getY()!=0){
									int itemY = txItem.getItemcoord().getY();
									if(itemY!=previousPositionY && previousPositionY!=-1){
										textList.add(sbuffer.toString());
										sbuffer.setLength(0);
									}
									sbuffer.append(txItem.getItemstring());
									previousPositionY = itemY;
									if(i == dataResultList.size()-1){
										textList.add(sbuffer.toString());
									}
								}
							}
							sbuffer.setLength(0);
							for(String lineTxt : textList){
								sbuffer.append(lineTxt).append("\n");
							}
							LOGGER.info("Tencent云OCR识别图片（名称:"+file.getName()+"）成功;识别结果:\n"+sbuffer.toString());
							return sbuffer.toString();
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Tencent云OCR识别图片（名称:"+file.getName()+"）失败;异常原因："+e.getMessage());
		}
		return "";
	}

	 static class TxOcrDataItem{
		private ItemCoord itemcoord;
		private String itemstring;

		public ItemCoord getItemcoord() {
			return itemcoord;
		}

		public void setItemcoord(ItemCoord itemcoord) {
			this.itemcoord = itemcoord;
		}

		public String getItemstring() {
			return itemstring;
		}

		public void setItemstring(String itemstring) {
			this.itemstring = itemstring;
		}
	}

	static class ItemCoord {
		private int x;
		private int y;
		private int width;
		private int height;

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}

	public static void main(String[] args) {

	}
}
