package com.woon7713.backend.controller;

import com.woon7713.backend.dto.PostRequest;
import com.woon7713.backend.dto.PostResponse;
import com.woon7713.backend.entity.Like;
import com.woon7713.backend.service.LikeService;
import com.woon7713.backend.service.PostService;
import com.woon7713.backend.service.S3Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final S3Service s3Service;
    private final LikeService likeService;

    private static final int EXPIRATION_MINUTES = 60;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest request) {
        PostResponse response = postService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponse> posts = postService.getUserPosts(userId, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> getUserPostCount(@PathVariable Long userId) {
        Long count = postService.getUserPostCount(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest request
    ) {
        PostResponse response = postService.updatePost(postId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/image")
    public ResponseEntity<Map<String, String>> getPresignedUrl(@RequestParam String url) {
        String imageUrl = s3Service.generatePresignedUrl(url, EXPIRATION_MINUTES);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long postId) {
        boolean isLiked = likeService.toggleLike(postId);
        Long likeCount = likeService.getLikeCount(postId);

        return ResponseEntity.ok().body(Map.of(
                "isLiked", isLiked,
                "likeCount", likeCount
        ));
    }
}