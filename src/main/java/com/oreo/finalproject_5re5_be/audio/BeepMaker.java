package com.oreo.finalproject_5re5_be.audio;


/**
 * 파라미터로 전달된 주파수의 오디오를 생성한다.
 */
public class BeepMaker {
    public static final int SAMPLE_RATE = 44100;
    private static final int FREQUENCY_MIN = 0;

    /**
     *
     * @param frequency
     * @param duration
     *
     * <br>
     * frequency = 생성할 주파수
     * <br>
     * duration = 음성 길이
     * <br>
     *
     */
    public static byte[] makeBeep(int frequency, float duration) {
        // 총 샘플 수 (소수점 단위로 계산된 시간에 따라 샘플 수 계산)
        int totalSamples = (int) (SAMPLE_RATE * duration);

        return generate(frequency, totalSamples);
    }

    //무음 오디오 생성
    public static byte[] makeSilence(float duration) {
        return makeBeep(FREQUENCY_MIN, duration);
    }

    //오디오 바이트 생성
    private static byte[] generate(int frequency, int totalSamples) {
        byte[] output = new byte[totalSamples];
        for (int i = FREQUENCY_MIN; i < totalSamples; i++) {
            output[i] = (byte) (Math.sin(2 * Math.PI * frequency * (double) i / SAMPLE_RATE) * 127); // 8비트로 변환
        }
        return output;
    }
}
