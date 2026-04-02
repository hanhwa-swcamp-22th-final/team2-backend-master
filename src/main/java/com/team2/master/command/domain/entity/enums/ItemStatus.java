package com.team2.master.command.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ItemStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String dbValue;

    public static ItemStatus fromDbValue(String dbValue) {
        return Arrays.stream(values())
                .filter(s -> s.dbValue.equals(dbValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ItemStatus dbValue: " + dbValue));
    }
}
