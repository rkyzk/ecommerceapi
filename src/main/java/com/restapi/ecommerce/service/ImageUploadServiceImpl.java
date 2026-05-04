package com.restapi.ecommerce.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
	 * @params multipartFile
	 *         folder
	 *         fileName
	 * @return return code
	 */
//	public String uploadImage(MultipartFile multipartFile,
//			String folder, String origFileName) {
//		LocalDateTime currTime = LocalDateTime.now();
//		String fileName = origFileName +
//				currTime.toString()
//		            .replace(" ", "-").replace(":", "");
//		String filePath = folder + "/" + fileName;
//		try {
//			File file = convertMultipartFileToFile(multipartFile);
//			// upload file
//			amazonS3.putObject(bucketName, filePath, file);
//			file.delete();
//		} catch (Exception e){
//			return null;
//		}
//		return filePath;
//	}

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
	
	
	/**
	 * Delete file from AWS S3 bucket
	 * 
	 * @params fileName
	 * @return return code
	 */
//	public boolean deleteImage(final String fileName) {
//		final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
//	    try {
//	    	amazonS3.deleteObject(deleteObjectRequest);
//	    } catch (AmazonServiceException e) {
//            // Amazon S3 exception
//            e.printStackTrace();
//            return false;
//        } catch (SdkClientException e) {
//            // Amazon S3 couldn't be contacted for a response, or the client
//            // couldn't parse the response from Amazon S3.
//            e.printStackTrace();
//            return false;
//        }
//	    return true;
//	}

	/**
	 * Convert multipart file to file.
	 * 
	 * @params file: multipart file
	 * @return file
	 */
	private File convertMultipartFileToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertedFile);
		fos.write(file.getBytes());
		fos.close();		
		return convertedFile;
	}
}
