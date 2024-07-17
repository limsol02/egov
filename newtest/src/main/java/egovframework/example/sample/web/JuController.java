package egovframework.example.sample.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.example.sample.service.JuService;
import egovframework.example.sample.service.Score;
import egovframework.example.sample.service.Sheet;

@Controller
public class JuController {

	@Resource(name="juService")
	private JuService jusvc;

	//관리자 페이지 보여주기
	@RequestMapping(value = "/admin.do", method = RequestMethod.GET)
	public String adminPage(HttpSession session, Model d) throws Exception {
		String role = (String) session.getAttribute("role");
		if (jusvc.cheekAdmin(role)==1) {
			return "redirect:/sess.do";
		}
		return "newtest/admin/main";
	}
	//공모전 만드는 페이지
	@RequestMapping(value = "/competition.do", method = RequestMethod.GET)
	public String competitionPage(HttpSession session, Model d) throws Exception {
		String role = (String) session.getAttribute("role");
		if (jusvc.cheekAdmin(role)==1) {
			return "redirect:/sess.do";
		}
        return "newtest/admin/competition";
	}
	
	//공모전 등록하기
	@ResponseBody
	@RequestMapping(value="/addCompetition.do", method=RequestMethod.POST,produces = "application/json")
	public ResponseEntity<?> addCompetition(HttpSession session,@RequestParam("title")String title){
	    String role = (String) session.getAttribute("role");
	    if (jusvc.cheekAdmin(role) == 0){
	    	try 
	    	{
	    		System.out.println("받은 공모전 제목 : " + title);
	    		jusvc.addCompetition(title);
	    		Map<String, String> response = new HashMap<>();
	    		response.put("message", "공모전이 추가되었습니다.");
	    		return ResponseEntity.ok(response); // 원하는 응답을 반환할 수 있습니다.
	    	}
	    	catch (Exception e) 
	    	{
	    		Map<String, String> response = new HashMap<>();
	    		response.put("message", "서버 오류가 발생했습니다.");
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
	
	
	
	//평가항목만드는 페이지 보여주기
	@RequestMapping(value = "/evaluation.do", method = RequestMethod.GET)
	public String evaluationPage(HttpSession session, Model d) throws Exception {
		String role = (String) session.getAttribute("role");
		if (jusvc.cheekAdmin(role)==1) {
			return "redirect:/sess.do";
		}
        return "newtest/admin/evaluation";
	}
	
	//평가항목등록
	@ResponseBody
	@RequestMapping(value="/addEvaluation.do", method=RequestMethod.POST,produces = "application/json")
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
	
	//평가지 만드는 뷰
	@RequestMapping(value = "/sheet.do", method = RequestMethod.GET)
	public String sheet(HttpSession session, Model m) throws Exception {
		String role = (String) session.getAttribute("role");
		if (jusvc.cheekAdmin(role)==1) {
			return "redirect:/sess.do";
		}
		m.addAttribute("list",jusvc.getCompetitionList());
		m.addAttribute("item",jusvc.getEvaluationItemsList());
        return "newtest/admin/sheet";
	}
	//평가지 등록(미완성 + jsp포함)
	@ResponseBody
	@PostMapping("/addSheet.do")
	public Map<String,Object> addSheet(HttpSession session,@RequestParam("competition_id") int competitionId, 
            @RequestParam("evaluation_ids") List<Integer> evaluationIds){
		// 폼 데이터가 제대로 전달되었는지 확인하기 위해 sheet 객체의 내용을 출력합니다.
	    System.out.println("공모전 ID: " + competitionId);
	    System.out.println("평가항목 ID: " + evaluationIds);
	    String role = (String) session.getAttribute("role");
	    if (jusvc.cheekAdmin(role) == 0){
	    	jusvc.addSheet(competitionId,evaluationIds);
	    	Map<String, Object> response = new HashMap<>();
	    	response.put("result","평가지등록완료");
	    	return response;
	    }
	    Map<String, Object> response = new HashMap<>();
    	response.put("result","평가지등록완료");
    	return response; 
	}
	
	//점수 등록하기
	@ResponseBody
	@RequestMapping(value="/addScore.do", method=RequestMethod.POST,produces = "application/json")
	public Map<String,Object> addScore(HttpSession session,@ModelAttribute List<Score> score){
		// 평가 항목을 출력해보기
	    String role = (String) session.getAttribute("role");
	    if(jusvc.cheekAdmin(role) == 0) {
	    	try 
	    	{
	    		System.out.println("받은 점수 목록 : " + score);
	    		jusvc.addScore(score);
	    		Map<String, Object> response = new HashMap<>();
	    		response.put("message", "공모전이 추가되었습니다.");
	    		return response; // 원하는 응답을 반환할 수 있습니다.
	    	}
	    	catch (Exception e) 
	    	{
	    		Map<String, Object> response = new HashMap<>();
	    		response.put("message", "서버 오류가 발생했습니다.");
	    		return response;
	    	}  	
	    }
	    else 
	    {
	    	Map<String, Object> response = new HashMap<>();
	    	response.put("message", "관리자 권한이 필요합니다.");
	    	return response;
	    }
	}
	
	
}