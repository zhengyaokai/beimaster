package bmatser.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

//import bmatser.service.MongoServiceI;
import bmatser.pageModel.PageMode;
import bmatser.pageModel.QuestionnaireSurvey;
import bmatser.service.QuestionnaireSurveyI;
import bmatser.util.ResponseMsg;

@Controller
@RequestMapping("questionnaireSurvey")
@Api(value="questionnaireSurvey", description="问卷调查送工币")
public class QuestionnaireSurveyController {
	@Autowired
	private QuestionnaireSurveyI questionnaireSurveyService; 
//	@Autowired
//	private MongoServiceI mongoService;
	/**
	 * 问卷调查送工币
	 * @return
	 */
	@RequestMapping(value={"/giveDealerBean"}, method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "问卷调查送工币")
	public ResponseMsg giveDealerBean(HttpServletRequest request,@ModelAttribute QuestionnaireSurvey questionnaireSurvey){
		ResponseMsg msg = new ResponseMsg();
		PageMode pageMode = new PageMode(request);
		try {
			String dealerId = pageMode.getSaasLogin().getDealerId();
			questionnaireSurvey.setDealerId(dealerId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			//+8小时
			cal.add(Calendar.HOUR_OF_DAY, 8);
			Timestamp t = Timestamp.valueOf(sdf.format(cal.getTime()));
			questionnaireSurvey.setTime(t);
			questionnaireSurveyService.updateDealerBean(questionnaireSurvey);
			//保存问卷调查数据到mongoDB
//			mongoService.save(questionnaireSurvey);
			msg.setCode(0);
		}catch (Exception e) {
			msg.setError(e);
		}
		return msg;
	}
	
}
