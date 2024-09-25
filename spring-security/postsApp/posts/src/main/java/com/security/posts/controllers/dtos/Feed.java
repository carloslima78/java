package com.security.posts.controllers.dtos;

import java.util.List;

public record Feed(List<FeedItem> feedItens,
                   int page,
                   int pageSize,
                   int totalPages,
                   long totalElements) {
}
