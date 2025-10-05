package com.gaymantwo.demo.controller;

import com.gaymantwo.demo.dto.VoiceResponseDto;
import com.gaymantwo.demo.service.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // CORS 허용
public class VoiceController {

    private final VoiceService voiceService;

    @Autowired
    public VoiceController(VoiceService voiceService) {
        this.voiceService = voiceService;
    }

    @PostMapping("/voice/upload")
    public ResponseEntity<VoiceResponseDto> processVoiceFile(@RequestParam("audioFile") MultipartFile audioFile) {
        try {
            System.out.println("POST /api/voice/upload 요청 수신");
            System.out.println("파일명: " + audioFile.getOriginalFilename());
            System.out.println("파일 크기: " + audioFile.getSize() + " bytes");
            System.out.println("파일 타입: " + audioFile.getContentType());

            if (audioFile.isEmpty()) {
                VoiceResponseDto errorResponse = new VoiceResponseDto(
                    false,
                    "업로드된 음성 파일이 비어있습니다.",
                    null
                );
                return ResponseEntity.badRequest().body(errorResponse);
            }

            VoiceResponseDto response = voiceService.processVoiceFile(audioFile);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            System.err.println("음성 파일 처리 중 오류 발생: " + e.getMessage());
            VoiceResponseDto errorResponse = new VoiceResponseDto(
                false,
                "음성 파일 처리 중 서버 내부 오류가 발생했습니다: " + e.getMessage(),
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