package com.library.web.controller;

import lombok.Data;

import java.util.HashMap;

/**
 * Errors occurred during {@code Form} validation
 */
@Data
public class ActionErrors {

    /**
     * Map with errors occurred during {@code Form} validation
     */
    private HashMap<String, String> errorsMap = new HashMap<>();

    /**
     * {@code Form} validation has errors marker
     */
    private boolean hasErrors = false;

    /**
     * Adds {@code Form} error to errors map
     * @param errorName - a name of occurred error
     * @param errorMessage - message of occurred error
     */
    public void addError(String errorName, String errorMessage) {
        errorsMap.put(errorName, errorMessage);
        hasErrors = true;
    }
}
