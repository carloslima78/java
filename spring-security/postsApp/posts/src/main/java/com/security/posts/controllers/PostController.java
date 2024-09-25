package com.security.posts.controllers;

import com.security.posts.controllers.dtos.CreatePost;
import com.security.posts.controllers.dtos.Feed;
import com.security.posts.controllers.dtos.FeedItem;
import com.security.posts.entities.Post;
import com.security.posts.repositories.ClientRepository;
import com.security.posts.repositories.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class PostController {

    private final PostRepository postRepository;

    private final ClientRepository clientRepository;

    public PostController(PostRepository postRepository, ClientRepository clientRepository) {
        this.postRepository = postRepository;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestBody CreatePost createPost,
                                           JwtAuthenticationToken token){

        var client = this.clientRepository.findById(UUID.fromString(token.getName()));

        var post = new Post();
        post.setClient(client.get());
        post.setContent(createPost.content());

        this.postRepository.save(post);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           JwtAuthenticationToken token){

        var client = this.clientRepository.findById(UUID.fromString(token.getName()));

        var post = this.postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (post.getClient().getClientId().equals(UUID.fromString(token.getName()))){

            this.postRepository.deleteById(postId);
        }else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<Feed> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){

        var posts = this.postRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "timestamp"))
                .map(post ->
                        new FeedItem(post.getPostId(), post.getContent(), post.getClient().getName())
                );

        return ResponseEntity.ok(new Feed(
                posts.getContent(), page, pageSize, posts.getTotalPages(), posts.getTotalElements()));
    }
}
