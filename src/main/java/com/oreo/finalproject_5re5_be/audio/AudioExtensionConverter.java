package com.oreo.finalproject_5re5_be.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class AudioExtensionConverter {

    private static final int DEFAULT_BIT_DEPTH = 16;

    public static AudioInputStream mp3ToWav(File file) throws UnsupportedAudioFileException, IOException {
        // 파일을 AudioInputStream으로 읽기
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

        // WAV 포맷으로 변환
        AudioFormat baseFormat = audioInputStream.getFormat();

        //디코딩 템플릿 같은거
        AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                DEFAULT_BIT_DEPTH,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
        );

        return AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
    }

    //wavToMp3
}

