package com.foodzy.web.az.amazon.s3.service;


import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link Service} for communicating with Amazon S3 services.
 *
 * @author Foodzy
 * @since 1.0.0
 */
@Service
public class AmazonS3Service {

    private static final Logger logger = LoggerFactory.getLogger(AmazonS3Service.class);

    private static final List<String> SUPPORTED_PROFILES = Arrays.asList("local", "staging", "production");

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${application.amazon.s3.bucket-name}")
    private String bucketName;

    @Value("${application.amazon.s3.access-key}")
    private String accessKey;

    @Value("${application.amazon.s3.secret-key}")
    private String secretKey;

    @Value("${application.amazon.s3.region}")
    private String region;

    private AmazonS3 s3client;

    @PostConstruct
    private void onPostConstruct() {
        logger.info("onPostConstruct...");
        logger.info(String.format("+ Active Profile: %s", this.activeProfile));
        logger.info(String.format("+ Bucket: %s", this.bucketName));
        logger.info(String.format("+ Access Key: %s", this.accessKey));
        logger.info(String.format("+ Secret Key: %s", this.secretKey));
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    /**
     * Upload to Amazon S3 bucket using some {@link InputStream}, given the path segments to the file,
     * and the file name itself.
     *
     * @param pathSegments   The list of path segments
     * @param fileName       The file name
     * @param inputStream    The {@link InputStream} to upload
     * @param objectMetadata The {@link ObjectMetadata} to describe the meta data of the file
     * @return
     * @throws IllegalArgumentException
     */
    public String upload(
            List<String> pathSegments,
            String fileName,
            InputStream inputStream,
            ObjectMetadata objectMetadata)
            throws IllegalArgumentException {
        String filePath = this.buildFilePath(pathSegments, fileName);
        logger.info(String.format("Uploading object from file path: %s", filePath));
        if (Objects.isNull(inputStream))
            throw new IllegalArgumentException("Failed to upload file (reason: invalid input stream).");
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                this.bucketName,
                filePath,
                inputStream,
                Optional.ofNullable(objectMetadata).orElseGet(ObjectMetadata::new));
        PutObjectResult putObjectResult = this.s3client.putObject(putObjectRequest);
        if (Objects.nonNull(putObjectResult)) {
            URL fileUrl = this.s3client.getUrl(this.bucketName, filePath);
            logger.info("Uploaded successfully...");
            logger.info(String.format("\t- File MD5: %s", putObjectResult.getContentMd5()));
            logger.info(String.format("\t- File URL: %s", fileUrl.toString()));
            logger.info(String.format("Original File URL: %s", fileUrl.toString()));
            return UriComponentsBuilder.fromHttpUrl("https://static.adfoodzy.com")
                    .path(filePath)
                    .build().encode().toUri().toString();
        } else {
            logger.error("Failed to upload...");
            return null;
        }
    }

    /**
     * Delete file from Amazon S3 bucket, given the path segments to the file, and the file name itself.
     *
     * @param pathSegments The list of path segments
     * @param fileName     The file name
     * @throws IllegalArgumentException
     * @throws SdkClientException
     */
    public void delete(List<String> pathSegments, String fileName)
            throws IllegalArgumentException, SdkClientException {
        String filePath = this.buildFilePath(pathSegments, fileName);
        logger.info(String.format("Deleting object from file path: %s", filePath));
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucketName, filePath);
        this.s3client.deleteObject(deleteObjectRequest);
        logger.info("Deleted successfully");
    }

    /**
     * Convenience method to build file path from path segments and file name.
     *
     * @param pathSegments The list of path segments
     * @param fileName     The file name
     * @return
     * @throws IllegalArgumentException
     */
    private String buildFilePath(List<String> pathSegments, String fileName) throws IllegalArgumentException {
        if (StringUtils.isBlank(this.activeProfile))
            throw new IllegalArgumentException("Failed to build file path (reason: invalid active profile).");
        if (!SUPPORTED_PROFILES.contains(this.activeProfile))
            throw new IllegalArgumentException("Failed to build file path (reason: unsupported active profile).");
        if (StringUtils.isBlank(fileName))
            throw new IllegalArgumentException("Failed to build file path (reason: invalid file name).");
        if (Objects.nonNull(pathSegments)) {
            String tmpPath = pathSegments.stream()
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("/"));
            return StringUtils.isNotBlank(tmpPath) ?
                    String.format("%s/%s/%s", this.activeProfile, tmpPath, fileName) :
                    String.format("%s/%s", this.activeProfile, fileName);
        }
        return String.format("%s/%s", this.activeProfile, fileName);
    }

    @Deprecated
    public String uploadFile(MultipartFile multipartFile) throws RestClientException {
        try {
            String fileName = generateFileName(multipartFile);
            logger.info("Uploading file to s3 :: " + fileName);
            this.s3client.putObject(new PutObjectRequest(this.bucketName, fileName, convertMultiPartToFile(multipartFile))
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            URL fileUrl = this.s3client.getUrl(this.bucketName, fileName);
            logger.info("Upload file url :: " + fileUrl.toString());
            return fileUrl.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestClientException(e.getMessage());
        }
    }

    @Deprecated
    public void deleteFileFromS3Bucket(String fileUrl) throws RestClientException {
        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            logger.info("Deleting file from s3 :: " + fileName);
            this.s3client.deleteObject(new DeleteObjectRequest(this.bucketName, fileName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestClientException(e.getMessage());
        }
    }

    @Deprecated
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}
