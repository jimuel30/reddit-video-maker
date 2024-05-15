package com.aparzero.videomaker.service;

import com.aparzero.videomaker.domain.VideoResource;

import java.util.List;

public interface AutomationService {
    VideoResource processPost(String url, String name, String outputFolder, String title);
    List<VideoResource> processComments(String url, String postId, String outputFolder) throws InterruptedException;
}
