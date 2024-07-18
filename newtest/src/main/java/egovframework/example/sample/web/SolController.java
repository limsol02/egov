package egovframework.example.sample.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.object.bodytext.Section;
import kr.dogfoot.hwplib.object.bodytext.paragraph.Paragraph;
import kr.dogfoot.hwplib.object.bodytext.paragraph.text.HWPChar;
import kr.dogfoot.hwplib.reader.HWPReader;

@Controller
public class SolController {

	@Resource(name = "solService")
	private SolService service;

	// 파일업로드 경로
	@Value("${file.upload.path}")
	private String uploadPath;

	// 파일 이름을 Base64로 인코딩
	public String encodeFileName(String fileName) {
		return Base64.getEncoder().encodeToString(fileName.getBytes(StandardCharsets.UTF_8));
	}

	// 파일 이름을 Base64로 디코딩
	public String decodeFileName(String encodedFileName) {
		return new String(Base64.getDecoder().decode(encodedFileName), StandardCharsets.UTF_8);
	}

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
	@RequestMapping(value = "/delPart.do", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ResponseEntity<String> delPart(@RequestParam("participant_id") int participantId) {
		try {
			System.out.println("참여아이디 : " + participantId);
			int result = service.delPart(participantId);

			if (result > 0) {
				return ResponseEntity.ok("{\"result\": \"success\"}");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"삭제 실패\"}");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"SQL 에러가 발생했습니다: " + e.getMessage() + "\"}");
		}
	}

	@PostConstruct
	public void init() {
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
	}

