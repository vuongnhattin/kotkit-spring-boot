package com.example.kotkit.controller;

import com.example.kotkit.dto.response.ChatResponse;
import com.example.kotkit.dto.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
public class ChatController {
    @Operation(summary = "Get all chat messages between main user and another user")
    @GetMapping("/api/chats/{room_id}")
    public DataResponse<List<ChatResponse>> getChatMessages(@PathVariable int room_id) {
        return null;
    }

    @Operation(summary = "Send a chat message from main user to another user")
    @PostMapping("/api/chats/{room_id}")
    public DataResponse<ChatResponse> sendMessage(@PathVariable int room_id) {
        return null;
    }
}
