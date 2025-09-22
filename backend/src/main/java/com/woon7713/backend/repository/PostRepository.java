package com.woon7713.backend.repository;


import com.woon7713.backend.entity.Post;
import com.woon7713.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT p FROM Post p WHERE p.deleted = false ORDER BY p.createdAt DESC")
    Page<Post> findAllActive(Pageable pageable);


    // AND p.deleted = false 로 삭제되었나 체크를 함으로써 soft delete 구현 (delete 상태 여부로 필터링 방식)
    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT p FROM Post p WHERE p.user = :user AND p.deleted = false ORDER BY p.createdAt DESC")
    Page<Post> findByUserAndNotDeleted(@Param("user") User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT p FROM Post p WHERE p.user.id = :userId AND p.deleted = false ORDER BY p.createdAt DESC")
    Page<Post> findByUserIdAndNotDeleted(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.id = :id AND p.deleted = false")
    Optional<Post> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT COUNT(p) FROM Post p WHERE p.user = :user AND p.deleted = false")
    long countByUserAndNotDeleted(@Param("user") User user);



}