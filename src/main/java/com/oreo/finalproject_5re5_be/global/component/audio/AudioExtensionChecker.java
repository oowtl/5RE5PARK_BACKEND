package com.oreo.finalproject_5re5_be.global.component.audio;

import java.io.*;

/**
 * 이 클래스는 파일 또는 바이트 배열의 시그니처를 읽어 어떤 확장자 인지 확인합니다.
 * @apiNote 바이트 배열을 조작해 확장자의 시그니처와 일치 시키는 경우 True를 반환합니다.<br>
 * 바이트 배열이 실제 파일을 변환한 값인지, 확인하는 과정이 선행 되어야 할 수 있습니다.
 * @author K-KY
 */
public class AudioExtensionChecker {
    private static final int WAV_SIGNATURE_BYTE = 4;
    private static final int MP3_SIGNATURE_BYTE = 2;

    //wav확장자 검사
    public static boolean isWavExtension(File file) throws IOException {
        //파일을 바이트로 읽기
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[WAV_SIGNATURE_BYTE]; //wav 시그니처 8글자 읽어야 하기때문에 4바이트
            if (fileInputStream.read(buffer) != -1) {
                String hexSignature = bytesToHex(buffer);//읽은 바이트 배열을 문자열로 변환
                fileInputStream.close();//리소스 반환
                return AudioExtensions.isWavExtension(hexSignature);//파일 검사
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
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);//InputStream으로 변환

        byte[] buffer = new byte[WAV_SIGNATURE_BYTE];
        if(byteArrayInputStream.read(buffer) != -1) {//스트림에서 WAV_SIGNATURE_BYTE 만큼 읽기
            String hexSignature = bytesToHex(buffer);//읽은 buffer을 String으로 변환
            byteArrayInputStream.close();//리소스 반환
            System.out.println("hexSignature = " + hexSignature);
            return AudioExtensions.isWavExtension(hexSignature);//바이트 배열 검사
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
