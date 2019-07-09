package com.library.web.controller.commands;

import lombok.Getter;

/**
 * Contains constant names of view form fields.
 * This names are used by JSPs and by {@code FrontCommand}
 * for getting corresponding parameter from request.
 * Also has mandatory getters for accessing to
 * them from the views.
 */
public class RequestParameterNamesDict {
    @Getter public static final String COMMAND = "command";


}
