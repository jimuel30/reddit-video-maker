package com.aparzero.videomaker.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {


    private final String ACCESS_KEY;

    private final String KEY_ID;


    public ApplicationConfig (@Value("${aws.access-key}") final String accessKey,
                     @Value("${aws.key-id}") final String keyId) {
        ACCESS_KEY = accessKey;
        KEY_ID = keyId;
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(){
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(KEY_ID,ACCESS_KEY));
    }

    @Bean
    public AmazonPolly amazonPolly() {
        return AmazonPollyClientBuilder.standard()
                .withRegion("us-east-1") // Replace with your desired AWS region (e.g., Regions.US_EAST_1)
                .withCredentials(awsCredentialsProvider())
                .build();
    }
}
