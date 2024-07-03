package com.aparzero.videomaker.service;

import com.aparzero.videomaker.model.RedditVideo;

public interface NotificationService {

    void sendNotification(RedditVideo redditVideo);
}
