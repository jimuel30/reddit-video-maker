package com.aparzero.videomaker.service;

import java.io.IOException;

public interface S3Service {

    String saveToS3(String videoPath) throws IOException;
}

