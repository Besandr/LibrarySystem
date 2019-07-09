package com.library.web.controller.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Represents an unknown command received from request
 */
public class UnknownCommand extends FrontCommand {

    private static final Logger log = LogManager.getLogger(UnknownCommand.class);

    /**
     * Logs exception situation with unknown command and forwards
     * to error page
     */
    @Override
    public void process() throws ServletException, IOException {
        log.error(String.format("Received unknown command \"%s\" from view.", request.getParameter(RequestParameterNamesDict.COMMAND)));
        forward("error");
    }

}
