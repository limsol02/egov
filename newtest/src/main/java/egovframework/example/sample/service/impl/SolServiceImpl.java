package egovframework.example.sample.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.sample.service.Competition;
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
	public List<Competition> competitionList() throws Exception {
		return dao.competitionList();
	}

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

	        // 방금 삽입된 Participant의 ID 가져오기
	        int participantId = insPart.getParticipant_id();
	        System.out.println("생성된 참가자 ID: " + participantId);

	        if (partResult > 0 && participantId > 0) {
	            System.out.println("참여 등록 성공");
	            System.out.println("업로드 경로: " + uploadPath);

	            // 파일 등록 (파일이 있을 경우에만 처리)
	            if (files != null && files.length > 0) {
	                for (MultipartFile file : files) {
	                    if (!file.isEmpty()) {  // 파일이 존재하는 경우에만 처리
	                        // 파일 원래이름 가져오기
	                        String fileName = file.getOriginalFilename();
	                        System.out.println("업로드할 파일 이름: " + fileName);

	                        // Properties 경로 참조
	                        File uploadDir = new File(uploadPath);
	                        if (!uploadDir.exists()) {
	                            boolean dirCreated = uploadDir.mkdirs(); // 업로드 디렉토리가 없으면 생성
	                            if (dirCreated) {
	                                System.out.println("업로드 디렉토리 생성 성공: " + uploadDir.getAbsolutePath());
	                            } else {
	                                System.out.println("업로드 디렉토리 생성 실패: " + uploadDir.getAbsolutePath());
	                            }
	                        } else {
	                            System.out.println("업로드 디렉토리가 이미 존재합니다: " + uploadDir.getAbsolutePath());
	                        }

	                        File uploadFile = new File(uploadDir, fileName);
	                        System.out.println("업로드할 파일 전체 경로: " + uploadFile.getAbsolutePath());

	                        // 파일 업로드 처리
	                        try {
	                            file.transferTo(uploadFile);
	                            System.out.println("파일 업로드 성공: " + fileName);
	                        } catch (IOException e) {
	                            System.out.println("파일 업로드 실패: " + fileName);
	                            e.printStackTrace();
	                            throw e;
	                        }

	                        // 파일 DB 저장
	                        FileStorage fs = new FileStorage(fileName, uploadPath, participantId);

	                        int fileResult = dao.insFile(fs);
	                        if (fileResult <= 0) {
	                            throw new Exception("파일 등록 실패");
	                        }
	                    } else {
	                        System.out.println("업로드할 파일이 없습니다.");
	                    }
	                }
	            } else {
	                System.out.println("업로드할 파일이 없습니다.");
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

	@Override
	public List<Participant> partList(int competition_id) throws Exception {
		return dao.partList(competition_id);
	}

	@Override
	public Competition comTitleBycomId(int paricipant_id) throws Exception {
		return dao.comTitleBycomId(paricipant_id);
	}

	@Override
	public int delPart(int participant_id) throws Exception {
		try {
			return dao.delPart(participant_id);
		} catch (Exception e) {
			System.out.println("SQL 에러 발생: " + e.getMessage());
			throw e;
		}
	}
}
