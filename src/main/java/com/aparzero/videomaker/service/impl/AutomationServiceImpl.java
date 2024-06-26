package com.aparzero.videomaker.service.impl;
import com.aparzero.videomaker.constant.RedditConstant;
import com.aparzero.videomaker.constant.VoiceConstant;
import com.aparzero.videomaker.domain.VideoResource;
import com.aparzero.videomaker.service.AutomationService;
import com.aparzero.videomaker.service.VoiceService;
import com.aparzero.videomaker.util.NameUtil;
import com.microsoft.playwright.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.nio.file.Paths;


import java.util.ArrayList;
import java.util.List;


@Service
public class AutomationServiceImpl implements AutomationService {


    private static final Logger LOG = LoggerFactory.getLogger(AutomationServiceImpl.class);

    private final VoiceService voiceService;

    public AutomationServiceImpl(final VoiceService voiceService) {
        this.voiceService = voiceService;
    }




    /**
     @param url the path of  the post from reddit
     @param name name of the post
     @param outputFolder destination on which the screenshot will be saved
     @param title the title of the post
     @return output screenshot and the audio from the post title text
     */

    @Override
    public VideoResource processPost(final String url,
                                     final String name,
                                     final String outputFolder,
                                     final String title) {

        LOG.info("PROCESSING POST: {}",url);


        try (Playwright playwright = Playwright.create()) {

            final Browser browser = playwright.webkit().launch(); // Assuming WebKit browser
            final Page page = browser.newPage();
            page.setViewportSize(10_000, 844);
            page.navigate(url);
            final String fileName = NameUtil.generateUniqueName();
            final String imageFolder = "images/".concat(fileName);
            final String imageOutput = outputFolder.concat(imageFolder).concat(".png");
            final Locator element = page.locator("#" + name);


            element.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(imageOutput)));

            page.close();
            browser.close();

            final String greeting = title.concat(VoiceConstant.FOLLOW_SCRIPT);

            final String voiceOutput  =  outputFolder.concat(fileName).concat(".mp3");
            voiceService.generateVoice(greeting,outputFolder,fileName.concat(".mp3"));
            LOG.info("Post Voice saved to: {}", voiceOutput);


            LOG.info("PROCESSING POST SUCCESS....");
            return new VideoResource(imageOutput,voiceOutput);

        }
    }

    /**
     @param postId  the id of the post
     @param url the path of  the post from reddit
     @param outputFolder destination on which the screenshot will be saved
     @return output screenshot and the audio from the post title text
     */
    @Override
    public  List<VideoResource> processComments(final String url,
                                                final String postId,
                                                final String outputFolder) {
        LOG.info("PROCESSING COMMENT: {}", postId);
        LOG.info("NAVIGATING URL: {}", url);
        final List<VideoResource> videoResources = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {
           final Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
           final Page page = browser.newPage();
           page.setViewportSize(1000, 844);
           page.navigate(url);

            final String selector = String.format(RedditConstant.COMMENT_SELECTOR, postId);
            final List<ElementHandle> comments = page.querySelectorAll(selector);
            LOG.info("LOCATOR SELECTOR: {}", selector);
            LOG.info("TOTAL COMMENTS GATHERED: {}", comments.size());


            final int commentLimit = Math.min(comments.size(), 5);


            for (int i = 0; i < commentLimit; i++) {
                try {
                    final ElementHandle comment = comments.get(i);
                    comment.scrollIntoViewIfNeeded();
                    Thread.sleep(1000); // Add a 1-second delay to ensure the element is fully loaded

                    final String fileName = (NameUtil.generateUniqueName());

                    final String imageFolder = "images/".concat(fileName);
                    final  String imageOutput = outputFolder.concat(imageFolder).concat(".png");


                    final boolean captureSuccess = captureScreenshot( comment,imageOutput); //screenshot comment

                    if (captureSuccess){
                        LOG.info("Screenshot saved to: {}", imageOutput);


                        final String commentContent = (String) comment.evaluate(RedditConstant.COMMENT_CONTENT_EXPRESSION);


                        LOG.info("Comment: {}", commentContent);

                        final String voiceOutput  =  outputFolder.concat(fileName).concat(".mp3");
                        voiceService.generateVoice(commentContent,outputFolder,fileName.concat(".mp3"));
                        LOG.info("Voice saved to: {}", voiceOutput);
                        videoResources.add(new VideoResource(imageOutput,voiceOutput));
                    }
                }
                catch (InterruptedException e) {
                    LOG.info("Exception: {}", e.getMessage());
                   }
            }

            page.close();
            browser.close();

            LOG.info("PROCESSING COMMENTS SUCCESS....");
        }
        return videoResources;
    }


    /**
     @param element the html element to be screenshotted
     @param outputFolder destination on which the screenshot will be saved
     @return  screenshot is successful or not
     */
    public boolean  captureScreenshot(final ElementHandle element,
                                      final String outputFolder) {

        LOG.info("SAVING TO : {}",outputFolder);
        boolean success = true;

        try {
            element.screenshot(new ElementHandle.ScreenshotOptions().setPath(Paths.get(outputFolder)));
            LOG.info("Saved to:  {}",outputFolder);
        } catch (Exception e) {
            success = false;
            LOG.error("Error capturing screenshot: {}", e.getMessage());
        }
        return success;
    }



}
