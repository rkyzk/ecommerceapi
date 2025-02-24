package com.restapi.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImgUploadService {
	public String uploadImg(MultipartFile multipartFile, String folder, String origFileName);
	public boolean deleteImg(final String fileName);
}
