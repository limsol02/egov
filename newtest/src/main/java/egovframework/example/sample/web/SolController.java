package egovframework.example.sample.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.example.sample.service.FileStorage;
import egovframework.example.sample.service.Participant;
import egovframework.example.sample.service.SolService;

@Controller
public class SolController {

	@Resource(name="solService")
	private SolService service;
		
	@RequestMapping(value = "/sess.do", method = RequestMethod.GET)
	public String sess(HttpSession session, Model d) throws Exception {
		System.out.println(session.getAttribute("role"));
		return "newtest/top";
	}

	@RequestMapping(value = "/session.do", method = RequestMethod.GET , produces = "text/plain;charset=UTF-8")
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
	@RequestMapping(value = "/participant.do", method = RequestMethod.GET)
	public String participant(Model d) throws Exception {
		d.addAttribute("comList", service.competitionList());
		return "newtest/upload";
	}
	
	@RequestMapping(value="/insPart.do", method = RequestMethod.POST)
	public String insPart(@RequestParam("application_title") String applicationTitle,
	                      @RequestParam("files") MultipartFile[] files,
	                      RedirectAttributes redirectAttributes) throws Exception {
	    Participant insP = new Participant();
	    insP.setApplication_title(applicationTitle);
	    FileStorage insF = new FileStorage();

	    try {
	        int result = service.insFile(insF, files, insP);
	        if (result > 0) {
	            // 성공 메시지를 리다이렉트 속성에 추가
	            redirectAttributes.addFlashAttribute("message", "참여 등록 및 파일 업로드 성공");
	            return "redirect:/participant.do";
	        } else {
	            redirectAttributes.addFlashAttribute("message", "참여 등록 실패");
	            return "redirect:/errorPage.do";
	        }
	    } catch (Exception e) {
	        // 예외 발생 시 오류 메시지를 리다이렉트 속성에 추가
	        redirectAttributes.addFlashAttribute("message", "오류 발생: " + e.getMessage());
	        return "redirect:/errorPage.do";
	    }
	}
	
}
