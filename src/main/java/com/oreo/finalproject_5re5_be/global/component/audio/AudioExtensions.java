package com.oreo.finalproject_5re5_be.global.component.audio;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

/**
 * @apiNote 이 클래스는 파일의 확장자(---.ext) 를 검사하는 클래스가 아닙니다. 파일 확장자 고유의 시그니처를 검사합니다.
 * @see @code AudioExtensionChecker
 * @author K-KY
 */
@Getter
public enum AudioExtensions {
    // wav파일 시그니처를 표현하기 위해 앞에 문자열을 붙임
    E_52494646("WAV"),
    E_FFF3("MP3"),
    E_FFF2("MP3"),
    E_FFFB("MP3");

    AudioExtensions(String signatures) {
        this.signatures = signatures;
    }

    private static final List<String> collect =
            Arrays.stream(AudioExtensions.values()).map(Enum::toString).toList();
    private final String signatures;

    // 파라미터가 Enum중 있는지 여부 반환
    public static boolean isSupported(String signature) {
        return collect.contains("E_" + signature);
    }

    // Enum의 wav 확장자 시그니처와 파라미터가 같은지 여부 반환
    public static boolean isWavExtension(String signature) {
        return AudioExtensions.E_52494646.toString().equals("E_" + signature);
    }

    // wav 확장자가 아니고 지원되는 확장자라면 mp3확장자
    public static boolean isMp3Extension(String signature) {
        if (isWavExtension(signature)) {
            return false;
        }
        return isSupported(signature);
    }
}
