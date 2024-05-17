package com.aparzero.videomaker.service.impl;


import com.aparzero.videomaker.domain.RedditData;
import com.aparzero.videomaker.domain.Response;
import com.aparzero.videomaker.domain.VideoResource;
import com.aparzero.videomaker.service.AutomationService;
import com.aparzero.videomaker.service.RedditService;
import com.aparzero.videomaker.service.VideoService;
import com.aparzero.videomaker.util.NameUtil;
import com.aparzero.videomaker.util.ResponseEntityUtil;
import com.aparzero.videomaker.util.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedditServiceImpl implements RedditService {

    private final RestTemplate restTemplate;


    private final String DOMAIN;

    private final AutomationService automationService;

    private final String SCREENSHOT_OUTPUT;

    private final VideoService  videoService;

    private static final Logger LOG = LoggerFactory.getLogger(RedditServiceImpl.class);

    public RedditServiceImpl(final RestTemplate restTemplate,
                             @Value("${reddit.domain}") final String domain,
                             final AutomationService automationService,
                             final @Value("${assets.screenshots-folder}") String screenShotOutput,
                             final VideoService videoService) {
        this.restTemplate = restTemplate;
        DOMAIN = domain;
        this.automationService = automationService;
        SCREENSHOT_OUTPUT = screenShotOutput;
        this.videoService = videoService;
    }



    @Override
    public ResponseEntity<Response> convertPostToVideo(final String postUrl) {

        final String url = postUrl+".json";
        LOG.info("POST URL: {}",url);
        ResponseEntity<Response> response;

        try {
            final ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            LOG.info("POST API Response: {}",responseEntity.getStatusCode());

            final JSONArray postChildren =(JSONArray) new JSONParser().parse(responseEntity.getBody());
            LOG.info("POST DATA: {}",postChildren.get(0));

            final JSONObject redditData = (JSONObject) postChildren.get(0);

            final JSONObject jsonData = (JSONObject) redditData.get("data");
            LOG.info("REDDIT DATA: {}",jsonData);

            final JSONArray children = (JSONArray) jsonData.get("children");

            final RedditData redditPost =   convertJsonObjectToRedditData((JSONObject) children.get(0));
            final String destination =  createDestinationPath(redditPost.getName());
            final String title = StringUtil.processText(redditPost.getTitle());
            final VideoResource  postResource= automationService.processPost(postUrl,redditPost.getName(),destination,title);
            LOG.info("Video resource: {}",postResource);
            final List<VideoResource> commentResources=  automationService.processComments(postUrl,redditPost.getId(),destination);
            LOG.info("Number of video resources: {}",commentResources.size());

            final List<VideoResource> arrangedVideoResources = arrangeVideoSources(postResource,commentResources);
            final String result = videoService.createVideo(arrangedVideoResources,destination,redditPost.getTitle());

            response = ResponseEntityUtil.successResponse("SUCCESS",result);


        }
        catch (HttpStatusCodeException e){
            LOG.info(e.getMessage());
            response = ResponseEntityUtil.fail(e.getMessage(), e.getStatusCode().value());
        }
        catch (InterruptedException e){
            LOG.info(e.getMessage());
            response = ResponseEntityUtil.fail(e.getMessage(),400);
        }
        catch (Exception e){
            //resource access
            //parse exception
            //generic
            LOG.info(e.getMessage());
            response = ResponseEntityUtil.fail(e.getMessage(), 500);
        }
        return response;
    }


    private List<VideoResource> arrangeVideoSources(VideoResource post, List<VideoResource> comments){
        final List<VideoResource> videoResources = new ArrayList<>(List.of(post));
        videoResources.addAll(comments);
        return videoResources;
    }


    private String createDestinationPath(final String name){
        final String folderName=name.concat("-").concat(NameUtil.generateUniqueName())+"/";
         return SCREENSHOT_OUTPUT.concat(folderName);
    }


    private RedditData convertJsonObjectToRedditData(final JSONObject post){
        final JSONObject actualPost = (JSONObject) post.get("data");
        final  String name = (String) actualPost.get("name");
        final String url = DOMAIN.concat((String) actualPost.get("permalink"));
        final String selfText = (String) actualPost.get("selftext");
        final String id = (String) actualPost.get("id");
        final String commentsUrl = (String) actualPost.get("url");
        final String title  = (String) actualPost.get("title");
        return new RedditData(url,name,selfText,id,commentsUrl,title);
    }

}
