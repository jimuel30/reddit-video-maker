package com.aparzero.videomaker.service.impl;


import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.util.IOUtils;
import com.aparzero.videomaker.constant.VoiceConstant;
import com.aparzero.videomaker.service.VoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class VoiceServiceImpl implements VoiceService {


    private static final Logger LOG = LoggerFactory.getLogger(VoiceServiceImpl.class);

    private final AmazonPolly amazonPolly;

    public VoiceServiceImpl(final AmazonPolly amazonPolly) {
        this.amazonPolly = amazonPolly;
    }


    @Override
    public void generateVoice(final String text, final String outputFolder, final String fileName) {

        LOG.info("GENERATING AUDIO: ");



        final SynthesizeSpeechRequest synthesizeSpeechRequest = new SynthesizeSpeechRequest()
                .withText(text)
                .withTextType("text")
                .withVoiceId(VoiceConstant.VOICE_ID)
                .withOutputFormat("mp3"); // Specify the desired output format (e.g., mp3, pcm)


        final SynthesizeSpeechResult synthesizeSpeechResult = amazonPolly.synthesizeSpeech(synthesizeSpeechRequest);

        // Save the audio to a file
        final File audioFile = new File(outputFolder,fileName); // Replace "output.mp3" with your desired filename
        try (FileOutputStream outputStream = new FileOutputStream(audioFile)) {
            IOUtils.copy(synthesizeSpeechResult.getAudioStream(), outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.info("Audio generated successfully: {}", audioFile.getAbsolutePath());

    }

}
