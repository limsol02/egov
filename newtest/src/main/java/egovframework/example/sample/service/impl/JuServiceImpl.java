package egovframework.example.sample.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.example.sample.service.Competition;
import egovframework.example.sample.service.EvaluationItems;
import egovframework.example.sample.service.JuService;
import egovframework.example.sample.service.Score;
import egovframework.example.sample.service.Sheet;


@Service("juService")
public class JuServiceImpl extends EgovAbstractServiceImpl implements JuService {

	

	@Resource(name = "JuMapper")
	private JuMapper dao;
	
	
	//평가항목추가
	@Override
	public void addEvaluation(String item) throws Exception {
		if(item.length()>225) 
		{
			throw new Exception("글자수가 너무 많습니다.");
		}
		dao.addEvaluation(item);
	}
	
	//관리자권한체크
	@Override
	public int cheekAdmin(String role){
		if (!"admin".equals(role)) {
            return 1; //  null이거나 admin 이 아닌 경우 top 페이지로 이동합니다.
        }
		return 0;
	}
	// 공모전 추가
	@Override
	public void addCompetition(String title) throws Exception {
		if(title.length()>225) 
		{
			throw new Exception("글자수가 너무 많습니다.");
		}
		//나중에 제목중복 예외도 해야함.
		dao.addCompetition(title);
	}
	
	//공모전 리스트 체크
	@Override
	public List<Map<String, Competition>> getCompetitionList() {
		return dao.getCompetitionList();
	}
	//평가항목 리스트 가져오기
	@Override
	public List<Map<String, EvaluationItems>> getEvaluationItemsList() {
		return dao.getEvaluationItemsList();
	}
	//점수 저장
	@Override
	public void addScore(List<Integer> score, List<Integer> sheet, int participant_id,int judge_id) {
		for (int i = 0; i < score.size(); i++) {
    		Score s = new Score();
    		int a = score.get(i);
    		int b = sheet.get(i);
    		s.setScore(a);
    		s.setSheet_id(b);
    		s.setParticipant_id(participant_id);
    		s.setJudge_id(judge_id);
    		dao.addScore(s);
		}
	}
	
	//평가지 저장
	@Override
	public void addSheet(int competitionId, List<Integer> evaluationIds) {
		List<Sheet> sheets = new ArrayList<>();
		for (Integer id : evaluationIds) {
            Sheet sheet = new Sheet();
            sheet.setCompetition_id(competitionId);
            sheet.setEvaluation_id(id);
            sheets.add(sheet);
        }
        dao.addSheet(sheets);
    }
	//공모전 아이디로 평가항목 가져오기
	@Override
	public List<String> getEitemsBycomIdInSheet(int competition_id) {
		return dao.getEitemsBycomIdInSheet(competition_id);
	}

	@Override
	public int getSheetidBycomIdandei(int competition_id, String evaluationItems) {
		return dao.getSheetidBycomIdandei(competition_id,evaluationItems);
	}

	@Override
	public List<Competition> competitionByJudgeID(int judge_id) {
		return dao.competitionByJudgeID(judge_id);
		
	}


}
