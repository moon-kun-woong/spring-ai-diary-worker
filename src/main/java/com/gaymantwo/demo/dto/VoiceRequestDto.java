package com.gaymantwo.demo.dto;

public class VoiceRequestDto {
    private String voiceText;
    private String timestamp;

    public VoiceRequestDto() {}

    public VoiceRequestDto(String voiceText, String timestamp) {
        this.voiceText = voiceText;
        this.timestamp = timestamp;
    }

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VoiceRequestDto{" +
                "voiceText='" + voiceText + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}