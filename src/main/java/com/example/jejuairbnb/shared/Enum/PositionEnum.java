package com.example.jejuairbnb.shared.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PositionEnum {
    JEJU( "제주"),
    JEJU_CITY( "제주시"),
    AEWOL("애월"),
    HALLIM("한림"),
    HANKYUNG("한경");

    private final String positionName;

    @Override
    public String toString() {
        return positionName;
    }
}
