package com.example.kotkit.entity.enums;

public enum VideoMode {
    PUBLIC,
    FRIEND,
    PRIVATE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
