package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.ConcatRowDto;
import com.oreo.finalproject_5re5_be.concat.dto.RowAudioFileDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.AudioFileRequestDto;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.repository.AudioFileRepository;
import com.oreo.finalproject_5re5_be.concat.service.helper.AudioFileHelper;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.global.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RequiredArgsConstructor
@Service
@Log4j2
public class AudioFileService {

    private final AudioFileRepository audioFileRepository;
    private final S3Service s3Service;
    private final AudioFileHelper audioFileHelper;

    // audioFile seq로 audioFile 정보 조회 (1개)
    public AudioFile getAudioFile(Long audioFileSeq) {
        return audioFileRepository.findById(audioFileSeq)
                .orElseThrow(() -> new RuntimeException("Audio file not found"));
    }

    public List<RowAudioFileDto> getAudioFilesByProjectAndStatusTrue(Long projectSeq) {
        List<AudioFile> audioFiles = audioFileRepository.findAudioFileByProjectSeq(projectSeq);
        return audioFiles.stream().map(this::convertToRowAudioFileDto).collect(Collectors.toList());
    }

    // audioFile Url로 audioFile 정보 조회 (1개)
    public AudioFile getAudioFileByUrl(String audioUrl) {
        return audioFileRepository.findByAudioUrl(audioUrl)
                .orElseThrow(() -> new IllegalArgumentException("AudioFile not found with URL: " + audioUrl));
    }

    // audioFile Url로 audioFileSeq 정보 조회 (N개)
    public List<Long> getAudioFileSeqsByUrls(List<String> audioUrls) {
        return audioUrls.stream()
                .map(url -> getAudioFileByUrl(url).getAudioFileSeq()) // URL로 AudioFile 조회 후 Seq 추출
                .toList();
    }

    // audioFile Name으로 audioFile 정보 조회 (1개)
    public AudioFile getAudioFileByName(String fileName) {
        return audioFileRepository.findByFileName(fileName)
                .orElseThrow(() -> new IllegalArgumentException("AudioFile not found with fileName: " + fileName));
    }


    // concatRow의 seq를 받아서 그에 매칭되는 audioFile 정보 조회 (1개)
    public AudioFile getAudioFileByRowSeq(Long rowSeq) {
        return audioFileRepository.findByRowSeq(rowSeq)
                .orElseThrow(() -> new IllegalArgumentException("AudioFile not found for concatRowSeq: " + rowSeq));
    }

    // concatRow의 seq를 받아서 그에 매칭되는 audioFile 정보 조회 (n개)
    public List<AudioFileDto> getAudioFileByRowSeq(List<Long> rowSeq) {
        return audioFileRepository.findAllByConcatRowSeqs(rowSeq).stream()
                .map(this::convertToAudioFileDto).toList();
    }


    // 날짜를 받아서 매칭되는 audioFile 정보 조회 (N개)
    public List<AudioFile> getAudioFilesByCreatedDate(LocalDate date) {
        //매칭되는 오디오파일을 리스트로 저장
        List<AudioFile> audioFiles = audioFileRepository.findByCreatedDateOnly(date);
        if (audioFiles.isEmpty()) {
            throw new IllegalArgumentException("No AudioFiles found with the specified created date: " + date);
        }
        return audioFiles;
    }


    // 파일확장자로 오디오파일들을 페이징처리해서 조회 (N개)
    public List<ConcatUrlResponse> findAudioFilesByExtensionWithPaging(String extension, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 페이지 번호와 크기를 설정
        Page<AudioFile> audioFilePage = audioFileRepository.findByExtension(extension, pageable);

        // AudioFile -> ConcatUrlResponse 변환
        return audioFilePage.getContent().stream()
                .map(file -> ConcatUrlResponse.builder()
                        .seq(file.getAudioFileSeq())
                        .url(file.getAudioUrl())
                        .build())
                .collect(Collectors.toList());
    }

    public List<AudioFileDto> findByConcatRowSeq(List<Long> concatRowSeq) {
        List<AudioFile> audioFiles = audioFileRepository.findAllByConcatRowSeq(concatRowSeq);
        return audioFiles.stream().map(this::convertToAudioFileDto).collect(Collectors.toList());
    }


    // AudioFile seq로 삭제 (1개)
    public void deleteAudioFileBySeq(Long audioFileSeq) {
        //만약 존재하지 않으면 예외
        if (!audioFileRepository.existsById(audioFileSeq)) {
            throw new IllegalArgumentException("Audio file not found with seq: " + audioFileSeq);
        }
        audioFileRepository.deleteById(audioFileSeq);
    }

    // AudioFile seq로 삭제 (N개)
    public void deleteAudioFilesBySeq(List<Long> audioFileSeqList) {
        for (Long seq : audioFileSeqList) {
            //만약 존재하지 않으면 예외
            if (!audioFileRepository.existsById(seq)) {
                throw new IllegalArgumentException("Audio file not found with seq: " + seq);
            }
            audioFileRepository.deleteById(seq);
        }
    }

    // AudioFile seq 리스트를 받아서 매칭되는 ConcatRow seq 리스트 반환
    public List<Long> findConcatRowSeqsByAudioFileSeqs(List<Long> audioFileSeqs) {
        return audioFileRepository.findConcatRowSeqsByAudioFileSeqs(audioFileSeqs);
    }

