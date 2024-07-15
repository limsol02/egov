package egovframework.example.sample.service;

import org.springframework.web.multipart.MultipartFile;

public interface SolService {
	int insFile(FileStorage ins, MultipartFile[] files) throws Exception;

}
