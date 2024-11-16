package com.oreo.finalproject_5re5_be.member.service;


import com.oreo.finalproject_5re5_be.member.dto.request.CodeRequest;
import com.oreo.finalproject_5re5_be.member.dto.request.CodeUpdateRequest;
import com.oreo.finalproject_5re5_be.member.dto.response.CodeResponse;
import com.oreo.finalproject_5re5_be.member.dto.response.CodeResponses;
import com.oreo.finalproject_5re5_be.member.entity.Code;
import com.oreo.finalproject_5re5_be.member.exception.CodeDuplicatedException;
import com.oreo.finalproject_5re5_be.member.exception.CodeNotFoundException;
import com.oreo.finalproject_5re5_be.member.repository.CodeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CodeServiceImpl {

    private final CodeRepository codeRepository;

    public CodeServiceImpl(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    // 코드 등록
    public CodeResponse create(CodeRequest request) {
        // 유효성 검증이 완료된 request로부터 엔티티를 생성한다
        // 기존에 코드와 중복되는지 확인한다
        boolean isDuplicated = codeRepository.existsByCode(request.getCode());
        if (isDuplicated) {
            throw new CodeDuplicatedException();
        }

        // 중복되는 코드가 없다면 저장한다
        Code code = request.createCodeEntity();
        Code savedCode = codeRepository.save(code);
        return CodeResponse.of(savedCode);
    }

    // 코드 조회

    // 모든 코드 조회
    public CodeResponses readAll() {
        // 모든 코드 엔티티를 조회
        List<Code> foundCodes = codeRepository.findAll();
        // 리스트로 변환
        List<CodeResponse> codeResponseList = foundCodes.stream()
                                                     .map(CodeResponse::of)
                                                     .toList();

        // 조회된 엔티티를 response로 변환하여 반환한다
        return CodeResponses.of(codeResponseList);
    }

    // 시퀀스로 조회
    public CodeResponse read(Long codeSeq) {
        // 시퀀스로 특정 코드 엔티티를 조회한다
        Code foundCode = codeRepository.findCodeByCodeSeq(codeSeq);
        // 조회된 엔티티를 response로 변환하여 반환한다
        return CodeResponse.of(foundCode);
    }

    // 코드 번호로 특정 코드 조회
    public CodeResponse read(String code) {
        // 코드 번호로 특정 코드 엔티티를 조회한다
        Code foundCode = codeRepository.findCodeByCode(code);
        // 조회된 엔티티를 response로 변환하여 반환한다
        return CodeResponse.of(foundCode);
    }


    // 각 파트별 사용 가능한 코드 조회
    public CodeResponses readAvailableCodeByCateNum(String cateNum) {
        // 전달 받은 파트에 해당하는 사용 가능한 모든 코드 엔티티를 조회한다
        List<Code> foundCodes = codeRepository.findAvailableCodesByCateNum(cateNum);
        // 리스트에 담는다
        List<CodeResponse> codeResponseList = foundCodes.stream()
                                                        .map(CodeResponse::of)
                                                        .toList();
        // 조회된 엔티티를 responses로 변환하여 반환한다
        return CodeResponses.of(codeResponseList);
    }

    // 각 파트별 모든 코드 조회
    public CodeResponses readAllCodeByPart(String cateNum) {
        // 전달 받은 파트에 해당하는 모든 코드 엔티티를 조회한다
        List<Code> foundCodes = codeRepository.findCodesByCateNum(cateNum);
        // 리스트에 담는다
        List<CodeResponse> codeResponseList = foundCodes.stream()
                .map(CodeResponse::of)
                .toList();
        // 조회된 엔티티를 responses로 변환하여 반환한다
        return CodeResponses.of(codeResponseList);
    }


    // 코드 수정
    public void update(Long codeSeq, CodeUpdateRequest request) {
        // 시퀀스로 특정 코드 엔티티를 조회한다
        Code foundCode = codeRepository.findCodeByCodeSeq(codeSeq);

        // 조회에 실패할 경우 예외를 발생시킨다.
        if (foundCode == null) {
            // 없을 경우 예외 발생
            throw new CodeNotFoundException();
        }

        // 유효성 검증이 완료된 request로부터 엔티티를 수정한다
        foundCode.update(request);
    }

    // 코드 삭제
    public void delete(Long codeSeq) {
        // 시퀀스로 특정 코드 엔티티를 조회한다
        Code foundCode = codeRepository.findCodeByCodeSeq(codeSeq);

        // 조회에 실패할 경우 예외를 발생시킨다.
        if (foundCode == null) {
            // 없을 경우 예외 발생
            throw new CodeNotFoundException();
        }

        // 조회된 엔티티를 삭제한다
        codeRepository.delete(foundCode);
    }

}
