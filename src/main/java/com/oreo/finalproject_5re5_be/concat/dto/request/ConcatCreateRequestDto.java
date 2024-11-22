package com.oreo.finalproject_5re5_be.concat.dto.request;

public class ConcatCreateRequestDto {
    private final Long projectSequence;
    private final Long memberSequence;

    public ConcatCreateRequestDto(Long projectSequence, Long memberSequence) {
        this.projectSequence = projectSequence;
        this.memberSequence = memberSequence;
    }

    public Long getProjectSequence() {
        return projectSequence;
    }

    public Long getMemberSequence() {
        return memberSequence;
    }
}
