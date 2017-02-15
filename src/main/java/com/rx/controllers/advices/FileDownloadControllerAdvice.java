package com.rx.controllers.advices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by multi-view on 2/15/17.
 */

@ControllerAdvice
public class FileDownloadControllerAdvice {

    private Logger logger = LogManager.getLogger(FileDownloadControllerAdvice.class);

    @ExceptionHandler(IOException.class)
    public void handle(IOException ex, HttpServletResponse response) {
        logger.warn("Error when preparing file to downloading!", ex);
        response.setStatus(500);
    }
}
