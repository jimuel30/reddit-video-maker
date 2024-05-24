package com.aparzero.videomaker.service;

import com.aparzero.videomaker.domain.Response;
import com.aparzero.videomaker.model.RedditVideo;
import org.springframework.http.ResponseEntity;

public interface RedditService {


    ResponseEntity<Response>  processPost(String post);
    ResponseEntity<Response> getRedditVideo(long videoId);
}
