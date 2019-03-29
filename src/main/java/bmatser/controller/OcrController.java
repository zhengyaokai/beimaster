package bmatser.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import bmatser.util.JSONUtil;
import bmatser.util.ResponseMsg;
import bmatser.util.TencentOcrUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;


@Controller
@RequestMapping("ocr")
@Api(value="ocr", description="OCR图片文字识别")
public class OcrController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OcrController.class);

	@RequestMapping(value="/general",produces="text/html; charset=UTF-8")
	@ResponseBody
	@ApiOperation(value = "OCR图片文字识别")
	public String generalImgOcr(@ApiParam(value="图片地址") @RequestParam("uploadfile") MultipartFile file,
								HttpServletRequest request, HttpServletResponse response) {
		ResponseMsg msg = new ResponseMsg();
		try{
			CommonsMultipartFile cf = (CommonsMultipartFile) file;
			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
			File f = fi.getStoreLocation();
			String ocrStr = TencentOcrUtil.generalImgOcr(f);
			if(StringUtils.isNotEmpty(ocrStr)){
				msg.setCode(0);
				msg.setData(ocrStr);
			}else{
				msg.setCode(-1);
				msg.setMsg("未能识别!");
			}
		}catch (Exception e){
			msg.setCode(-1);
			msg.setError(e);
			LOGGER.error("转换图片文件参数异常!\n"+e.getMessage());
		}
		//response.addHeader("Access-Control-Allow-Origin","*");
		return JSONUtil.objectToJson(msg).toString();
	}
}
