package com.oreo.finalproject_5re5_be.code.dto.response;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class CodeResponses {

    private List<CodeResponse> codeResponses;

    public CodeResponses(List<CodeResponse> codeResponses) {
        this.codeResponses = codeResponses;
    }

    public static CodeResponses of(List<CodeResponse> codeResponses) {
        return new CodeResponses(codeResponses);
    }
}
