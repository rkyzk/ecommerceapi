package com.restapi.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


/** 
 * Generate AWSCrendetials object.
 * 
 * @author R.Yazaki
 * @version 1.0.0
 */
@Configuration
public class AwsConfig {	
	@Value("${aws.access.key.id}")
	private String accessKey;

	@Value("${aws.secret.access.key}")
	private String secretKey;

	@Value("${aws.s3.region}")
	private String region;

	@Bean
	public AmazonS3 getAwsCredentials() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		return AmazonS3ClientBuilder
				.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.build();
	}
}