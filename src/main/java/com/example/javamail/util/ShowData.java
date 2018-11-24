package com.example.javamail.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ShowData {

    default Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

}
