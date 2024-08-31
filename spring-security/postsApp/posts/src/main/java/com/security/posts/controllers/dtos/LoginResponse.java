package com.security.posts.controllers.dtos;

public record LoginResponse(String accessToken, Long expiresIn) {
}
