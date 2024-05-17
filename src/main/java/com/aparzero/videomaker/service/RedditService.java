package com.aparzero.videomaker.service;

import com.aparzero.videomaker.domain.Response;
import org.springframework.http.ResponseEntity;

public interface RedditService {
    ResponseEntity<Response> convertPostToVideo(String post);
}
