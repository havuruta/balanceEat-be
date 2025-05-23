package com.balanceeat.demo.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    OTHER("기타");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    @JsonValue
    public String getValue() {
        return name();
    }

    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static Gender fromValue(String value) {
        for (Gender gender : values()) {
            if (gender.name().equals(value)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender value: " + value);
    }
} 