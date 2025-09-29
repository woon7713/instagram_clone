package com.woon7713.backend.controller;

import com.woon7713.backend.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class S3Controller {
    private final S3Service s3Service;
    @PostMapping("/post")
    public ResponseEntity<Map<String, String>> uploadPostImage(@RequestParam("file") MultipartFile file) {
        String url = s3Service.uploadFile(file, "w7/post");
        return ResponseEntity.ok(Map.of("url", url));
    }

    @PostMapping("/profile")
    public ResponseEntity<Map<String, String>> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        String url = s3Service.uploadFile(file, "h662/profile");
        return ResponseEntity.ok(Map.of("url", url));
    }
}