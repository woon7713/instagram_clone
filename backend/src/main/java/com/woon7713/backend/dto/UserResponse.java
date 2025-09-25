package com.woon7713.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String profileImageUrl;
    private String bio;
    private Long followersCount;
    private Long followingCount;
    private boolean isFollowing;

    private String accessToken;
    private String refreshToken;
}