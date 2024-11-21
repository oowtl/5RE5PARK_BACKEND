package com.oreo.finalproject_5re5_be.tts.entity;

public enum ServerCode {
    GOOGLE_CLOUD("google cloud"),
    NAVER_CLOVA("naver clova");

    private final String name;

    ServerCode(String name) {
        this.name = name;
    }
}
