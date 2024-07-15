package egovframework.example.sample.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
}
