package bmatser.dao;

import org.apache.ibatis.annotations.Param;

public interface QuestionnaireSurveyMapper {

	int selectDealerBean(@Param("dealerId") String dealerId);

	void insertDealerBean(@Param("dealerId") String dealerId);

	void updateBeanToDealer(@Param("dealerId") String dealerId);

}
