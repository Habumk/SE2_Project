package com.klearn.dto;

import lombok.Data;

/**
 * UC-02: Register form binding object.
 */
@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
}
