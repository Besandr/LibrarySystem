package com.library.model.Services;

import java.util.Objects;
import java.util.Optional;

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

    protected static <T> Optional<T> checkAndCastToOptional(Object optionalCandidate) {
        if (Objects.nonNull(optionalCandidate) && optionalCandidate instanceof Optional) {
            return (Optional<T>) optionalCandidate;
        } else {
            return Optional.empty();
        }
    }
}
