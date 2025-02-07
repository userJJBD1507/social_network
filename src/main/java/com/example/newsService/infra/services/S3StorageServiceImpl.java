package com.example.newsService.infra.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.newsService.core.S3StorageService;

import jakarta.annotation.PostConstruct;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3StorageServiceImpl implements S3StorageService {

@Value("${service.endpoint}")
    private String SERVICE_ENDPOINT;
    @Value("${region.name}")
    private String REGION_NAME;
    @Value("${access.key}")
    private String ACCESS_KEY;
    @Value("${secret.key}")
    private String SECRET_KEY;
    @Value("${bucket.name}")
    public String BUCKET_NAME;
    private AmazonS3 s3Client;

    @PostConstruct
    public void init() {
        this.s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(SERVICE_ENDPOINT, REGION_NAME))
                .withPathStyleAccessEnabled(true)
                .build();
    }
    public String upload(byte[] data) {
        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            String filename = "file" + UUID.randomUUID() + ".pdf";
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(data.length);
            s3Client.putObject(new PutObjectRequest(
                    BUCKET_NAME,
                    filename,
                    inputStream,
                    objectMetadata
            ));
            System.out.println("uploaded with name " + filename);
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }


    public String uploadWithExistingFilename(byte[] data, String filename) {
        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(data.length);
            s3Client.putObject(new PutObjectRequest(
                    BUCKET_NAME,
                    filename,
                    inputStream,
                    objectMetadata
            ));
            System.out.println("uploaded with name " + filename);
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public String uploadFileWithExistingFilename(MultipartFile file, String filename) {
        // try (InputStream inputStream = file.getInputStream()) {
        //     ObjectMetadata objectMetadata = new ObjectMetadata();
        //     objectMetadata.setContentLength(file.getSize());
        //     s3Client.putObject(new PutObjectRequest(
        //             BUCKET_NAME,
        //             filename,
        //             inputStream,
        //             objectMetadata
        //     ));
        //     return filename;
        // } catch (Exception e) {
        //     throw new RuntimeException("Failed to upload file", e);
        // }
        try {
            if (s3Client.doesObjectExist(BUCKET_NAME, filename)) {
                throw new RuntimeException("File with this name already exists");
            }
            try (InputStream inputStream = file.getInputStream()) {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(file.getSize());
                s3Client.putObject(new PutObjectRequest(
                        BUCKET_NAME,
                        filename,
                        inputStream,
                        objectMetadata
                ));
                return filename;
            }
    
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public String upload(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String filename = "file" + UUID.randomUUID();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            s3Client.putObject(new PutObjectRequest(
                    BUCKET_NAME,
                    filename,
                    inputStream,
                    objectMetadata
            ));
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public void update(String linkToObject, byte[] data) {
        try (InputStream inputStream = new ByteArrayInputStream(data)){
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(data.length);
            s3Client.putObject(new PutObjectRequest(
                    BUCKET_NAME,
                    linkToObject,
                    inputStream,
                    objectMetadata
            ));
            System.out.println("updated with name " + linkToObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String linkToObject) {
        s3Client.deleteObject(BUCKET_NAME, linkToObject);
        System.out.println("deleted with " + linkToObject);
    }

    @Override
    public byte[] get(String linkToObject) {
        try {
            S3Object s3Object = s3Client.getObject(BUCKET_NAME, linkToObject);
            try (InputStream inputStream = s3Object.getObjectContent()) {
                System.out.println("got");
                return inputStream.readAllBytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
