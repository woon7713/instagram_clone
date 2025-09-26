package com.woon7713.backend.service;



import com.woon7713.backend.dto.PostResponse;
import com.woon7713.backend.entity.Bookmark;
import com.woon7713.backend.entity.Post;
import com.woon7713.backend.entity.User;
import com.woon7713.backend.exception.ResourceNotFoundException;
import com.woon7713.backend.repository.BookmarkRepository;
import com.woon7713.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;

@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final AuthenticationService authenticationService;

    public boolean toggleBookmark(Long postId) {
        User currentUser = authenticationService.getCurrentUser();
        Post post = postRepository.findByIdAndNotDeleted(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        boolean alreadyBookmarked = bookmarkRepository.existsByUserAndPost(currentUser, post);

        if (alreadyBookmarked) {
            bookmarkRepository.deleteByUserAndPost(currentUser, post);
            return false;
        } else {
            Bookmark bookmark = Bookmark.builder()
                    .user(currentUser)
                    .post(post)
                    .build();
            bookmarkRepository.save(bookmark);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public boolean isBookmarked(Long postId) {
        User currentUser = authenticationService.getCurrentUser();
        Post post = postRepository.findByIdAndNotDeleted(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        return bookmarkRepository.existsByUserAndPost(currentUser, post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getBookmarkedPosts(Pageable pageable) {
        User currentUser = authenticationService.getCurrentUser();
        Page<Post> bookmarkedPosts = bookmarkRepository.findBookmarkedPostsByUser(currentUser, pageable);

        return bookmarkedPosts.map(PostResponse::fromEntity);
    }
}