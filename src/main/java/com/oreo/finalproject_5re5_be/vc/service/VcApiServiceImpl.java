package com.oreo.finalproject_5re5_be.vc.service;

import com.oreo.finalproject_5re5_be.global.component.audio.AudioFileTypeConverter;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VcApiServiceImpl implements VcApiService{
    @Value("${VC_URL}")
    private String vcUrl;//VC API 제공 해주는 곳
    @Value("${VC_APIKEY}")
    private String vcApiKey;//키 값

    private static final String REMOVE_BACKGROUND_NOISE = "remove_background_noise"; // 백그라운드 노이즈 제거 설정 키
    private static final String RESPONSE_FILENAME = "response.wav"; // 응답 파일 이름
    private static final String RESPONSE_CONTENT_TYPE = "audio/wav"; // 응답 파일 MIME 타입

    /**
     * trgID 생성
     * @param file
     * @return String
     */
    @Override
    public String trgIdCreate(MultipartFile file) {
        String url = vcUrl + "/voices/add";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = createHttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .addTextBody("name", "5re5PARKTRG", ContentType.TEXT_PLAIN) // 요청 이름 설정
                    .addBinaryBody("files", file.getInputStream(), ContentType.create(file.getContentType()), file.getOriginalFilename()) // 파일 데이터 추가
                    .addTextBody(REMOVE_BACKGROUND_NOISE, "true", ContentType.TEXT_PLAIN); // 노이즈 제거 옵션 설정

            post.setEntity(builder.build());
            String responseBody = executeRequest(httpClient, post);// API 요청 실행
            //trgID 추출
            String trgId = extractValue(responseBody, "voice_id");
            if (trgId == null) {
                throw new IllegalArgumentException("voice_id is null");
            }
            log.info("Generated trgId: {}", trgId);
            return trgId;
        } catch (Exception e) {
            log.error("Error occurred while creating trgId: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create trgId", e);
        }
    }

    /**
     * 단일 resultFile 생성
     * @param file
     * @param trgId
     * @return MultipartFile
     */
    @Override
    public MultipartFile resultFileCreate(MultipartFile file, String trgId) {
        return createResultFile(file, trgId);
    }

    /**
     * resultFile 여러개 생성
     * @param files
     * @param trgId
     * @return
     */
    @Override
    public List<MultipartFile> resultFileCreate(List<MultipartFile> files, String trgId) {
        List<MultipartFile> resultFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new VcAPIFilesIsEmptyException("File is empty");
            }
            resultFiles.add(createResultFile(file, trgId));
        }
        return resultFiles;
    }
    //결과 파일 생성 api 요청
    private MultipartFile createResultFile(MultipartFile file, String trgId) {
        String url = vcUrl + "/speech-to-speech/" + trgId + "?output_format=mp3_44100_192";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = createHttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    .addBinaryBody("audio", file.getInputStream(),
                            ContentType.create(file.getContentType()),
                            file.getOriginalFilename()) // 파일 데이터 추가
                    .addTextBody(REMOVE_BACKGROUND_NOISE, "true", ContentType.TEXT_PLAIN); // 노이즈 제거 옵션 설정

            post.setEntity(builder.build());
            try (CloseableHttpResponse response = httpClient.execute(post)) {
                InputStream inputStream = response.getEntity().getContent();// 응답 데이터
                // InputStream을 MultipartFile로 변환
                return convertInputStreamToMultipartFile(inputStream, RESPONSE_FILENAME, RESPONSE_CONTENT_TYPE);
            }
        } catch (Exception e) {
            log.error("Error occurred while creating result file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create result file", e);
        }
    }

    //HttpPost 객체 생성
    private HttpPost createHttpPost(String url) {
        HttpPost post = new HttpPost(url);
        post.setHeader("xi-api-key", vcApiKey);
        return post;
    }
    //API 요청 실행
    private String executeRequest(CloseableHttpClient httpClient, HttpPost post) throws Exception {
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = new String(response.getEntity().getContent().readAllBytes()); // 응답
            if (statusCode >= 400) {
                log.error("Request failed with status code {}: {}", statusCode, responseBody);
                throw new IllegalArgumentException("Request failed with status code: " + statusCode);
            }
            return responseBody;
        }
    }
    //JSON 데이터에서 특정 키의 값을 추출
    private static String extractValue(String json, String key) {
        String keyPattern = "\"" + key + "\":\"";
        int startIndex = json.indexOf(keyPattern) + keyPattern.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (startIndex <= keyPattern.length() || endIndex <= 0) {
            return null;
        }
        return json.substring(startIndex, endIndex);
    }

    // InputStream -> MultipartFile로 변환
    private static MultipartFile convertInputStreamToMultipartFile(InputStream inputStream, String filename, String contentType) {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            return new MockMultipartFile("file", filename, contentType, buffer.toByteArray());
        } catch (Exception e) {
            log.error("Error occurred while converting InputStream to MultipartFile: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to convert InputStream to MultipartFile", e);
        }
    }
}
