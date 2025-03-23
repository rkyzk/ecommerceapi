package com.restapi.ecommerce.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

/*
 * Upload and delete files on AWS S3 bucket.
 */
@Service
public class ImgUploadServiceImpl implements ImgUploadService {
	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket.name}")
    private String bucketName;

	/*
	 * Upload file on AWS S3 bucket
	 * 
	 * @params multipartFile
	 *         folder
	 *         fileName
	 * @return return code
	 */
	public String uploadImg(MultipartFile multipartFile,
			String folder, String origFileName) {
		LocalDateTime currTime = LocalDateTime.now();
		String fileName = origFileName +
				currTime.toString()
		            .replace(" ", "-").replace(":", "");
		String filePath = folder + "/" + fileName;
		try {
			File file = convertMultipartFileToFile(multipartFile);
			// upload file
			amazonS3.putObject(bucketName, filePath, file);
			file.delete();
		} catch (Exception e){
			return null;
		}
		return filePath;
	}
	
	/*
	 * Delete file from AWS S3 bucket
	 * 
	 * @params fileName
	 * @return return code
	 */
	public boolean deleteImg(final String fileName) {
		final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
	    try {
	    	amazonS3.deleteObject(deleteObjectRequest);
	    } catch (AmazonServiceException e) {
            // Amazon S3 exception
            e.printStackTrace();
            return false;
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
            return false;
        }
	    return true;
	}

	/*
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
