package com.oreo.finalproject_5re5_be.audio;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum AudioExtensions {
    E_52494646("WAV"), E_FFF3("MP3"), E_FFF2("MP3"), E_FFFB("MP3");

    AudioExtensions(String signatures) {
        this.signatures = signatures;
    }

    private final static List<String> collect = Arrays.stream(AudioExtensions.values()).map(Enum::toString).toList();
    private final String signatures;

    public static boolean isSupported(String signature) {
        return collect.contains("E_" + signature);
    }

    public static boolean isWavExtension(String signature) {
        return AudioExtensions.E_52494646.toString().equals("E_" + signature);
    }

    //wav 확장자가 아니고 지원되는 확장자라면 mp3확장자
    public static boolean isMp3Extension(String signature) {
        if (isWavExtension(signature)) {
            return false;
        }
        return isSupported(signature);
    }


}
