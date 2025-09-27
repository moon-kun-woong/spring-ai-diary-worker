package com.gaymantwo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoiceResponseDto {
    private boolean success;
    private String message;
    private String processedText;

    public VoiceResponseDto() {}

    public VoiceResponseDto(boolean success, String message, String processedText) {
        this.success = success;
        this.message = message;
        this.processedText = processedText;
    }

    @Override
    public String toString() {
        return "VoiceResponseDto{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", processedText='" + processedText + '\'' +
                '}';
    }
}