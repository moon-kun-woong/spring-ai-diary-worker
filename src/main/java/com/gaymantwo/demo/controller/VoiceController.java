package com.gaymantwo.demo.controller;

import com.gaymantwo.demo.dto.VoiceRequestDto;
import com.gaymantwo.demo.dto.VoiceResponseDto;
import com.gaymantwo.demo.service.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 개발에서 CORS 허용
public class VoiceController {

    private final VoiceService voiceService;

    @Autowired
    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
    }

    @PostMapping("/voice")
    public ResponseEntity<VoiceResponseDto> processVoice(@RequestBody VoiceRequestDto request) {
        try {
            System.out.println("POST /api/voice 요청 수신: " + request);
            
            VoiceResponseDto response = voiceService.processVoiceText(request);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            System.err.println("컨트롤러에서 오류 발생: " + e.getMessage());
            VoiceResponseDto errorResponse = new VoiceResponseDto(
                false, 
                "서버 내부 오류가 발생했습니다: " + e.getMessage(), 
                null
            );
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/voice/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("음성 API 엔드포인트가 정상적으로 작동 중입니다.");
    }
}