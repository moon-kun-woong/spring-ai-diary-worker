# Spring AI Diary Worker - 전체 코드 흐름

## 시스템 아키텍처 개요

```mermaid
graph TB
    subgraph "브라우저 (Frontend)"
        A[main.html] --> B[MediaRecorder API]
        B --> C[음성 녹음]
        C --> D[FormData 생성]
        D --> E[POST /api/voice/upload]
    end

    subgraph "Spring Boot Application"
        subgraph "Controller Layer"
            F[VoiceController]
            G[MainController]
        end

        subgraph "Service Layer"
            H[VoiceService]
        end

        subgraph "DTO Layer"
            I[VoiceResponseDto]
        end

        subgraph "External Services"
            J[OpenAI Whisper API]
        end
    end

    subgraph "Database"
        K[MariaDB]
    end

    E --> F
    F --> H
    H --> J
    H --> I
    I --> F
    F --> E

    G --> A

    H -.-> K

    style A fill:#e1f5fe
    style F fill:#f3e5f5
    style H fill:#e8f5e8
    style J fill:#fff3e0
    style K fill:#fce4ec
```

## 상세 코드 흐름

### 1. 프론트엔드 흐름
```mermaid
graph LR
    A[사용자 음성 녹음 시작] --> B[MediaRecorder.start()]
    B --> C[음성 데이터 수집]
    C --> D[녹음 중지]
    D --> E[Blob 생성]
    E --> F[FormData에 파일 추가]
    F --> G[fetch API로 서버 전송]
    G --> H[응답 처리 및 화면 표시]
```

### 2. 백엔드 API 흐름
```mermaid
graph TD
    A[POST /api/voice/upload] --> B{파일 검증}
    B -->|유효| C[VoiceService.processVoiceFile()]
    B -->|무효| D[400 Bad Request]

    C --> E[MultipartFile → ByteArrayResource 변환]
    E --> F[AudioTranscriptionPrompt 생성]
    F --> G[OpenAI Whisper API 호출]
    G --> H{STT 처리 성공?}

    H -->|성공| I[VoiceResponseDto 성공 응답 생성]
    H -->|실패| J[VoiceResponseDto 실패 응답 생성]

    I --> K[200 OK 응답]
    J --> L[400/500 Error 응답]

    style A fill:#e3f2fd
    style C fill:#e8f5e8
    style G fill:#fff3e0
    style I fill:#e1f5fe
    style J fill:#ffebee
```

### 3. 의존성 주입 흐름
```mermaid
graph LR
    A[Spring Boot 시작] --> B[OpenAiAudioTranscriptionModel Bean 생성]
    B --> C[VoiceService Bean 생성]
    C --> D[VoiceController Bean 생성]
    D --> E[API 엔드포인트 등록]

    subgraph "Configuration"
        F[application.properties]
        G[OpenAI API Key]
        H[Spring AI 설정]
    end

    F --> B
    G --> B
    H --> B
```

### 4. 에러 처리 흐름
```mermaid
graph TD
    A[요청 수신] --> B{파일 존재 여부}
    B -->|없음| C[빈 파일 에러]
    B -->|있음| D{파일 크기 검증}

    D -->|25MB 초과| E[파일 크기 에러]
    D -->|정상| F{파일 타입 검증}

    F -->|지원 안함| G[파일 타입 에러]
    F -->|지원| H[STT 처리]

    H --> I{STT 성공?}
    I -->|실패| J[STT 처리 에러]
    I -->|성공| K[정상 응답]

    C --> L[400 Bad Request]
    E --> L
    G --> L
    J --> M[500 Internal Server Error]
    K --> N[200 OK]

    style C fill:#ffcdd2
    style E fill:#ffcdd2
    style G fill:#ffcdd2
    style J fill:#ffcdd2
    style K fill:#c8e6c9
```

## 주요 컴포넌트 관계도

```mermaid
classDiagram
    class VoiceController {
        -VoiceService voiceService
        +processVoiceFile(MultipartFile) ResponseEntity~VoiceResponseDto~
        +testEndpoint() ResponseEntity~String~
    }

    class VoiceService {
        -OpenAiAudioTranscriptionModel audioTranscriptionModel
        +processVoiceFile(MultipartFile) VoiceResponseDto
    }

    class VoiceResponseDto {
        -boolean success
        -String message
        -String processedText
        +getters/setters
    }

    class OpenAiAudioTranscriptionModel {
        +call(AudioTranscriptionPrompt) AudioTranscriptionResponse
    }

    class MainController {
        +main() String
    }

    VoiceController --> VoiceService : uses
    VoiceService --> VoiceResponseDto : creates
    VoiceService --> OpenAiAudioTranscriptionModel : uses
    VoiceController --> VoiceResponseDto : returns

    note for VoiceController "CORS 설정\n에러 핸들링\n로깅"
    note for VoiceService "파일 변환\nSTT 처리\n예외 처리"
    note for OpenAiAudioTranscriptionModel "Spring AI 제공\nOpenAI Whisper 연동"
```

## 데이터 흐름

```mermaid
graph LR
    A[음성 파일<br/>MultipartFile] --> B[ByteArrayResource]
    B --> C[AudioTranscriptionPrompt]
    C --> D[OpenAI Whisper API]
    D --> E[AudioTranscriptionResponse]
    E --> F[String transcribedText]
    F --> G[VoiceResponseDto]
    G --> H[JSON Response]

    style A fill:#e1f5fe
    style D fill:#fff3e0
    style G fill:#e8f5e8
    style H fill:#f3e5f5
```