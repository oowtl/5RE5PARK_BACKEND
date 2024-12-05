package com.oreo.finalproject_5re5_be.global.constant;

public enum MessageType {
    TTS_MAKE("TTS_MAKE"),
    VC_MAKE("VC_MAKE"),
    CONCAT_MAKE("CONCAT_MAKE"),
    CONCAT_BGM_MAKE("CONCAT_BGM_MAKE");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
