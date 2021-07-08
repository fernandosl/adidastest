package com.adidas.test.pubservice.common.error;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@NoRepositoryBean
public abstract class RestExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class.getName());

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(Exception ex) {
    LOG.error("Error de servidor", ex);
    return buildResponseEntity(
        new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error de servidor",
            ex.getLocalizedMessage(), null, null));
  }

  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<Object> handleValidationException(Exception ex) {
    LOG.error("Error de validación", ex);

    ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error de validación",
        ex.getLocalizedMessage(), null, null);
    error.setSubCode("VALIDATION");

    return buildResponseEntity(error);
  }

  @ExceptionHandler(ApplicationException.class)
  protected ResponseEntity<Object> handleApplicationException(ApplicationException ex) {
    LOG.error("Error de aplicación", ex);
    ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getDetail(),
        ex.getDebug(), ex.getErrors(), ex.getCode());

    error.setSubCode("APPLICATION");

    return buildResponseEntity(error);
  }

  @ExceptionHandler(InvalidTokenException.class)
  protected ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex) {
    LOG.error("Error de token", ex);

    ApiError error = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error de token",
        ex.getLocalizedMessage(), null, null);
    error.setSubCode("INVALID_TOKEN");

    return buildResponseEntity(error);
  }

  protected ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<Object>(apiError, apiError.getHttpStatus());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    LOG.info("Campos incorrectos", ex);
    List<String> errors = new ArrayList<String>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }

    ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Campos incorrectos", null,
        ex.getLocalizedMessage(), errors);
    apiError.setSubCode("FIELDS");
    return handleExceptionInternal(
        ex, apiError, headers, apiError.getHttpStatus(), request);
  }

  //@ExceptionHandler(HttpMessageNotReadableException.class)
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    String error = "Datos con formato incorrecto";
    LOG.info(error, ex);
    return buildResponseEntity(
        new ApiError(HttpStatus.BAD_REQUEST, error, null, ex.getLocalizedMessage(), null));
  }
}
