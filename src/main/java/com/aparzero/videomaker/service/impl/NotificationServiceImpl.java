package com.aparzero.videomaker.service.impl;

import com.aparzero.videomaker.model.RedditVideo;
import com.aparzero.videomaker.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class NotificationServiceImpl implements NotificationService {


    private final String URL;

    private final RestTemplate restTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);


    public NotificationServiceImpl(@Value("${ws-broker.notify-url}") final String URL, final RestTemplate restTemplate) {
        this.URL = URL;
        this.restTemplate = restTemplate;
    }




    @Override
    public  void sendNotification(RedditVideo redditVideo){
        LOG.info("Sending notification");
        try {
            final HttpEntity httpEntity = new HttpEntity<>(redditVideo);
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class);
            LOG.info("RESULT: {}",response);
        }
        catch (Exception e){
            LOG.error("ERROR DETAILS: {}", e.getMessage());
        }
    }
}
