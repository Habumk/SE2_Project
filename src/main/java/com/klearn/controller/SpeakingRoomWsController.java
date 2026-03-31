package com.klearn.controller;

import com.klearn.dto.MicToggleMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SpeakingRoomWsController {

    private final SimpMessagingTemplate messagingTemplate;

    // Broadcast mic toggle state to all subscribers (client-side only).
    @MessageMapping("/speaking-room/{roomId}/mic")
    public void handleMicToggle(
        @DestinationVariable("roomId") Long roomId,
        MicToggleMessage message
    ) {
        messagingTemplate.convertAndSend("/topic/speaking-room/" + roomId + "/mic", message);
    }
}

