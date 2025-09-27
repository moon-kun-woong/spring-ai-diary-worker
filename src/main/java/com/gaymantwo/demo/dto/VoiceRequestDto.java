package com.gaymantwo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoiceRequestDto {
    private String voiceText;
    private String timestamp;

    public VoiceRequestDto() {}

    public VoiceRequestDto(String voiceText, String timestamp) {
        this.voiceText = voiceText;
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