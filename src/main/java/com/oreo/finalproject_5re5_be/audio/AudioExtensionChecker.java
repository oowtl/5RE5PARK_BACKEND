package com.oreo.finalproject_5re5_be.audio;

import java.io.*;

public class AudioExtensionChecker {
    private static final int WAV_SIGNATURE_BYTE = 4;
    private static final int MP3_SIGNATURE_BYTE = 2;

    //wav확장자 검사
    public static boolean isWavExtension(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[WAV_SIGNATURE_BYTE]; // 8글자 읽어야 하기때문에 4바이트
            if (fileInputStream.read(buffer) != -1) {
                String hexSignature = bytesToHex(buffer);
                fileInputStream.close();
                return AudioExtensions.isWavExtension(hexSignature);
            }
        }
        return false;
    }

    //mp3확장자 검사
    public static boolean isMp3Extension(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[MP3_SIGNATURE_BYTE]; // 4글자만 읽어야 하기떄문에 2바이트로 지정
            if (fileInputStream.read(buffer) != -1) {
                String hexSignature = bytesToHex(buffer);
                fileInputStream.close();
                return AudioExtensions.isMp3Extension(hexSignature);
            }
        }
        return false;
    }

    //wav확장자 검사
    public static boolean isWavExtension(byte[] byteArray) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        byte[] buffer = new byte[WAV_SIGNATURE_BYTE];
        if(byteArrayInputStream.read(buffer) != -1) {
            String hexSignature = bytesToHex(buffer);
            byteArrayInputStream.close();
            System.out.println("hexSignature = " + hexSignature);
            return AudioExtensions.isWavExtension(hexSignature);
        }
        return false;
    }

    //mp3확장자 검사
    public static boolean isMp3Extension(byte[] byteArray) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);

        byte[] buffer = new byte[MP3_SIGNATURE_BYTE];
        if(byteArrayInputStream.read(buffer) != -1) {
            String hexSignature = bytesToHex(buffer);
            byteArrayInputStream.close();
            System.out.println("hexSignature = " + hexSignature);
            return AudioExtensions.isMp3Extension(hexSignature);
        }
        return false;
    }

    // 바이트 배열을 헥사 문자열로 변환
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
