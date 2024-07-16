package egovframework.example.sample.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.example.sample.service.Competition;
import egovframework.example.sample.service.EvaluationItems;
import egovframework.example.sample.service.JuService;


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

	@Override
	public List<Map<String, EvaluationItems>> getEvaluationItemsList() {
		return dao.getEvaluationItemsList();
	}

}
