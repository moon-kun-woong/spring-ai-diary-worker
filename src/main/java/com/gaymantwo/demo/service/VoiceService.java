package com.gaymantwo.demo.service;

import com.gaymantwo.demo.dto.VoiceResponseDto;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class VoiceService {

    private final OpenAiAudioTranscriptionModel audioTranscriptionModel;

    @Autowired
    public VoiceService(OpenAiAudioTranscriptionModel audioTranscriptionModel) {
        this.audioTranscriptionModel = audioTranscriptionModel;
    }


    public VoiceResponseDto processVoiceFile(MultipartFile audioFile) {
        try {
            System.out.println("음성 파일 STT 처리 시작");

            // MultipartFile을 ByteArrayResource로 변환
            ByteArrayResource audioResource = new ByteArrayResource(audioFile.getBytes()) {
                @Override
                public String getFilename() {
                    return audioFile.getOriginalFilename();
                }
            };

            // AudioTranscriptionPrompt 생성
            AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioResource);

            // Spring AI를 사용해서 음성을 텍스트로 변환
            AudioTranscriptionResponse response = audioTranscriptionModel.call(transcriptionRequest);
            String transcribedText = response.getResult().getOutput();

            System.out.println("STT 변환 결과: " + transcribedText);

            // 변환된 텍스트 반환 (추가 처리 없이)

            return new VoiceResponseDto(
                true,
                "음성 파일이 성공적으로 텍스트로 변환되었습니다.",
                transcribedText
            );

        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
            return new VoiceResponseDto(
                false,
                "음성 파일을 읽는 중 오류가 발생했습니다: " + e.getMessage(),
                null
            );
        } catch (Exception e) {
            System.err.println("STT 처리 중 오류 발생: " + e.getMessage());
            return new VoiceResponseDto(
                false,
                "음성 인식 처리 중 오류가 발생했습니다: " + e.getMessage(),
                null
            );
        }
    }
}