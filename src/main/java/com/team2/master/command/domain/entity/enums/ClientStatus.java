package com.team2.master.command.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ClientStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String dbValue;

    public static ClientStatus fromDbValue(String dbValue) {
        return Arrays.stream(values())
                .filter(s -> s.dbValue.equals(dbValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ClientStatus dbValue: " + dbValue));
    }
}
