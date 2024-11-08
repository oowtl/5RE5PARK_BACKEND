package com.oreo.finalproject_5re5_be.audio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;


@SpringBootTest
class AudioExtensionConverterTest {

    @Test
    void convertMp3ToWavTest() throws UnsupportedAudioFileException, IOException {
        AudioInputStream audioInputStream = AudioExtensionConverter.mp3ToWav(new File("/Users/kyuyoung/Downloads/lora.mp3"));

        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File("test.wav"));
    }
}