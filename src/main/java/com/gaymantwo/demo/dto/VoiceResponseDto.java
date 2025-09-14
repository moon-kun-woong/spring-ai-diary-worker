package com.gaymantwo.demo.dto;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProcessedText() {
        return processedText;
    }

    public void setProcessedText(String processedText) {
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