	@RequestMapping(value = "/fileUpload.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
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

					String fullPath = uploadPath + fileName;
					File outputFile = new File(fullPath);
					out = new FileOutputStream(outputFile);
					out.write(bytes);
					out.close();

					// PDF 파일 경로 설정
					String pdfFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".pdf";
					String pdfFilePath = uploadPath + pdfFileName;
					File pdfFile = new File(pdfFilePath);

					// 파일 변환 (필요시)
					if (fileName.endsWith(".docx")) {
						convertDocxToPDF(outputFile, new File(uploadPath + pdfFile));
					} else if (fileName.endsWith(".hwp")) {
						convertHwpToPDF(outputFile, new File(uploadPath + pdfFile));
					}

					// 디버깅 메시지 추가
					System.out.println("Original file saved at: " + fullPath);
					System.out.println("PDF file saved at: " + pdfFilePath);
					System.out.println("PDF file exists: " + pdfFile.exists());

					printWriter = resp.getWriter();
					resp.setContentType("application/json; charset=utf-8");

					// 파일 이름을 Base64 인코딩 후 URL 인코딩
					String encodedFileName = URLEncoder.encode(
							Base64Utils.encodeToUrlSafeString(fileName.getBytes(StandardCharsets.UTF_8)), "UTF-8");
					String fileUrl = req.getContextPath() + "/viewFile.do?filename=" + encodedFileName;
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

	private void convertDocxToPDF(File inputFile, File outputFile) throws Exception {
		try (XWPFDocument doc = new XWPFDocument(new FileInputStream(inputFile)); PDDocument pdf = new PDDocument()) {

			// 클래스패스에서 폰트 파일 로드
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream fontStream = classLoader.getResourceAsStream("fonts/NotoSansKR-Regular.ttf");
			if (fontStream == null) {
				throw new IOException("폰트 파일을 찾을 수 없습니다.");
			}
			PDType0Font font = PDType0Font.load(pdf, fontStream);

			List<XWPFParagraph> paragraphs = doc.getParagraphs();
			PDPage page = new PDPage();
			pdf.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(pdf, page);
			contentStream.setFont(font, 12);

			float margin = 50;
			float yPosition = page.getMediaBox().getHeight() - margin;

			for (XWPFParagraph paragraph : paragraphs) {
				contentStream.beginText();
				contentStream.newLineAtOffset(margin, yPosition);
				contentStream.showText(paragraph.getText());
				contentStream.endText();
				yPosition -= 15; // 줄 간격
				if (yPosition <= margin) { // 새로운 페이지
					contentStream.close();
					page = new PDPage();
					pdf.addPage(page);
					contentStream = new PDPageContentStream(pdf, page);
					contentStream.setFont(font, 12);
					yPosition = page.getMediaBox().getHeight() - margin;
				}
			}
			contentStream.close();
			pdf.save(outputFile);
		}
	}

	private void convertHwpToPDF(File inputFile, File outputFile) throws Exception {
		HWPFile hwpFile = HWPReader.fromFile(inputFile.getAbsolutePath());
		try (PDDocument pdf = new PDDocument()) {
			PDPage page = new PDPage();
			pdf.addPage(page);
			PDPageContentStream contentStream = new PDPageContentStream(pdf, page);
			contentStream.setFont(PDType1Font.HELVETICA, 12);

			float margin = 50;
			float yPosition = page.getMediaBox().getHeight() - margin;

			for (Section section : hwpFile.getBodyText().getSectionList()) {
				for (int i = 0; i < section.getParagraphCount(); i++) {
					Paragraph paragraph = section.getParagraph(i);
					// Null 체크 추가
					if (paragraph.getText() != null) {
						for (HWPChar hwpChar : paragraph.getText().getCharList()) {
							contentStream.beginText();
							contentStream.newLineAtOffset(margin, yPosition);
							contentStream.showText(String.valueOf(hwpChar.getCode()));
							contentStream.endText();
							yPosition -= 15; // 줄 간격
							if (yPosition <= margin) { // 새로운 페이지
								contentStream.close();
								page = new PDPage();
								pdf.addPage(page);
								contentStream = new PDPageContentStream(pdf, page);
								contentStream.setFont(PDType1Font.HELVETICA, 12);
								yPosition = page.getMediaBox().getHeight() - margin;
							}
						}
					} else {
						System.out.println("Paragraph text is null for paragraph index: " + i);
					}
				}
			}
			contentStream.close();
			pdf.save(outputFile);
		}
	}

	@RequestMapping(value = "/viewFile.do", method = RequestMethod.GET)
	public void viewFile(@RequestParam("filename") String encodedFilename, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		try {
			// Base64 URL 안전한 디코딩
			String base64UrlSafeFilename = encodedFilename.replace('-', '+').replace('_', '/');
			byte[] decodedBytes = Base64.getDecoder().decode(base64UrlSafeFilename);
			String filename = new String(decodedBytes, StandardCharsets.UTF_8);
			System.out.println("Base64 Decoded filename: " + filename);

			// 파일 경로
			File file = new File(uploadPath + filename);
			if (!file.exists()) {
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			res.reset();
			res.setContentType(getContentType(filename));
			res.setContentLength((int) file.length());

			String userAgent = req.getHeader("User-Agent");
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				filename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
			} else {
				filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
			}

			res.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
			res.setHeader("Cache-Control", "cache, must-revalidate");
			res.setHeader("Content-Transfer-Encoding", "binary");
			res.setHeader("Pragma", "no-cache");
			res.setHeader("Expires", "-1");

			try (FileInputStream fis = new FileInputStream(file); OutputStream os = res.getOutputStream()) {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = fis.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.flush();
			} catch (IOException e) {
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				System.out.println("IOException occurred: " + e.getMessage());
				e.printStackTrace();
			}

		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			System.out.println("Exception occurred: " + e.getMessage());
			e.printStackTrace();
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

	@PostMapping("/savefileurl.do")
	@ResponseBody
	public ResponseEntity<?> uptUrl(@ModelAttribute Participant upt) throws Exception {
		try {
			System.out.println("참여자 아이디 : " + upt.getParticipant_id());
			System.out.println("파일 링크 : " + upt.getFile_url());
			return ResponseEntity.ok(service.uptURL(upt));
		} catch (Exception e) {
			return ResponseEntity.status(500).body("파일 URL 저장 실패: " + e.getMessage());
		}
	}
	
	
}
