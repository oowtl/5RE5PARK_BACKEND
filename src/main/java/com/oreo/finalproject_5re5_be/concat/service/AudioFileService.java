package com.oreo.finalproject_5re5_be.concat.service;

import com.oreo.finalproject_5re5_be.concat.dto.response.ConcatUrlResponse;
import com.oreo.finalproject_5re5_be.concat.entity.AudioFile;
import com.oreo.finalproject_5re5_be.concat.repository.AudioFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AudioFileService {

    private final AudioFileRepository audioFileRepository;


//    //AudioFile 정보를 받아 저장 (1개)
//    public ConcatUrlResponse saveAudioFile(OriginAudioRequest request) {
//        //해당하는 AudioRowSeq를 조회
////        ConcatRow rowSeq = rowFind(request.getSeq()); //audioFile의 seq를 써서 어떤 row와 매칭되는지 가져오기
//        //seq번호를 받아서 어떤 concatRow 객체와 매칭되는지
//
//        //해당하는 AudioFormatSeq를 조회
////        AudioFormat formatSeq = formatFind(request.getSeq()); //audioFile의 seq를 써서 어떤 format과 매칭되는지 가져오기
//
//        //프로젝트 조회한 값과 입력한 값 저장을 하기 위한 AudioFile 객체 생성
//        AudioFile audioFile = AudioFile.builder()
////                .concatRow(rowSeq) //어떤 concatRow에 대응되는지 넣기
////                .audioFormat(formatSeq) //어떤 audioFormat에 대응되는지 넣기
//                .audioUrl(request.getAudioUrl())
//                .extension(request.getExtension())
//                .fileSize(request.getFileSize())
//                .fileLength(request.getFileLength())
//                .fileName(request.getFileName())
//                .createdDate(request.getCreatedDate())
//                .build();
//
//        AudioFile save = audioFileRepository.save(audioFile); //audioFile 객체 저장
//
//        return ConcatUrlResponse.builder() //response 객체 생성
//                .seq(save.getAudioFileSeq())
//                .url(save.getAudioUrl())
//                .build();
//    }


//    // AudioFile 정보를 받아 저장 (N개)
//    public List<ConcatUrlResponse> batchSaveAudioFiles(List<OriginAudioRequest> requestList) {
//        // 요청 리스트를 반복 처리하여 AudioFile 객체 생성
//        List<AudioFile> audioFileList = requestList.stream().map(request -> {
//            // 해당하는 AudioRowSeq를 조회
////            ConcatRow rowSeq = rowFind(request.getSeq()); // audioFile의 seq를 써서 어떤 row와 매칭되는지 가져오기
//
//            // 해당하는 AudioFormatSeq를 조회
////            AudioFormat formatSeq = formatFind(request.getSeq()); // audioFile의 seq를 써서 어떤 format과 매칭되는지 가져오기
//
//            // AudioFile 객체 생성
//            return AudioFile.builder()
////                    .concatRow(rowSeq) // 어떤 concatRow에 대응되는지 넣기
////                    .audioFormat(formatSeq) // 어떤 audioFormat에 대응되는지 넣기
//                    .audioUrl(request.getAudioUrl())
//                    .extension(request.getExtension())
//                    .fileSize(request.getFileSize())
//                    .fileLength(request.getFileLength())
//                    .fileName(request.getFileName())
//                    .createdDate(request.getCreatedDate())
//                    .build();
//        }).collect(Collectors.toList());
//
//        // AudioFile 리스트 저장 (Batch 처리)
//        List<AudioFile> savedFiles = audioFileRepository.saveAll(audioFileList);
//
//        // 저장된 파일 정보로 응답 리스트 생성
//        return savedFiles.stream().map(savedFile ->
//                ConcatUrlResponse.builder()
//                        .seq(savedFile.getAudioFileSeq())
//                        .url(savedFile.getAudioUrl())
//                        .build()
//        ).collect(Collectors.toList());
//    }


    // audioFile seq로 audioFile 정보 조회 (1개)
    public AudioFile getAudioFile(Long audioFileSeq) {
        return audioFileRepository.findById(audioFileSeq)
                .orElseThrow(() -> new RuntimeException("Audio file not found"));
    }


    // audioFile Url로 audioFile 정보 조회 (1개)
    public AudioFile getAudioFileByUrl(String audioUrl) {
        return audioFileRepository.findByAudioUrl(audioUrl)
                .orElseThrow(() -> new IllegalArgumentException("AudioFile not found with URL: " + audioUrl));
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
}

