package egovframework.example.sample.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

public interface SolService {
	int insFile(FileStorage ins, MultipartFile[] files,Participant insPart) throws Exception;
	int insPart(Participant ins) throws Exception;
	List<Competition> competitionList() throws Exception;
	List<Participant> partList(int competition_id) throws Exception;
	Competition comTitleBycomId(int paricipant_id) throws Exception;
	int delPart(@Param("participant_id")int participant_id) throws Exception;
	int uptURL(Participant upt) throws Exception;
}
