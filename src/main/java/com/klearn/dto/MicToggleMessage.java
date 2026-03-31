package com.klearn.dto;

import lombok.Data;

@Data
public class MicToggleMessage {
    private Long userId;
    private boolean micOn;
}

