package com.aparzero.videomaker.service.impl;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.aparzero.videomaker.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;

    private final String BUCKET;


    private static final Logger LOG = LoggerFactory.getLogger(S3ServiceImpl.class);

    public S3ServiceImpl(final AmazonS3 amazonS3,
                         @Value("${aws.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        BUCKET = bucket;
    }

    @Override
    public String saveToS3(final String fileName) throws IOException {

            File file = new File(fileName);

        if (!file.exists()) {
            LOG.error("File not found: {}", fileName);
            throw new FileNotFoundException("File not found: " + fileName);
        }

        // Upload the file to S3
        PutObjectRequest request = new PutObjectRequest(BUCKET, fileName, file);

        amazonS3.putObject(request);

        LOG.info("File uploaded to S3: {}", fileName);

        // Return the S3 object URL (optional)
        return amazonS3.getUrl(BUCKET, fileName).toString();
    }



}
