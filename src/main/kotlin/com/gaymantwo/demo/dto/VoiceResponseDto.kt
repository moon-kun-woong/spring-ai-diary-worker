package com.gaymantwo.demo.dto

data class VoiceResponseDto(
    val success: Boolean,
    val message: String,
    val processedText: String?
)
