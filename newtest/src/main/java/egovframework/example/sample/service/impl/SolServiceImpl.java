package egovframework.example.sample.service.impl;

import java.io.File;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.sample.service.FileStorage;
import egovframework.example.sample.service.SolService;

@Service("solService")
public class SolServiceImpl extends EgovAbstractServiceImpl implements SolService {

	@Value("${file.upload.path}")
	private String uploadPath;

	@Resource(name = "solMapper")
	private SolMapper dao;

	@Override
	public int insFile(FileStorage ins, MultipartFile[] files) throws Exception {
		int result = 0;

		return result;
	}

}
