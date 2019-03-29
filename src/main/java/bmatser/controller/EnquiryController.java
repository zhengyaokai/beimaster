package bmatser.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import bmatser.dao.EnquiryPriceGoodsMapper;
import bmatser.dao.EnquiryPriceMapper;
import bmatser.model.EnquiryPrice;
import bmatser.model.EnquiryPriceGoods;
import bmatser.pageModel.EnquiryPricePage;
import bmatser.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/enquiry")
@Api(value="enquiry", description="询价单")
public class EnquiryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnquiryController.class);

	private static final String epTemplateFileName = "enquiry_price_template.xls";

	private static final String templateDownLoadName = "工电宝询价模板.xls";


	@Autowired
	private EnquiryPriceMapper enquiryPriceDao;
	@Autowired
	private EnquiryPriceGoodsMapper enquiryPriceGoodsDao;

	/**
	 * 新增询价单
	 * @return
	 */
	@ApiOperation(value = "生成询价单")
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	@Transactional(rollbackFor=Exception.class)
	public ResponseMsg  addEnquiryPriceAndGoods(@RequestBody EnquiryPricePage enquiryEntityPage, HttpServletRequest request) throws Exception{
		ResponseMsg msg = new ResponseMsg();
		msg.setCode(-1);
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(loginInfo==null && enquiryEntityPage.getDealerId()==null){
			msg.setCode(403);
			return msg;
		}
		try {
			EnquiryPrice ePrice = new EnquiryPrice();
			ePrice.setName(enquiryEntityPage.getName());
			if(StringUtils.isNotEmpty(enquiryEntityPage.getEndDate())){
				ePrice.setEndDate( new SimpleDateFormat("yyyy-MM-dd").parse(enquiryEntityPage.getEndDate()));
			}
			ePrice.setRemark(enquiryEntityPage.getRemark());
			if(loginInfo!=null && StringUtils.isNotEmpty(loginInfo.getDealerId())){
				ePrice.setDealerId(Integer.valueOf(loginInfo.getDealerId()));
			}else if(enquiryEntityPage.getDealerId()!=null && enquiryEntityPage.getDealerId()!=0){
				ePrice.setDealerId(enquiryEntityPage.getDealerId());
			}
			ePrice.setCreateTime(new Date());
			ePrice.setStatus(Status.ENQUIRYPREPARE.getValue());
			enquiryPriceDao.insertSelective(ePrice);
			if(ePrice.getId()!=null){
				List<EnquiryPriceGoods> gList =  enquiryEntityPage.getGoodsList();
				enquiryPriceGoodsDao.insertGoodsList(gList,ePrice.getId());
				msg.setCode(0);
				return msg;
			}
		} catch (Exception e) {
			msg.setError(e);
			LOGGER.error("客户("+enquiryEntityPage.getDealerId()+")生成询价单异常:\n"+e.getMessage());
			throw e;
		}
		return msg;
	}

	/**
	 * 下载报价单模板
	 * @param res
	 * @param req
	 */
	@ApiOperation(value = "下载报价单模板")
	@RequestMapping(value="/downloadEpTemplate",method=RequestMethod.GET)
	public void downloadEnquiryPriceTemplate(HttpServletResponse res, HttpServletRequest req){
		try {
			String filePath = req.getSession().getServletContext().getRealPath("/")+File.separator+"dowload"+ File.separator+epTemplateFileName;
			download(res, req, filePath);
		} catch (Exception e) {
			LOGGER.error("下载报价单模板异常:\n"+e.getMessage());
		}
	}



	private void download(HttpServletResponse response,HttpServletRequest request,String filePath) throws IOException {
		response.setContentType("application/octet-stream");
		request.setCharacterEncoding("UTF-8");
		File file = new File(filePath);
		response.setHeader("Content-disposition", "attachment; filename=" + new String(templateDownLoadName.getBytes(), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		InputStream is = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(is);
		//输出流
		OutputStream os = response.getOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(os);
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		//关闭流
		is.close();
		bis.close();
		bos.close();
		os.close();
	}

}
