package egovframework.example.sample.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.example.sample.service.JuService;

@Controller
public class JuController {

	@Resource(name="juService")
	private JuService jusvc;

	//관리자 페이지 보여주기
	@RequestMapping(value = "/admin.do", method = RequestMethod.GET)
	public String adminPage(HttpSession session, Model d) throws Exception {
		String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "newtest/admin/main"; // admin 인 경우 main 페이지로 이동합니다.
        } else {
            return "newtest/top"; // null이거나 admin 이 아닌 경우 top 페이지로 이동합니다.
        }
	}
	
	//평가항목만드는 페이지 보여주기
	@RequestMapping(value = "/evaluation.do", method = RequestMethod.GET)
	public String evaluationPage(HttpSession session, Model d) throws Exception {
		String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) {
            return "newtest/admin/evaluation"; // admin 인 경우 main 페이지로 이동합니다.
        } else {
            return "newtest/top"; // null이거나 admin 이 아닌 경우 top 페이지로 이동합니다.
        }
	}
	//평가항목등록
	@ResponseBody
	@RequestMapping(value="/addEvaluation.do", method=RequestMethod.POST)
	public ResponseEntity<?> addEvaluation(HttpSession session,@RequestParam("Item")String item){
		// 평가 항목을 출력해보기
	    String role = (String) session.getAttribute("role");
	    if ("admin".equals(role)) 
	    {
	    	try 
	    	{
	    		System.out.println("받은 평가 항목 : " + item);
				jusvc.addEvaluation(item);
				Map<String, String> response = new HashMap<>();
		        response.put("message", "평가 항목이 추가되었습니다.");
				return ResponseEntity.ok(response); // 원하는 응답을 반환할 수 있습니다.
			}
	    	catch (Exception e) 
	    	{
	    		Map<String, String> response = new HashMap<>();
	            response.put("message", "평가 항목 추가 중 오류가 발생했습니다.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(response);
			}
	    }
	    else 
	    {
	    	Map<String, String> response = new HashMap<>();
	        response.put("message", "관리자 권한이 필요합니다.");
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(response);
	    }
	}
	
}
