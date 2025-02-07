package com.example.newsService.core;

import org.springframework.web.multipart.MultipartFile;

public interface S3StorageService {
    public String upload(MultipartFile multipartFile);
    public void update(String linkToObject, byte[] data);
    public void delete(String linkToObject);
    public byte[] get(String linkToObject);
}
