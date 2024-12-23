package com.example.kotkit.service;

import com.example.kotkit.dto.input.VideoInput;
import com.example.kotkit.dto.response.VideoDataResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.Video;
import com.example.kotkit.repository.VideoRepository;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;

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

    @Value("${minio.endpoint}")
    private String endpoint;

    private final ModelMapper mapper;

    public VideoResponse uploadVideo(MultipartFile file, VideoInput input) {
        try {
            // Kiểm tra bucket có tồn tại không
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            // Tạo bucket nếu chưa tồn tại
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }

            // Tạo tên file ngẫu nhiên để tránh trùng lặp
            String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());

            // Upload file lên MinIO
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            String fileUrl = endpoint + "/" + bucketName + "/" + fileName;

            Video video = mapper.map(input, Video.class);

            video.setVideoUrl(fileUrl);

            return videoService.uploadVideo(video);

        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new RuntimeException("Could not upload file to MinIO", e);
        }
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
