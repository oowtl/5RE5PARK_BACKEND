package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.vc.exception.VcAPIFilesIsEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VcApiServiceImpl implements VcApiService{
    @Value("${VC_URL}")
    private String vcUrl;//VC API 제공 해주는 곳
    @Value("${VC_APIKEY}")
    private String vcApiKey;//키 값

    @Override
    public String trgIdCreate(MultipartFile file) {
        String trgId = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = vcUrl + "/voices/add";//API 주소
            HttpPost post = new HttpPost(url);
            post.setHeader("xi-api-key", vcApiKey);//키값 입력

            // Multipart 요청 작성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("name",
                    "5re5PARKTRG",
                    ContentType.TEXT_PLAIN);//이름 생성
            builder.addBinaryBody(
                    "files",                         // 필드 이름
                    file.getInputStream(),           // 파일 데이터
                    ContentType.create("audio/wav"), // MIME 타입
                    file.getOriginalFilename()       // 파일 이름
            );//파일 입력
            post.setEntity(builder.build());

            // 요청 실행 및 응답 처리
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                trgId = extractValue(new String(response.getEntity().getContent().readAllBytes()),"voice_id");
                if (trgId == null){
                    throw new IllegalArgumentException("voice_id is null");
                }
                log.info("trgId : {} ", trgId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("trgIdResult : {} ", trgId);
        return trgId;
    }

    @Override
    public MultipartFile resultFileCreate(MultipartFile file, String trgId) {
        MultipartFile multipartFile = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            if(file.isEmpty()) {//파일이 없을경우 에러
                throw new VcAPIFilesIsEmptyException("vc api empty file");
            }
            //api 주소 값 입력, 나오는 오디오 포켓은 mp3_44100_192 지정
            String url = vcUrl + "/speech-to-speech/"+trgId+"?output_format=mp3_44100_192";
            HttpPost post = new HttpPost(url);
            //api 키 값 입력
            post.setHeader("xi-api-key", vcApiKey);

            // Multipart 요청 작성
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //요청할 값
            builder.addBinaryBody(
                    "audio",                         // 필드 이름
                    file.getInputStream(),           // 파일 데이터
                    ContentType.create("audio/wav"), // MIME 타입
                    file.getOriginalFilename()       // 파일 이름
            );//파일 값 입력
            //요청
            post.setEntity(builder.build());
            // 요청 실행 및 응답 처리
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                //응답 오디오 inputStream
                InputStream inputStream = response.getEntity().getContent();

                String filename = "response.wav";
                log.info("input stream : {} ", inputStream);
                multipartFile = convertInputStreamToMultipartFile(inputStream,
                        filename,
                        "audio/wav");//multipartfile로 변경

                // 서버가 반환한 파일 이름 (수동 설정)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return multipartFile;
    }

    // JSON에서 특정 키의 값을 추출하는 간단한 메서드
    private static String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\":\"";
        int startIndex = json.indexOf(keyPattern) + keyPattern.length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }
    //입력된 파일 MultipartFile로 변경
    public static MultipartFile convertInputStreamToMultipartFile(InputStream inputStream, String filename, String contentType) {
        try {
            // InputStream 데이터를 바이트 배열로 변환
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }

            // MockMultipartFile 생성
            return new MockMultipartFile(
                    "file", // 필드 이름
                    filename, // 원본 파일 이름
                    contentType, // 파일 Content-Type
                    buffer.toByteArray() // 파일 데이터
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
