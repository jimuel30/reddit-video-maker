package com.aparzero.videomaker.service;

import com.aparzero.videomaker.domain.VideoResource;
import org.json.simple.parser.ParseException;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;

public interface VideoService {

    String createVideo(List<VideoResource> videoResourceList, String outputFolder, String title) throws UnsupportedAudioFileException, IOException, ParseException, InterruptedException;
}
