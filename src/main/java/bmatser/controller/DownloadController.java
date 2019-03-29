package bmatser.controller;

import javax.servlet.http.HttpServletRequest;

import org.igfay.jfig.JFig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import bmatser.service.DownloadI;
import bmatser.util.LoginInfo;
import bmatser.util.MemberTools;
@Controller
@RequestMapping("download")
@Api(value="download", description="下载")
public class DownloadController {
	
	@Autowired
	private DownloadI downloadService;
	private String urlUpload = JFig.getInstance().getSection("system_options").get("URL_UPLOAD");

	@RequestMapping(value="/authorize", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "授权委托")
	public ModelAndView authorizeBydealerId(HttpServletRequest request) {
		/*String referer = request.getHeader("Referer");*/
		ModelAndView mav = null;
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return new ModelAndView("redirect:"+urlUpload+"/login");
		}
		try {
			mav = new ModelAndView("dowload/authorize");
			mav.addObject("map", downloadService.authorizeBydealerId(loginInfo.getDealerId()));
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("redirect:"+urlUpload+"/login");
		}
		
	}
	
	@RequestMapping(value="/application", method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "开票申请")
	public ModelAndView applicationBydealerId(HttpServletRequest request) {
		/*String referer = request.getHeader("Referer");*/
		ModelAndView mav = null;
		LoginInfo loginInfo = MemberTools.isSaasLogin(request);
		if(null == loginInfo || null == loginInfo.getDealerId()){
			return new ModelAndView("redirect:"+urlUpload+"/login");
		}
		try {
			mav = new ModelAndView("dowload/application");
			mav.addObject("map", downloadService.authorizeBydealerId(loginInfo.getDealerId()));
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("redirect:"+urlUpload+"/login");
		}
		
	}
}
