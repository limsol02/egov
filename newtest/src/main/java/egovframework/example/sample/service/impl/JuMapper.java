package egovframework.example.sample.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.example.sample.service.Competition;
import egovframework.example.sample.service.EvaluationItems;
import egovframework.example.sample.service.Score;
import egovframework.example.sample.service.Sheet;

@Mapper("JuMapper")
public interface JuMapper {

	void addEvaluation(String item)throws Exception;

	void addCompetition(String title)throws Exception;

	List<Map<String, Competition>> getCompetitionList();
	Integer searchCompetitionInTitle(String title);

	List<Map<String, EvaluationItems>> getEvaluationItemsList();

	void addScore(Score score);

	void addSheet(List<Sheet> sheets);

	List<String> getEitemsBycomIdInSheet(int competition_id);

	Integer getSheetidBycomIdandei(@Param("competition_id")int competition_id,@Param("evaluation_items") String evaluation_items);
	
	
}
