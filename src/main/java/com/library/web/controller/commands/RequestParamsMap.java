package com.library.web.controller.commands;

/**
 * Contains constant names of view form fields.
 * This names are used by JSPs and by Controller
 * for getting corresponding parameter from request.
 * Also has mandatory getters for accessing to
 * them from the views.
 */
public class RequestParamsMap {
    public static final String COMMAND = "command";



    public String getCOMMAND() {
        return COMMAND;
    }
}
