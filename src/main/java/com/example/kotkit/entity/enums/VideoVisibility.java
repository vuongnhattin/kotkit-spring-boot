package com.example.kotkit.entity.enums;

public enum VideoVisibility {
    PUBLIC,
    PRIVATE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
