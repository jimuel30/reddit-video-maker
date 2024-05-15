package com.aparzero.videomaker.service.impl;


import com.aparzero.videomaker.constant.FFMpegConstant;
import com.aparzero.videomaker.domain.ProcessObject;
import com.aparzero.videomaker.domain.VideoResource;
import com.aparzero.videomaker.service.VideoService;
import com.aparzero.videomaker.util.FFMpegUtil;
import com.aparzero.videomaker.util.ProcessUtil;
import com.aparzero.videomaker.util.StringUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.Arrays;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {


    private final String BASE_VIDEO;


    public VideoServiceImpl(final @Value("${assets.movie}") String baseVideo) {
        BASE_VIDEO = baseVideo;
    }
    private static final Logger LOG = LoggerFactory.getLogger(VideoServiceImpl.class);



    //working command
    //ffmpeg -i src/main/resources/assets/screenshots/t3_1cou5s8-20240513192926/trim.mp4 -i src/main/resources/assets/screenshots/t3_1cou5s8-20240513192926/20240513192939.mp3 -i src/main/resources/assets/screenshots/t3_1cou5s8-20240513192926/20240513192939.png -filter_complex "[0:v]scale=trunc(iw*0.95):-1[img]; [img][2:v]overlay=(main_w-overlay_w)/2:(main_h-overlay_h)/2:enable='between(t,0,5)'" -c:v libx264 -c:a aac -strict experimental src/main/resources/assets/screenshots/t3_1cou5s8-20240513192926/output.mp4



    @Override
    public String createVideo(final List<VideoResource> videoResourceList,
                              final String outputFolder,
                              final String title) throws IOException, InterruptedException, UnsupportedAudioFileException, ParseException {
        // Get total audio length
        final double totalAudioLength = getTotalAudioLength(videoResourceList);
        final String formattedTitle = StringUtil.removeSpecialChar(title);



        // Trim base video (assuming you have a separate method for this)
        String trimmedVideoPath = trimBaseVideo(totalAudioLength, outputFolder);
        LOG.info("Trimmed video path: {}", trimmedVideoPath);

        // Output video path
        String outputVideoPath = outputFolder + "output.mp4";

        // Process each video resource
        int startTime = 0;

        for (int i = 0; i < videoResourceList.size(); i++) {

            final VideoResource videoResource = videoResourceList.get(i);

            LOG.info("Processing video: {}", i);

            // Get audio length
            final int audioLength = (int) Math.round(getAudioLength(videoResource.getAudioUrl()));



            final String previousVideoPath = i==0?trimmedVideoPath:outputFolder+"edited"+ (i-1) +".mp4";
            final String editedPath = i==videoResourceList.size()-1?
                                        outputFolder+formattedTitle+".mp4":
                                        outputFolder+"edited"+ i +".mp4";


            final String command = i == 0? FFMpegUtil.createCreateCommand(previousVideoPath,videoResource.getAudioUrl(),
                                           videoResource.getImageUrl(),startTime, startTime + audioLength,editedPath)
                                          :FFMpegUtil.createCreateCommandWithScale(previousVideoPath,videoResource.getAudioUrl(),
                                           videoResource.getImageUrl(),startTime, startTime + audioLength,editedPath);


            LOG.info("Command: {} ", command);
            try {
                final List<String> arguments = Arrays.asList(command.split(" ")); // Split the command string

                final ProcessBuilder processBuilder = new ProcessBuilder(arguments);
                final ProcessObject processObject = ProcessUtil.displayProcess(processBuilder);
                int exitCode = processObject.getProcess().waitFor();

                // Print logs
                String output = processObject.getOutputStream().toString();
                LOG.info("FFmpeg Output: {}", output);

                String error = processObject.getErrorStream().toString();
                if (!error.isEmpty()) {
                    LOG.error("FFmpeg Error: {}", error);
                }

                if (exitCode == 0) {
                    outputVideoPath = editedPath;
                } else {
                    throw new IOException("Failed to trim video. FFmpeg exit code: " + exitCode);
                }
            } catch (InterruptedException | IOException e) {
                throw new IOException("Error executing FFmpeg command: " + e.getMessage());
            }

            startTime += audioLength;
        }

        return outputVideoPath;
    }



    public String trimBaseVideo(final double audioLength,final String destination) throws IOException {
        final String trimmedVideoPath = destination .concat("trim.mp4");
        String ffmpegCommand = String.format(FFMpegConstant.TRIM_VIDEO_COMMAND, BASE_VIDEO, (int) Math.round(audioLength), trimmedVideoPath);
        LOG.info("Executing Trim command: {}", ffmpegCommand);
        try {
            final List<String> arguments = Arrays.asList(ffmpegCommand.split(" ")); // Split the command string

            final ProcessBuilder processBuilder = new ProcessBuilder(arguments);
            final ProcessObject processObject = ProcessUtil.displayProcess(processBuilder);
            int exitCode = processObject.getProcess().waitFor();

            String output = processObject.getOutputStream().toString();
            LOG.info("FFmpeg Output: {}", output);

            String error = processObject.getErrorStream().toString();
            if (!error.isEmpty()) {
                LOG.error("FFmpeg Error: {}", error);
            }

            if (exitCode == 0) {
                return trimmedVideoPath;
            } else {
                throw new IOException("Failed to trim video. FFmpeg exit code: " + exitCode);
            }

        } catch (InterruptedException | IOException e) {
            throw new IOException("Error executing FFmpeg command: " + e.getMessage());
        }
    }

    public double getAudioLength(final String filePath) throws IOException, InterruptedException, ParseException {
        LOG.info("Getting audio Length of: {}", filePath);

        // Build the ffprobe command
        final String command = FFMpegConstant.GET_AUDIO_LENGTH_COMMAND + filePath;
        LOG.info("Executing command: {}", command);

        // Execute the command and capture the output
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        reader.close();

        // Parse the JSON output to extract duration
        final JSONParser jsonParser = new JSONParser();
        final Object parsedObject = jsonParser.parse(String.valueOf(output));
        String jsonString = parsedObject.toString(); // convert to string
        LOG.info("MP3 Data: {}", jsonString);

        // Assuming the parsedObject is a JSONObject (verify!)
        final JSONObject format = (JSONObject) ((JSONObject) parsedObject).get("format");
        final String durationString = format.get("duration").toString();
        LOG.info("Duration: {}", durationString);
        return Double.parseDouble(durationString);
    }


    public double getTotalAudioLength(List<VideoResource> videoResourceList) throws IOException, ParseException, InterruptedException {

        double totalAudioLength = 0;
        for (VideoResource videoResource : videoResourceList) {
            totalAudioLength += getAudioLength(videoResource.getAudioUrl());
        }
        return totalAudioLength;

    }







 }


