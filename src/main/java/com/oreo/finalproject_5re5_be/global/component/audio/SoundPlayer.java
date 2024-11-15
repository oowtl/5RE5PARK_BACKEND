package com.oreo.finalproject_5re5_be.global.component.audio;

import javax.sound.sampled.*;
import java.io.IOException;

public class SoundPlayer {
    private byte[] buffer = new byte[4096]; // 오디오가 재생되는 버퍼의 크기
    private int bytesRead;

    /**
     * {@code AudioInputStream} 의 오디오를 실행
     *
     * @param audioInputStream
     * @throws LineUnavailableException
     * @throws IOException
     */
    public void play(AudioInputStream audioInputStream) throws IOException {
        try {
            Result result = getAudio(audioInputStream);//실행 할 수 있는 데이터로 변환

            SourceDataLine sourceDataLine = result.sourceLine();
            sourceDataLine.open(result.format());//시스템 리소스 획득
            sourceDataLine.start();//sourceLine의 IO 실행

            loadBuffer(audioInputStream, sourceDataLine);//버퍼에 담아 실행

            // 재생이 완료되면 리소스 반환
            sourceDataLine.drain();
            sourceDataLine.close();
            audioInputStream.close();
        } catch (LineUnavailableException e) {
            throw new IllegalArgumentException("재생할 수 있는 타입의 파라미터가 아닙니다");
        }
    }

    private Result getAudio(AudioInputStream audioInputStream) throws LineUnavailableException {
        //AudioInputStream을 AudioFormat으로 변환
        AudioFormat format = audioInputStream.getFormat();

        //단일 오디오 형식을 포함하는 지정된 정보로부터 데이터 라인의 정보 객체를 구성
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        //info에 지정된 audioFormat의 SourceDataLine 객체 획득
        SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
        return new Result(format, sourceLine);
    }

    private void loadBuffer(AudioInputStream audioInputStream, SourceDataLine sourceLine) throws IOException {
        //실제로 오디오가 실행되는 장소
        while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
            sourceLine.write(buffer, 0, bytesRead);
        }
    }

    //버퍼의 크기를 변경하기 위한 세터
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    private record Result(AudioFormat format, SourceDataLine sourceLine) {
    }
}
