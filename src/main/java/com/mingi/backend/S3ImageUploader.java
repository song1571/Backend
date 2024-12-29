package com.mingi.backend;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class S3ImageUploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    public String uploadImage(MultipartFile image) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        try (InputStream inputStream = image.getInputStream()){

            // 학장자를 분리하고 UUID로 파일 이름을 랜덤으로 변경
            String fileExtension = getFileExtension(image.getOriginalFilename());
            String randomName = UUID.randomUUID().toString().substring(0, 30) + fileExtension;

            // 오브젝트메타 데이터 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(image.getContentType());
            metadata.setContentLength(image.getSize());

            // S3 버킷에 이미지 업로드
            s3Client.putObject(new PutObjectRequest(bucket, randomName, inputStream, metadata));
            
            return s3Client.getUrl(bucket, randomName).toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 파일 확장자를 추출하는 코드
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(dotIndex); // 확장자 포함 (jpg, png...)
        } else {
            return "";
        }
    }
}
