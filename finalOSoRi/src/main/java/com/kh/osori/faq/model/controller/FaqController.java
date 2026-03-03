package com.kh.osori.faq.model.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.kh.osori.faq.model.service.FaqService;
import com.kh.osori.faq.model.vo.Faq;

@RestController
@RequestMapping("/faq")
public class FaqController {

	@Autowired
	private FaqService service;
	
	@Value("${gemini.api.key}")
	private String geminiKey;
	
	@PostMapping("/ask-ai")
    public ResponseEntity<?> askAi(@RequestBody Map<String, String> request) {
        String userQuestion = request.get("question");
        
        // 1. Gemini 1.5 Flash model 엔드포인트 URL
        String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + geminiKey;
        System.out.println("API KEY: " + geminiKey);
        RestTemplate restTemplate = new RestTemplate();
        
        // 2. 요청 바디(Body) 구성
        // GPT에게 역할을 부여하고 질문을 전달합니다.
        Map<String, Object> contents = Map.of(
        	"parts", List.of(Map.of("text", "너는 가계부 앱 'OSORI'의 전문 상담사야. 사용자의 질문에 친절하게 한국어로 답변해줘. 질문: " + userQuestion))
        );
        Map<String, Object> body = Map.of("contents", List.of(contents));
        

        try {
            // 3. Gemini 서버로 POST 요청 보내기
        	ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
            
            // 4. 응답 데이터에서 AI의 답변 텍스트만 추출하기
            // 응답 구조: andidates[0].content.parts[0].text
        	List candidates = (List) response.getBody().get("candidates");
            Map firstCandidate = (Map) candidates.get(0);
            Map content = (Map) firstCandidate.get("content");
            List parts = (List) content.get("parts");
            Map firstPart = (Map) parts.get(0);
            String aiAnswer = (String) firstPart.get("text");

            // 5. 성공 시 리액트로 답변 전달
            return ResponseEntity.ok(Map.of("answer", aiAnswer));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("AI 서비스와 통신 중 오류가 발생했습니다.");
        }
    }
	
	@GetMapping("/questionList")
	public ResponseEntity<?> questionList(){
		List<Faq> qList = service.questionList();
		RestTemplate restTemplate = new RestTemplate();
		if(qList != null && !qList.isEmpty()) {
			return ResponseEntity.ok(qList);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("질문 목록 조회를 실패했습니다.");
		}
	}
	
	@PostMapping("/addNewQuestion")
	public ResponseEntity<?> addNewQuestion(@RequestBody String question){
		int result = service.addNewQuestion(question);
		
		if(result > 0) {
			return ResponseEntity.ok(200);
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("질문 목록 조회를 실패했습니다.");
		}
	}
	
}