    public List<AudioFileRequestDto> checkExtension(List<AudioFileRequestDto> audioDto) throws IOException {

        List<AudioFileRequestDto> notSupported = new ArrayList<>();

        for (AudioFileRequestDto audioFileRequestDto : audioDto) {
            byte[] bytes = audioFileRequestDto.getAudioFile().getBytes();
            if (isAudioFile(bytes)) {
                log.info("checkExtension : [{}]", "AUDIO_FILE_CHECK_SUCCESS");
                continue;
            }

            notSupported.add(new AudioFileRequestDto(audioFileRequestDto.getAudioFile().getOriginalFilename()));
            log.info("checkExtension : [{}]", "AUDIO_FILE_CHECK_FAIL");
        }
        return notSupported;
    }

    //s3 업로드
    public List<OriginAudioRequest> saveAudioFile(List<AudioFileRequestDto> audioDto) throws IOException {
        if (audioDto.isEmpty()) {
            throw new IllegalArgumentException("[AudioFileService.saveAudioFile] 오디오 파일이 비어있습니다.");
        }
        if (checkExtension(audioDto).isEmpty()) {
            return audioDto.stream().map(dto -> {
                // S3 업로드
                String audioUrl = s3Service.upload(dto.getAudioFile(), "concat/audio");

                // AudioFile 엔티티 생성
                try {
                    return prepareDto(dto, audioUrl);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toList();
        }
        throw new DataNotFoundException("파일 형식이 올바르지 않습니다.");
    }


    public void saveAudioFiles(List<AudioFile> audioFiles) {
        audioFileHelper.batchInsert(audioFiles);
    }

    private OriginAudioRequest prepareDto(AudioFileRequestDto dto, String audioUrl) {
        try {
            return OriginAudioRequest.builder()
                    .audioUrl(audioUrl) // S3에 저장된 파일 URL
                    .extension(getFileExtension(dto.getAudioFile().getOriginalFilename())) // 파일 확장자 추출
                    .fileSize(dto.getAudioFile().getSize()) // 파일 크기
                    .fileLength(getFileLength(dto.getAudioFile().getInputStream())) // 파일 길이 계산
                    .fileName(dto.getAudioFile().getOriginalFilename()) // 원본 파일명
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //사용자의 오디오 파일 전체 조회
    public List<AudioFileDto> getMemberAudioFile(Long memberSeq, Pageable pageable) {
        List<AudioFile> memberAudios = audioFileRepository.findAudioFileByMember(memberSeq, pageable);
        if (memberAudios.isEmpty()) {//페이지 범위를 벗어나면 null을 반환 하므로 가장 뒤의 페이지를 반환
            log.info("요청 가능한 페이지 번호 초과, PageNumber : [{}]", pageable.getPageNumber());
            int maxPageNumber = getAudioFilePages(memberSeq, pageable.getPageSize()).size() - 1;
            Pageable maxPageable = PageRequest.of(maxPageNumber, pageable.getPageSize(), pageable.getSort());
            List<AudioFile> maxMemberAudios = audioFileRepository.findAudioFileByMember(memberSeq, maxPageable);
            return maxMemberAudios.stream().map(this::convertToAudioFileDto).toList();
        }
        return memberAudios.stream().map(this::convertToAudioFileDto).toList();
    }

    //사용자 오디오 파일 페이지 배열 반환
    public List<Integer> getAudioFilePages(Long memberSeq, int size) {
        long totalCount = audioFileRepository.countByMemberSeq(memberSeq);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // Generate page numbers (0-based indexing)
        return IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
    }

    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toUpperCase();
    }

    public static long getFileLength(InputStream inputStream) throws Exception {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream)) {
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            return (long) (frames / format.getFrameRate()); // 초 단위 길이 반환
        }
    }


    public static boolean isAudioFile(File file) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file)) {
            return true; // 파일이 오디오 형식으로 처리 가능
        } catch (UnsupportedAudioFileException | IOException e) {
            return false; // 오디오 파일이 아니거나 지원하지 않는 형식
        }
    }

    public static boolean isAudioFile(InputStream stream) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(stream)) {
            return true; // 파일이 오디오 형식으로 처리 가능
        } catch (UnsupportedAudioFileException | IOException e) {
            return false; // 오디오 파일이 아니거나 지원하지 않는 형식
        }
    }

    public static boolean isAudioFile(byte[] stream) {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(stream))) {
            return true; // 파일이 오디오 형식으로 처리 가능
        } catch (UnsupportedAudioFileException | IOException e) {
            return false; // 오디오 파일이 아니거나 지원하지 않는 형식
        }
    }

    //AudioFileDto변환기
    private AudioFileDto convertToAudioFileDto(AudioFile audioFile) {
        return AudioFileDto.builder()
                .audioFileSeq(audioFile.getAudioFileSeq())
                .audioUrl(audioFile.getAudioUrl())
                .fileLength(audioFile.getFileLength())
                .fileName(audioFile.getFileName())
                .fileSize(audioFile.getFileSize())
                .createdDate(audioFile.getCreatedDate())
                .extension(audioFile.getExtension())
                .build();
    }

    private RowAudioFileDto convertToRowAudioFileDto(AudioFile audioFile) {
        return RowAudioFileDto.builder()
                .audioFileSeq(audioFile.getAudioFileSeq())
                .audioUrl(audioFile.getAudioUrl())
                .fileLength(audioFile.getFileLength())
                .fileName(audioFile.getFileName())
                .fileSize(audioFile.getFileSize())
                .createdDate(audioFile.getCreatedDate())
                .extension(audioFile.getExtension())
                .concatRow(ConcatRowDto.of(audioFile.getConcatRow()))
                .build();
    }

    public List<AudioFile> saveAll(List<AudioFile> audioFiles) {
        return audioFileRepository.saveAll(audioFiles);
    }
}