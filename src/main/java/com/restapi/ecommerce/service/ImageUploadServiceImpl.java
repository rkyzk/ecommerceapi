package com.restapi.ecommerce.service;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


/**
 * image upload service implementation
 */
@Service
public class ImageUploadServiceImpl implements ImageUploadService {
	@Autowired
	private S3Client s3Client;

	@Value("${aws.s3.bucket.name}")
    private String bucketName;

	@Value("${aws.s3.storage.class}")
    private String storageClass;

	/**
	 * Upload file on AWS S3 bucket
	 * 
	 * @params s3Path
	 *         file
	 *
	 * @return
	 */
	public void uploadImage(String s3Path, MultipartFile file) {
        PutObjectRequest putObjRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path)
                .storageClass(storageClass)
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .build();
        byte[] bytes = null;
        try {
        	bytes = file.getBytes();
        	ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        	s3Client.putObject(putObjRequest,
            		RequestBody.fromInputStream(inputStream, bytes.length));
        } catch (Exception e){
        	e.printStackTrace();
        }
    }
}
