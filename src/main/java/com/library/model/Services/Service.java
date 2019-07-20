package com.library.model.Services;

import java.util.Objects;

public abstract class Service {

    final boolean EXECUTING_SUCCESSFUL = true;
    final boolean EXECUTING_FAILED = false;

    boolean checkAndCastExecutingResult(Object executingResult) {
        if (Objects.nonNull(executingResult) && executingResult instanceof Boolean) {
            return (Boolean) executingResult;
        } else {
            return false;
        }
    }
}
