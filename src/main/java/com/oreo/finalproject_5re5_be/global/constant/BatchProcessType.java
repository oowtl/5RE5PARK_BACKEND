package com.oreo.finalproject_5re5_be.global.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum BatchProcessType {
    CREATE("CREATE"),
    UPDATE("UPDATE");

    private final String type;

    BatchProcessType(String type) {
        this.type = type;
    }

    @JsonCreator
    public BatchProcessType from(String value) {
        return BatchProcessType.valueOf(value.toUpperCase());
    }
}
