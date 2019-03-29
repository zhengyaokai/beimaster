package bmatser.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bmatser.dao.QuestionnaireSurveyMapper;
import bmatser.exception.GdbmroException;
import bmatser.pageModel.QuestionnaireSurvey;
import bmatser.service.QuestionnaireSurveyI;
@Service("questionnaireSurveyService")
public class QuestionnaireSurveyImpl implements QuestionnaireSurveyI{
	@Autowired
	private QuestionnaireSurveyMapper questionnaireSurveyDao;

	@Override
	public void updateDealerBean(QuestionnaireSurvey questionnaireSurvey) {
		int count = questionnaireSurveyDao.selectDealerBean(questionnaireSurvey.getDealerId());
		if(count>0){
			throw new GdbmroException(32011,"您已参加过问卷调查工币活动!");
		}
		//向dealer_bean表插入一条数据
		questionnaireSurveyDao.insertDealerBean(questionnaireSurvey.getDealerId());
		//向dealer表增加1000工币
		questionnaireSurveyDao.updateBeanToDealer(questionnaireSurvey.getDealerId());

	}

}
