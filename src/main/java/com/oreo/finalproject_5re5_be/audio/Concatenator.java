package com.oreo.finalproject_5re5_be.audio;

import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * {@code Concatenator} 클래스의 기본 인터페이스 입니다.
 * <br>
 * 모든 Concatenator는 이 인터페이스를 구현합니다.
 */
public interface Concatenator {


    ByteArrayOutputStream concatenate(List<AudioInputStream> audioStreams) throws IOException;

    void setBufferSize(int bufferSize);
}

