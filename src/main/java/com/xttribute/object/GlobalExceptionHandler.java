package com.xttribute.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ModelAndView jsonError(String message, HttpStatus status) {
        ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
        mv.addObject("error", message);
        mv.setStatus(status);
        return mv;
    }

    @ExceptionHandler(MultipartException.class)
    public ModelAndView handleMultipartException(MultipartException ex, WebRequest request) {
        LOGGER.warn("MultipartException: {}", ex.getMessage());
        return jsonError("Invalid multipart request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSize(MaxUploadSizeExceededException ex, WebRequest request) {
        LOGGER.warn("MaxUploadSizeExceededException: {}", ex.getMessage());
        return jsonError("Uploaded file is too large", HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ModelAndView handleMissingPart(MissingServletRequestPartException ex, WebRequest request) {
        LOGGER.warn("MissingServletRequestPartException: {}", ex.getMessage());
        return jsonError("Missing multipart part: " + ex.getRequestPartName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParam(MissingServletRequestParameterException ex, WebRequest request) {
        LOGGER.warn("MissingServletRequestParameterException: {}", ex.getMessage());
        return jsonError("Missing request parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ModelAndView handleUnsupportedMedia(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        LOGGER.warn("HttpMediaTypeNotSupportedException: {}", ex.getMessage());
        return jsonError("Unsupported media type: " + ex.getContentType(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView handleNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        LOGGER.warn("HttpMessageNotReadableException: {}", ex.getMessage());
        return jsonError("Malformed request body: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleOther(Exception ex, WebRequest request) {
        LOGGER.error("Unhandled exception: ", ex);
        return jsonError("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}