package egovframework.example.sample.service;

import java.util.List;
import java.util.Map;

public interface JuService {

	int cheekAdmin(String role);
	void addEvaluation(String item)throws Exception;
	void addCompetition(String title)throws Exception;
	List<Map<String, Competition>> getCompetitionList();
	List<Map<String, EvaluationItems>> getEvaluationItemsList();
	void addSheet(int competitionId, List<Integer> evaluationIds);
	List<String> getEitemsBycomIdInSheet(int competition_id);
	int getSheetidBycomIdandei(int competition_id, String ei);
	void addScore(List<Integer> score, List<Integer> sheet, int participant_id,int judge_id);
	List<Competition> competitionByJudgeID(int judge_id);
}
