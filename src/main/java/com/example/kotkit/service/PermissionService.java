package com.example.kotkit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("perm")
@RequiredArgsConstructor
public class PermissionService {
    private final FriendshipService friendshipService;
    private final UserService userService;
    private final VideoService videoService;
    private final MinioService minioService;
}
