package com.example.kotkit.entity.enums;

public enum VideoMode {
    PUBLIC,
    PRIVATE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
