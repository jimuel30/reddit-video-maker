package com.aparzero.videomaker.util;

import com.aparzero.videomaker.domain.ProcessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessUtil.class);


    public static ProcessObject displayProcess(final ProcessBuilder processBuilder) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
        final Process process = processBuilder.start();

        // Capture output and error streams
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputStream.write((line + System.lineSeparator()).getBytes());
                }
            } catch (IOException e) {
                LOG.info("Input Stream Error: {}", e.getMessage());
            }
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    errorStream.write((line + System.lineSeparator()).getBytes());
                }
            } catch (IOException e) {
                LOG.info("Error stream Error: {}", e.getMessage());
            }
        }).start();

        return new ProcessObject(process,outputStream,errorStream);
    }
}
