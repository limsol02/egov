package egovframework.example.sample.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.JsonObject;

import egovframework.example.sample.service.Competition;
import egovframework.example.sample.service.FileStorage;
import egovframework.example.sample.service.Participant;
import egovframework.example.sample.service.SolService;

@Controller
public class SolController {

	@Resource(name = "solService")
	private SolService service;

	// 파일업로드 경로
	@Value("${file.upload.path}")
	private String uploadPath;

	// 메인페이지 호출
	@RequestMapping(value = "/sess.do", method = RequestMethod.GET)
	public String sess(HttpSession session, Model d) throws Exception {
		System.out.println(session.getAttribute("role"));
		return "newtest/top";
	}

	// 세션설정 메소드
	@RequestMapping(value = "/session.do", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String sess(@RequestParam("action") String action, HttpSession session, Model d) {
		String msg;
		switch (action) {
		case "judge":
			session.setAttribute("role", "judge");
			msg = "심사위원 세션 설정";
			break;
		case "admin":
			session.setAttribute("role", "admin");
			msg = "관리자 세션 설정";
			break;
		case "delete":
			session.invalidate();
			msg = "세션 삭제";
			break;
		default:
			msg = "유효하지 않은 요청";
			break;
		}

		d.addAttribute("msg", msg);
		return msg;
	}

	// 공모전 리스트
	@RequestMapping(value = "/participant.do", method = RequestMethod.GET)
	public String participant(Model d) throws Exception {
		d.addAttribute("comList", service.competitionList());
		return "newtest/upload";
	}

	// 파일 업로드 + 참여자 지원
	@RequestMapping(value = "/insPart.do", method = RequestMethod.POST)
	public String insPart(@RequestParam("application_title") String applicationTitle,
			@RequestParam("competition_id") int competition_id, @RequestParam("files") MultipartFile[] files,
			RedirectAttributes redirectAttributes) throws Exception {
		Participant insP = new Participant();
		insP.setApplication_title(applicationTitle);
		insP.setCompetition_id(competition_id);
		FileStorage insF = new FileStorage();

		try {
			int result = service.insFile(insF, files, insP);
			if (result > 0) {
				// 성공 메시지를 리다이렉트 속성에 추가
				redirectAttributes.addFlashAttribute("message", "참여 등록 및 파일 업로드 성공");
				return "redirect:/participant.do";
			} else {
				redirectAttributes.addFlashAttribute("message", "참여 등록 실패");
				return "redirect:/participant.do";
			}
		} catch (Exception e) {
			// 예외 발생 시 오류 메시지를 리다이렉트 속성에 추가
			redirectAttributes.addFlashAttribute("message", "오류 발생: " + e.getMessage());
			return "redirect:/participant.do";
		}
	}

	// 지원자 확인 메소드
	@RequestMapping(value = "partList.do", method = RequestMethod.GET)
	public String partList(Model d, HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam(value = "competition_id", defaultValue = "0") int competition_id) throws Exception {

		String role = (String) session.getAttribute("role");
		if (role == null || !role.equals("admin")) {
			redirectAttributes.addFlashAttribute("message", "접근 권한이 없습니다.");
			return "redirect:/sess.do"; // 접근 권한이 없으면 리다이렉트
		}

		d.addAttribute("comList", service.competitionList());

		try {
			System.out.println("competition_id: " + competition_id);
			d.addAttribute("plist", service.partList(competition_id));
		} catch (Exception e) {
			System.out.println("찾고찾던에러: " + e.getMessage());
		}

		return "newtest/participant";
	}

	// 다운로드
	@RequestMapping(value = "download.do", method = RequestMethod.GET)
	public void downloadFile(String fileName, HttpServletResponse response) {
		try {
			File file = new File(uploadPath + fileName);
			if (file.exists()) {
				String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
				try (FileInputStream fis = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = fis.read(buffer)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
					os.flush();
				}
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "register.do", method = RequestMethod.GET)
	public String register(Model d, HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam(value = "competition_id", defaultValue = "0") int competition_id) throws Exception {

		String role = (String) session.getAttribute("role");
		if (role == null || !role.equals("admin")) {
			redirectAttributes.addFlashAttribute("message", "접근 권한이 없습니다.");
			return "redirect:/sess.do"; // 접근 권한이 없으면 리다이렉트
		}

		d.addAttribute("comList", service.competitionList());

		try {
			System.out.println("competition_id: " + competition_id);
			List<Participant> partlist = service.partList(competition_id);
			d.addAttribute("plist", partlist);

			Map<Integer, String> competitionTitles = new HashMap<>();
			for (Participant participant : partlist) {
				int participantId = participant.getParticipant_id();
				if (!competitionTitles.containsKey(participantId)) {
					Competition competition = service.comTitleBycomId(participantId);
					String comTitle = competition.getCompetition_title();
					System.out.println(comTitle);
					competitionTitles.put(participantId, comTitle);
				}
				participant.setCompetitionTitle(competitionTitles.get(participantId));
			}

		} catch (Exception e) {
			System.out.println("찾고찾던에러: " + e.getMessage());
		}

		return "newtest/register";
	}

	// 참여작 삭제
	@RequestMapping(value = "/delPart.do", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> delPart(@RequestParam("participant_id") int participantId) {
		Map<String, Object> response = new HashMap<>();
		try {
			System.out.println("참여아이디 : " + participantId);
			int result = service.delPart(participantId);
			response.put("result", result);
		} catch (Exception e) {
			response.put("error", "SQL 에러가 발생했습니다: " + e.getMessage());
		}
		return response;
	}

	@PostConstruct
	public void init() {
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST)
	public void fileUpload(HttpServletRequest req, HttpServletResponse resp, MultipartHttpServletRequest multiFile)
			throws Exception {
		JsonObject jsonObject = new JsonObject();
		PrintWriter printWriter = null;
		OutputStream out = null;
		MultipartFile file = multiFile.getFile("files");

		if (file != null) {
			if (file.getSize() > 0 && file.getOriginalFilename() != null
					&& !file.getOriginalFilename().trim().isEmpty()) {
				try {
					String fileName = file.getOriginalFilename();
					byte[] bytes = file.getBytes();

					String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
					String fullPath = uploadPath + uniqueFileName;

					out = new FileOutputStream(new File(fullPath));
					out.write(bytes);

					printWriter = resp.getWriter();
					String fileUrl = req.getContextPath() + "/newtest/viewFile?filename=" + uniqueFileName;
					System.out.println("URL : " + fileUrl);

					jsonObject.addProperty("uploaded", 1);
					jsonObject.addProperty("fileName", fileName);
					jsonObject.addProperty("url", fileUrl);
					printWriter.print(jsonObject);

				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("IOException: " + e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("Exception: " + e.getMessage());
				} finally {
					if (out != null) {
						out.close();
					}
					if (printWriter != null) {
						printWriter.close();
					}
				}
			} else {
				System.err.println("파일이 비어 있거나 파일 이름이 잘못되었습니다.");
			}
		} else {
			System.err.println("업로드된 파일을 찾을 수 없습니다.");
		}
	}

	@RequestMapping(value = "/viewFile", method = RequestMethod.GET)
	public ResponseEntity<byte[]> viewFile(@RequestParam("filename") String filename) {
		try {
			File file = new File(uploadPath + filename);
			if (!file.exists()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			FileInputStream fis = new FileInputStream(file);
			byte[] fileContent = fis.readAllBytes();
			fis.close();

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, getContentType(filename));
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");

			return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String getContentType(String filename) {
		if (filename.endsWith(".pdf")) {
			return "application/pdf";
		} else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return "image/jpeg";
		} else if (filename.endsWith(".png")) {
			return "image/png";
		} else {
			return "application/octet-stream";
		}
	}
}
