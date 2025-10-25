package org.dcode.artificialswbackend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

//@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account-key-path:firebase-service-account.json}")
    private String serviceAccountKeyPath;

    @Value("${firebase.project-id:aswb-ac645}")
    private String projectId;

    //@PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                GoogleCredentials credentials;
                
                // 환경변수에서 서비스 계정 키 확인
                String serviceAccountKey = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY");
                
                if (serviceAccountKey != null && !serviceAccountKey.isEmpty()) {
                    // 환경변수에서 JSON 문자열로 인증 정보 로드
                    InputStream serviceAccountStream = new ByteArrayInputStream(serviceAccountKey.getBytes());
                    credentials = GoogleCredentials.fromStream(serviceAccountStream);
                    System.out.println("Firebase initialized with environment variable");
                } else {
                    // 파일에서 인증 정보 로드
                    ClassPathResource resource = new ClassPathResource(serviceAccountKeyPath);
                    if (resource.exists()) {
                        InputStream serviceAccount = resource.getInputStream();
                        credentials = GoogleCredentials.fromStream(serviceAccount);
                        System.out.println("Firebase initialized with service account file");
                    } else {
                        System.out.println("Firebase service account file not found, skipping initialization");
                        return;
                    }
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setProjectId(projectId)
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase application initialized successfully");
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize Firebase: " + e.getMessage());
            // 개발 환경에서는 에러를 로그만 남기고 계속 진행
            e.printStackTrace();
        }
    }
}
