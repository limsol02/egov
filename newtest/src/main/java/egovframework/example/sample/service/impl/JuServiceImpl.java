package egovframework.example.sample.service.impl;

import javax.annotation.Resource;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import egovframework.example.sample.service.JuService;


@Service("juService")
public class JuServiceImpl extends EgovAbstractServiceImpl implements JuService {

	

	@Resource(name = "juMapper")
	private JuMapper dao;
	
	

}
