package egovframework.example.sample.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface SolService {
	int insFile(FileStorage ins, MultipartFile[] files,Participant insPart) throws Exception;
	int insPart(Participant ins) throws Exception;
	List<Competition> competitionList() throws Exception;
}
