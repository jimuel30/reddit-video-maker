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


    private final String SECRET;

    private final String KEY;


    public ApplicationConfig (@Value("${aws.secret}") final String secret,
                     @Value("${aws.key}") final String key) {
        SECRET = secret;
        KEY = key;
    }


    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public AWSCredentialsProvider awsCredentialsProvider(){
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(KEY,SECRET));
    }

    @Bean
    public AmazonPolly amazonPolly() {
        return AmazonPollyClientBuilder.standard()
                .withRegion("us-east-1") // Replace with your desired AWS region (e.g., Regions.US_EAST_1)
                .withCredentials(awsCredentialsProvider())
                .build();
    }
}
