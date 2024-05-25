package com.aparzero.videomaker.service.impl;



import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.IOUtils;
import com.aparzero.videomaker.service.S3Service;
import com.aparzero.videomaker.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.URL;

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


    /**
     @param fileUrl the path of  the video to be uploaded
     @return url of the uploaded file to S3
     */
    @Override
    public String saveToS3(final String fileUrl) throws IOException {
        final String objectKey = StringUtil.generateUniqueKey(fileUrl);
        LOG.info("Object Key: {}", objectKey);
        LOG.info("File Url: {}", fileUrl);

        try {
            // Create a PutObjectRequest
            final PutObjectRequest request = new PutObjectRequest(BUCKET, objectKey, new File(fileUrl));

            // Upload the file
            final PutObjectResult result = amazonS3.putObject(request);

            // Log the ETag of the uploaded object
            LOG.info("ETag: {}", result.getETag());


        } catch (AmazonServiceException e) {
            LOG.error("Error uploading file to S3: {}", e.getMessage());
            throw new IOException("Error uploading file to S3", e);
        }

        // Return the object key
        final URL url = amazonS3.getUrl(BUCKET, objectKey);
        return url.toString();
    }


}
