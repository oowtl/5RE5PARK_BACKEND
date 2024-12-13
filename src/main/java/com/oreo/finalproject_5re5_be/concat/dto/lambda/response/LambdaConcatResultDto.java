package com.oreo.finalproject_5re5_be.concat.dto.lambda.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oreo.finalproject_5re5_be.concat.dto.request.OriginAudioRequest;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaConcatResultDto {
    private final AudioInfo info;
    private final List<OriginAudioRequest> processed;
    private final String s3Url;
    private final String contentType;
    private final String processId;
    private final int i;

    @JsonCreator
    public LambdaConcatResultDto(
            @JsonProperty("info") AudioInfo info,
            @JsonProperty("processed") List<OriginAudioRequest> processed,
            @JsonProperty("s3Url") String s3Url,
            @JsonProperty("contentType") String contentType,
            @JsonProperty("processId") String processId,
            @JsonProperty("i") int i) {
        this.info = info;
        this.processed = processed;
        this.s3Url = s3Url;
        this.contentType = contentType;
        this.processId = processId;
        this.i = i;
    }

    @Override
    public String toString() {
        return "LambdaConcatResultDto{"
                + ", processed="
                + processed
                + ", s3Url='"
                + s3Url
                + '\''
                + ", contentType='"
                + contentType
                + '\''
                + ", processId='"
                + processId
                + '\''
                + ", i="
                + i
                + '}';
    }
}
