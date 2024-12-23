package com.example.kotkit.service;

import com.example.kotkit.exception.AppException;
import com.example.kotkit.dto.input.VideoInput;
import com.example.kotkit.dto.response.VideoDataResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.Video;
import com.example.kotkit.repository.VideoRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;

import java.util.Objects;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;
    private final VideoService videoService;
    private final VideoRepository videoRepository;

    @Value("${minio.bucket}")
    private String bucketName;

    @PostConstruct
    private void init() {
        createBucket(bucketName);
    }

    @SneakyThrows
    private void createBucket(final String name) {
        final var found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(name)
                        .build()
        );
        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(name)
                            .build()
            );

            final var policy = """
                        {
                          "Version": "2012-10-17",
                          "Statement": [
                           {
                              "Effect": "Allow",
                              "Principal": "*",
                              "Action": "s3:GetObject",
                              "Resource": "arn:aws:s3:::%s/*"
                            }
                          ]
                        }
                    """.formatted(name);
            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(name).config(policy).build()
            );
        } else {
            log.info("Bucket %s already exists.".formatted(name));
        }
    }

    @SneakyThrows
    public String upload(@NonNull final MultipartFile file) {
        final String contentType = file.getContentType();

        String folderName = contentType.substring(0, contentType.lastIndexOf("/"));
        String fileFormat = contentType.substring(contentType.lastIndexOf("/") + 1);

        if (folderName.equals("video") && fileFormat.equals("x-matroska")) {
            fileFormat = "mp4";
        }

        final var fileName = folderName + "/" + UUID.randomUUID() + "." + fileFormat;

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .contentType(Objects.isNull(file.getContentType()) ? "video/mp4;" : file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
        } catch (Exception ex) {
            log.error("Error saving file \n {} ", ex.getMessage());
            throw new AppException(500, "Unable to upload file");
        }

        final var videoUrl = "/api/v1/buckets/kotkit/objects/download?prefix=%s&version_id=null".formatted(fileName);

        return videoUrl;
    }

    public VideoDataResponse getVideo(Integer videoId) {
        Video video = videoRepository.findById(videoId).orElse(null);

        if (video == null) {
            throw new RuntimeException("Video not found");
        }

        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(getFileName(video.getVideoUrl()))
                            .build()
            );

            byte[] data = response.readAllBytes();

            response.close();
            return VideoDataResponse.builder()
                    .videoData(data)
                    .videoUrl(video.getVideoUrl())
                    .build();

        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get video", e);
        }
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/"));
    }
}
