package com.klearn.controller;

import com.klearn.dto.ApiResponse;
import com.klearn.dto.SpeakingRoomParticipantDto;
import com.klearn.model.SpeakingRoom;
import com.klearn.model.User;
import com.klearn.security.UserDetailsImpl;
import com.klearn.service.SpeakingRoomService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/speaking-rooms")
@RequiredArgsConstructor
public class SpeakingRoomApiController {

    private final SpeakingRoomService speakingRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpeakingRoom>>> listActiveRooms() {
        return ResponseEntity.ok(
            ApiResponse.success("Success", speakingRoomService.listActiveRooms())
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SpeakingRoom>> createRoom(
        @RequestBody CreateRoomRequest request,
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
        if (user == null) return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));

        User createdBy = user.getUser();
        Integer maxParticipants = request != null ? request.getMaxParticipants() : null;
        SpeakingRoom room = speakingRoomService.createRoom(
            request != null ? request.getName() : null,
            maxParticipants,
            createdBy
        );

        broadcastParticipants(room.getRoomId());
        return ResponseEntity.ok(ApiResponse.success("Success", room));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<ApiResponse<Void>> joinRoom(
        @PathVariable("id") Long roomId,
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
        if (user == null) return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));

        speakingRoomService.joinRoom(roomId, user.getUser());
        broadcastParticipants(roomId);
        return ResponseEntity.ok(ApiResponse.success("Joined", null));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<ApiResponse<Void>> leaveRoom(
        @PathVariable("id") Long roomId,
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
        if (user == null) return ResponseEntity.status(401).body(ApiResponse.error("Unauthorized"));

        speakingRoomService.leaveRoom(roomId, user.getUser());
        broadcastParticipants(roomId);
        return ResponseEntity.ok(ApiResponse.success("Left", null));
    }

    private void broadcastParticipants(Long roomId) {
        List<SpeakingRoomParticipantDto> participants = speakingRoomService.listParticipants(roomId);
        messagingTemplate.convertAndSend("/topic/speaking-room/" + roomId + "/participants", participants);
    }

    @Data
    static class CreateRoomRequest {
        private String name;
        private Integer maxParticipants;
    }
}

