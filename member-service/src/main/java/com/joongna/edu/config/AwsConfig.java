package com.joongna.edu.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class AwsConfig {

    @Value("${cloud.aws.region.static}")
    private String awsRegion;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(
        AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }
    @Bean
    public BasicAWSCredentials AwsCredentials() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return awsCreds;
    }

    @Primary
    @Bean(destroyMethod = "shutdown")
    public AmazonSQSAsync amazonSQSAsync() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(this.AwsCredentials()))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build();
    }



}
