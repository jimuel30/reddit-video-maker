package com.aparzero.videomaker.controller;

import com.aparzero.videomaker.domain.Response;
import com.aparzero.videomaker.service.RedditService;
import com.aparzero.videomaker.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RedditController {
    private final RedditService redditService;

    private final S3Service s3Service;


    public RedditController(final RedditService redditService, S3Service s3Service) {
        this.redditService = redditService;
        this.s3Service = s3Service;
    }

    @GetMapping("/convert")
    public ResponseEntity<Response> convertToVideo(@RequestParam String url){
        return redditService.processPost(url);
    }


    @GetMapping("/get")
    public ResponseEntity<Response> getVideo(@RequestParam long videoId){
        return redditService.getRedditVideo(videoId);
    }

    @GetMapping("/test")
    public String tests3(@RequestParam String videoPath) throws IOException {
        return s3Service.saveToS3(videoPath);
    }
}
