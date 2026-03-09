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
    public ResponseEntity<?> askAi(@RequestBody Map<String, Object> request) {
        String userQuestion = (String)request.get("question");
        Map<String, Object> context = (Map<String, Object>) request.get("analysisContext");
        
        String aiReference = "정보 없음";
        if (context != null && "분석 완료".equals(context.get("status"))) {
            aiReference = String.format(
                "이번 달 예상 지출: %s원, 다음 달 예측: %s원, 월평균: %s원",
                context.get("currentPredict"), 
                context.get("nextPredict"), 
                context.get("avg")
            );
        }
        
        String finalPrompt = String.format(
    	    "너는 가계부 앱 'OSORI'의 친절하고 유능한 금융 비서야.\n\n" +
    	    "### [참고용 사용자 분석 데이터] ###\n" +
    	    "%s\n" +
    	    "###################################\n\n" +
    	    "**답변 지침:**\n" +
    	    "1. 사용자가 지출 현황이나 예측을 직접 물어볼 때만 위 데이터를 구체적인 수치와 함께 언급해.\n" +
    	    "2. 점심 메뉴 추천이나 일상적인 대화에서는 위 데이터를 '배경 지식'으로만 활용해. (예: 지출이 많으니 가성비 메뉴를 추천하는 식)\n" +
    	    "3. 모든 답변에 예상 지출 수치를 나열하지 마. 질문의 맥락에 집중해.\n" +
    	    "4. 친절하고 자연스러운 한국어로 대답해.\n\n" +
    	    "사용자 질문: %s", 
    	    aiReference, userQuestion
    	);
        
        // 1. Gemini 1.5 Flash model 엔드포인트 URL
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiKey;

        RestTemplate restTemplate = new RestTemplate();
        
        // 2. 요청 바디(Body) 구성
        // GPT에게 역할을 부여하고 질문을 전달합니다.
        Map<String, Object> contents = Map.of(
        	"parts", List.of(Map.of("text", finalPrompt))
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
