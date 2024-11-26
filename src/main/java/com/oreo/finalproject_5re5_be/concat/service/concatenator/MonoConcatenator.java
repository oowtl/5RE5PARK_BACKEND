package com.oreo.finalproject_5re5_be.concat.service.concatenator;


import javax.sound.sampled.AudioInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * <table class="striped">
 * <caption>기본 병합 클래스</caption>
 * <thead>
 *   <tr>
 *     <th scope="col">변수명
 *     <th scope="col">변수 타입
 *     <th scope="col">참고
 * </thead>
 * <tbody>
 *   <tr>
 *     <th scope="row">"files"
 *     <td>{@link java.util.List<AudioInputStream> List<AudioInputStream>}
 *     <td>리스트 Generic은  {@link AudioInputStream}
 * </tbody>
 * </table>
 */
public class MonoConcatenator implements Concatenator {
    private byte[] buffer = new byte[2048];

    @Override
    public ByteArrayOutputStream concatenate(List<AudioInputStream> audioStreams) throws IOException {
        boolean mono = audioStreams.stream().allMatch(as -> as.getFormat().getChannels() == 1);//모노 포맷인지 확인
        if (mono) {
            return merge(audioStreams);//병합
        }
        throw new IllegalArgumentException("잘못된 포맷 입니다.");
    }

    //오디오 파일 병합 메소드
    private ByteArrayOutputStream merge(List<AudioInputStream> audioStreams) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead;
        for (AudioInputStream audioStream : audioStreams) {
            while ((bytesRead = audioStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return outputStream;
    }

    @Override
    public void setBufferSize(int bufferSize) {
        buffer = new byte[bufferSize];//버퍼 사이즈 변경
    }
}
