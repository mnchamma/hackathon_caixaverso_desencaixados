package com.acessibilidade.api.exception;

import com.acessibilidade.api.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErroResponse> handleApiException(ApiException exception) {
        HttpStatus status = exception.getStatus();
        ErroResponse response = new ErroResponse(
                status.value(),
                status.getReasonPhrase(),
                List.of(exception.getMessage())
        );

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> detalhes = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErroResponse response = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detalhes
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleUnexpected(Exception exception) {
        ErroResponse response = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                List.of("Erro inesperado ao processar a requisicao.")
        );

        return ResponseEntity.internalServerError().body(response);
    }
}
