package com.gaymantwo.demo.service;

import com.gaymantwo.demo.dto.VoiceRequestDto;
import com.gaymantwo.demo.dto.VoiceResponseDto;
import org.springframework.stereotype.Service;

@Service
public class VoiceService {

    public VoiceResponseDto processVoiceText(VoiceRequestDto request) {
        try {
            // 음성 텍스트 로깅
            System.out.println("수신된 음성 텍스트: " + request.getVoiceText());
            System.out.println("타임스탬프: " + request.getTimestamp());

            // 여기서 추후 AI 처리, 데이터베이스 저장 등의 로직을 구현할 수 있습니다.
            String processedText = processText(request.getVoiceText());

            return new VoiceResponseDto(
                true, 
                "음성 텍스트가 성공적으로 처리되었습니다.", 
                processedText
            );

        } catch (Exception e) {
            System.err.println("음성 텍스트 처리 중 오류 발생: " + e.getMessage());
            return new VoiceResponseDto(
                false, 
                "음성 텍스트 처리 중 오류가 발생했습니다: " + e.getMessage(), 
                null
            );
        }
    }

    private String processText(String voiceText) {
        // 간단한 텍스트 처리 예제 (대소문자 변환, 길이 체크 등)
        if (voiceText == null || voiceText.trim().isEmpty()) {
            return "빈 텍스트";
        }

        String processed = voiceText.trim();
        
        // 추후 여기에 AI 분석, 감정 분석, 키워드 추출 등의 로직을 추가할 수 있습니다.
        
        return "처리됨: " + processed + " (길이: " + processed.length() + "자)";
    }
}