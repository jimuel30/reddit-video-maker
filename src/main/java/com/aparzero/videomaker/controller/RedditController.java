package com.aparzero.videomaker.controller;

import com.aparzero.videomaker.domain.Response;
import com.aparzero.videomaker.service.RedditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedditController {
    private final RedditService redditService;


    public RedditController(final RedditService redditService) {
        this.redditService = redditService;
    }

    @GetMapping("/generate")
    public ResponseEntity<Response> generateVideo(@RequestParam String subreddit){
        return redditService.convertSubredditPostsToVideo(subreddit);
    }

    @GetMapping("/convert")
    public ResponseEntity<Response> convertToVideo(@RequestParam String url){
        return redditService.convertPostToVideo(url);
    }
}
