package com.oreo.finalproject_5re5_be.global.component.audio;


import javax.sound.sampled.*;
import java.io.*;

public class AudioExtensionConverter {

    private static final int DEFAULT_BIT_DEPTH = 16;

    public static byte[] mp3ToWav(File file) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);// 파일을 AudioInputStream으로 읽기

        AudioFormat baseFormat = audioInputStream.getFormat();// WAV 포맷으로 변환할 대상 포맷 설정
        AudioFormat decodedFormat = getDecodedFormat(baseFormat);
        AudioInputStream finalStream = getAudioInputStream(decodedFormat, audioInputStream);

        ByteArrayOutputStream wavOutputStream = new ByteArrayOutputStream();//메모리에 저장할 ByteArray
        AudioSystem.write(finalStream, AudioFileFormat.Type.WAVE, wavOutputStream);// 메모리 내에서 WAV 형식으로 변환 및 저장

        return wavOutputStream.toByteArray();// 바이트 배열 반환

    }

    public static byte[] mp3ToWav(AudioInputStream audioInputStream) throws IOException {
        AudioFormat baseFormat = audioInputStream.getFormat();// WAV 포맷으로 변환할 대상 포맷 설정
        AudioFormat decodedFormat = getDecodedFormat(baseFormat);
        AudioInputStream finalStream = getAudioInputStream(decodedFormat, audioInputStream);

        ByteArrayOutputStream wavOutputStream = new ByteArrayOutputStream();//메모리에 저장할 ByteArray
        AudioSystem.write(finalStream, AudioFileFormat.Type.WAVE, wavOutputStream);// 메모리 내에서 WAV 형식으로 변환 및 저장

        return wavOutputStream.toByteArray();// 바이트 배열 반환

    }

    public static byte[] mp3ToWav(InputStream inputStream) throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);// 파일을 AudioInputStream으로 읽기

        AudioFormat baseFormat = audioInputStream.getFormat();// WAV 포맷으로 변환할 대상 포맷 설정
        AudioFormat decodedFormat = getDecodedFormat(baseFormat);
        AudioInputStream finalStream = getAudioInputStream(decodedFormat, audioInputStream);

        ByteArrayOutputStream wavOutputStream = new ByteArrayOutputStream();//메모리에 저장할 ByteArray
        AudioSystem.write(finalStream, AudioFileFormat.Type.WAVE, wavOutputStream);// 메모리 내에서 WAV 형식으로 변환 및 저장

        return wavOutputStream.toByteArray();// 바이트 배열 반환

    }

    private static AudioFormat getDecodedFormat(AudioFormat baseFormat) {
        return new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                DEFAULT_BIT_DEPTH,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
        );
    }

    private static AudioInputStream getAudioInputStream(
            AudioFormat decodedFormat, AudioInputStream audioInputStream) throws IOException {
        // 변환된 AudioInputStream
        AudioInputStream pcmStream = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);

        // 전체 데이터를 ByteArrayOutputStream에 저장하여 스트림 길이를 계산
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = pcmStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        // 데이터를 바이트 배열로 변환
        return getAudioInputStream(decodedFormat, byteArrayOutputStream);
    }

    private static AudioInputStream getAudioInputStream(AudioFormat decodedFormat,
                                                        ByteArrayOutputStream byteArrayOutputStream) {
        byte[] pcmData = byteArrayOutputStream.toByteArray();

        // 새로운 AudioInputStream 생성하여 길이 지정
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pcmData);
        return new AudioInputStream(
                byteArrayInputStream, decodedFormat, pcmData.length / decodedFormat.getFrameSize()
        );
    }
}

