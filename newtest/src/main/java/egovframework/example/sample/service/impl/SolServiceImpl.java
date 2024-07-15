package egovframework.example.sample.service.impl;

import java.io.File;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.sample.service.FileStorage;
import egovframework.example.sample.service.Participant;
import egovframework.example.sample.service.SolService;

@Service("solService")
public class SolServiceImpl extends EgovAbstractServiceImpl implements SolService {

	@Value("${file.upload.path}")
	private String uploadPath;

	@Resource(name = "solMapper")
	private SolMapper dao;
	
	@Override
	public int insPart(Participant ins) throws Exception {
		return 0;
	}

	@Override
	public int insFile(FileStorage ins, MultipartFile[] files, Participant insPart) throws Exception {
	    int result = 0;

	    try {
	        // 참가자 DB 등록
	        int partResult = dao.insPart(insPart);
	        if (partResult > 0) {
	            System.out.println("참여 등록 성공");

	            // 방금 삽입된 Participant의 ID 가져오기
	            int participantId = insPart.getParticipant_id();

	            // 파일 등록 (파일이 있을 경우에만 처리)
	            if (files != null && files.length > 0) {
	                for (MultipartFile file : files) {
	                    // 파일 원래이름 가져오기
	                    String fileName = file.getOriginalFilename();

	                    // Properties 경로 참조
	                    File uploadDir = new File(uploadPath);
	                    if (!uploadDir.exists()) {
	                        uploadDir.mkdirs(); // 업로드 디렉토리가 없으면 생성
	                    }
	                    File uploadFile = new File(uploadDir, fileName);
	                    file.transferTo(uploadFile);

	                    // 파일 DB 저장
	                    FileStorage fs = new FileStorage(fileName, uploadPath,participantId);

	                    int fileResult = dao.insFile(fs);
	                    if (fileResult <= 0) {
	                        throw new Exception("파일 등록 실패");
	                    }
	                }
	            }
	        } else {
	            System.out.println("참여 등록 실패");
	            throw new Exception("참여 등록 실패");
	        }
	        result = 1; 
	    } catch (Exception e) {
	        System.out.println("오류 발생: " + e.getMessage());
	        throw e; // 예외 발생 시 트랜잭션 롤백
	    }
	    return result;
	}


}
