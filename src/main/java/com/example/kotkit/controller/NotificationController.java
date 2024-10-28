package com.example.kotkit.controller;

import com.example.kotkit.dto.response.DataResponse;
import com.example.kotkit.entity.Notification;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class NotificationController {
    @Operation(summary = "Get all notifications of current user")
    @GetMapping("notifications")
    public DataResponse<List<Notification>> getNotifications() {
        return null;
    }
}
