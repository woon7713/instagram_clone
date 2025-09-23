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

//    @GetMapping("/presign")
//    public ResponseEntity<String> getPresignedUrl(@RequestParam String url) {
//        String presignedUrl = s3Service.generatePresignedUrl(url, 1);
//        return ResponseEntity.ok(presignedUrl);
//    }
}