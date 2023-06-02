package com.gxdxx.instagram.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gxdxx.instagram.exception.FileProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new FileProcessingException());
        String uploadImageUrl = putS3(uploadFile, dirName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String dirName) {
        String fileName = generateFileName(uploadFile.getName(), dirName);
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));// PublicRead 권한으로 업로드 됨
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String generateFileName(String originalFileName, String dirName) {
        String ext = extractExtension(originalFileName);
        return dirName + "/" + UUID.randomUUID() + "." + ext;
    }

    private String extractExtension(String originalFileName) {
        int index = originalFileName.lastIndexOf(".");
        return originalFileName.substring(index + 1);
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());
        try {
            if (convertFile.createNewFile()) {
                file.transferTo(convertFile);
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new FileProcessingException();
        }
        return Optional.empty();
    }

}
