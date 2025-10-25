package com.gaymantwo.demo.controller

import com.gaymantwo.demo.dto.VoiceResponseDto
import com.gaymantwo.demo.service.VoiceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = ["*"]) // CORS 허용
class VoiceController(
    private val voiceService: VoiceService
) {

    @PostMapping("/voice/upload")
    fun processVoiceFile(@RequestParam("audioFile") audioFile: MultipartFile): ResponseEntity<VoiceResponseDto> {
        return try {
            println("POST /api/voice/upload 요청 수신")
            println("파일명: ${audioFile.originalFilename}")
            println("파일 크기: ${audioFile.size} bytes")
            println("파일 타입: ${audioFile.contentType}")

            if (audioFile.isEmpty) {
                val errorResponse = VoiceResponseDto(
                    success = false,
                    message = "업로드된 음성 파일이 비어있습니다.",
                    processedText = null
                )
                return ResponseEntity.badRequest().body(errorResponse)
            }

            val response = voiceService.processVoiceFile(audioFile)

            if (response.success) {
                ResponseEntity.ok(response)
            } else {
                ResponseEntity.badRequest().body(response)
            }

        } catch (e: Exception) {
            System.err.println("음성 파일 처리 중 오류 발생: ${e.message}")
            val errorResponse = VoiceResponseDto(
                success = false,
                message = "음성 파일 처리 중 서버 내부 오류가 발생했습니다: ${e.message}",
                processedText = null
            )
            ResponseEntity.internalServerError().body(errorResponse)
        }
    }

    @GetMapping("/voice/test")
    fun testEndpoint(): ResponseEntity<String> {
        return ResponseEntity.ok("음성 API 엔드포인트가 정상적으로 작동 중입니다.")
    }
}
