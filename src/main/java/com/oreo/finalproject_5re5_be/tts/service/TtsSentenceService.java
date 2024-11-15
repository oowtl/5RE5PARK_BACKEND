package com.oreo.finalproject_5re5_be.tts.service;

import com.oreo.finalproject_5re5_be.tts.dto.request.TtsSentenceCreateRequest;
import com.oreo.finalproject_5re5_be.tts.dto.response.TtsSentenceDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface TtsSentenceService {
    TtsSentenceDto addSentence(@Valid @NotNull Long projectSeq, @Valid TtsSentenceCreateRequest createRequest);
}
