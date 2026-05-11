package com.restapi.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

/** image upload service */
public interface ImageUploadService {
	public void uploadImage(String folder, MultipartFile multipartFile);
}
