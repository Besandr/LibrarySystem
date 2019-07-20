package com.library.model.Services;

import java.util.Objects;

public abstract class Service {

    protected boolean checkAndCastExecutingResult(Object executingResult) {
        if (Objects.nonNull(executingResult) && executingResult instanceof Boolean) {
            return (Boolean) executingResult;
        } else {
            return false;
        }
    }
}
