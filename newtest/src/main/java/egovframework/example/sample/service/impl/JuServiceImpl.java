package egovframework.example.sample.service.impl;

import javax.annotation.Resource;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import egovframework.example.sample.service.JuService;


@Service("juService")
public class JuServiceImpl extends EgovAbstractServiceImpl implements JuService {

	

	@Resource(name = "JuMapper")
	private JuMapper dao;

	@Override
	public void addEvaluation(String item) throws Exception {
		if(item.length()>225) 
		{
			throw new Exception("글자수가 너무 많습니다.");
		}
		dao.addEvaluation(item);
		
	}
	
	

}
