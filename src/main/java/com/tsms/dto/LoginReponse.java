package com.tsms.dto;


public class LoginReponse {

    private String message;
    private Long userId;

    public LoginReponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }

    // getters
}
