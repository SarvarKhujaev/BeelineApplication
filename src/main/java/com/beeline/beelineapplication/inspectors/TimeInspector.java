package com.beeline.beelineapplication.inspectors;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class TimeInspector extends StringOperations {
    protected Date newDate () {
        return new Date();
    }

    /*
    проверяет что срок хранения токена не истек
     */
    protected boolean checkTokenDate (
            final Date date
    ) {
        /*
        пока что, оставим срок в 3 дня
         */
        return Duration.between(
                date.toInstant(), Instant.now()
        ).getSeconds() <= 86400 * 3;
    }
}
