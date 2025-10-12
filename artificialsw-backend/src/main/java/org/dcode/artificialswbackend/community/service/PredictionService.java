package org.dcode.artificialswbackend.community.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionService {
    
    private final RestTemplate restTemplate;
    
    @Value("${prediction.api.url:http://3.39.185.117:4000/predict}")
    private String predictionApiUrl;

    public PredictionService() {
        this.restTemplate = new RestTemplate();
    }

    public String sendPredictionRequest(String questionContent) {
        try {
            // 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 요청 바디 생성
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", questionContent);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            // POST 요청 전송
            ResponseEntity<String> response = restTemplate.postForEntity(
                predictionApiUrl,
                request,
                String.class
            );

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error calling prediction API: " + e.getMessage());
            return null;
        }
    }
}