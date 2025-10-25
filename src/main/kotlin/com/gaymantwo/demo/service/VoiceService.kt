package com.gaymantwo.demo.service

import com.gaymantwo.demo.dto.VoiceResponseDto
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions
import org.springframework.ai.openai.api.OpenAiAudioApi
import org.springframework.core.io.ByteArrayResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Service
class VoiceService(
    private val audioTranscriptionModel: OpenAiAudioTranscriptionModel
) {

    fun processVoiceFile(audioFile: MultipartFile): VoiceResponseDto {
        return try {
            println("음성 파일 STT 처리 시작")

            // MultipartFile을 ByteArrayResource로 변환
            val audioResource = object : ByteArrayResource(audioFile.bytes) {
                override fun getFilename(): String? = audioFile.originalFilename
            }

            // 한국어 인식을 위한 옵션 설정
            val options = OpenAiAudioTranscriptionOptions.builder()
                .model(OpenAiAudioApi.WhisperModel.WHISPER_1.value)
                .language("ko") // 한국어 명시
                .prompt("이것은 한국어 음성 일기입니다. 오늘, 일상, 생각, 느낌") // 한국어 컨텍스트 제공
                .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .temperature(0.0f) // 일관성 향상 (0.0 ~ 1.0, 낮을수록 일관적)
                .build()

            // AudioTranscriptionPrompt 생성 (옵션 포함)
            val transcriptionRequest = AudioTranscriptionPrompt(audioResource, options)

            // Spring AI를 사용해서 음성을 텍스트로 변환
            val response = audioTranscriptionModel.call(transcriptionRequest)
            val transcribedText = response.result.output

            println("STT 변환 결과: $transcribedText")

            VoiceResponseDto(
                success = true,
                message = "음성 파일이 성공적으로 텍스트로 변환되었습니다.",
                processedText = transcribedText
            )

        } catch (e: IOException) {
            System.err.println("파일 읽기 오류: ${e.message}")
            VoiceResponseDto(
                success = false,
                message = "음성 파일을 읽는 중 오류가 발생했습니다: ${e.message}",
                processedText = null
            )
        } catch (e: Exception) {
            System.err.println("STT 처리 중 오류 발생: ${e.message}")
            VoiceResponseDto(
                success = false,
                message = "음성 인식 처리 중 오류가 발생했습니다: ${e.message}",
                processedText = null
            )
        }
    }
}
