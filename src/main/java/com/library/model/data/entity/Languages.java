package com.library.model.data.entity;

import lombok.Getter;

/**
 * All supported by system languages for the view
 */
@Getter
public enum Languages {

    ENGLISH("English", "en"), RUSSIAN("Русский","ru"), UKRAINIAN("Українська","ua");

    /**
     * View language's name
     */
    String name;

    /**
     * Inner language's code
     */
    String code;

    Languages(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
