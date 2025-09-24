package com.woon7713.backend.service;

import com.woon7713.backend.entity.Like;
import com.woon7713.backend.entity.Post;
import com.woon7713.backend.entity.User;
import com.woon7713.backend.exception.BadRequestException;
import com.woon7713.backend.repository.LikeRepository;
import com.woon7713.backend.repository.PostRepository;
import com.woon7713.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public boolean toggleLike(Long postId) {
        User currentUser = authenticationService.getCurrentUser();

        Post post = postRepository.findByIdAndNotDeleted(postId)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        boolean alreadyLiked = likeRepository.existsByUserAndPost(currentUser, post);

        if (alreadyLiked) {
            likeRepository.deleteByUserAndPost(currentUser, post);
            return false;
        } else {
            Like like = Like.builder()
                    .user(currentUser)
                    .post(post)
                    .build();
            likeRepository.save(like);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Transactional(readOnly = true)
    public boolean isLikedByCurrentUser(Long postId) {
        User currentUser = authenticationService.getCurrentUser();

        Post post = postRepository.findByIdAndNotDeleted(postId)
                .orElseThrow(() -> new BadRequestException("Post not found"));

        return likeRepository.existsByUserAndPost(currentUser, post);
    }





}