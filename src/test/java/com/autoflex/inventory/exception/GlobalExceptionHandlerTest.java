package com.autoflex.inventory.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleResourceNotFound_ShouldReturnNotFound() {
        // Deve retornar status 404 Not Found quando uma ResourceNotFoundException for lançada
        ResourceNotFoundException ex = new ResourceNotFoundException("Not Found");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().get("error"));
    }

    @Test
    void handleValidation_ShouldReturnBadRequest() {
        // Deve retornar status 400 Bad Request com detalhes dos campos inválidos quando ocorrer erro de validação
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "defaultMessage");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidation(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> fields = (Map<String, String>) response.getBody().get("fields");
        assertEquals("defaultMessage", fields.get("field"));
    }

    @Test
    void handleGeneral_ShouldReturnInternalServerError() {
        // Deve retornar status 500 Internal Server Error para exceções genéricas não tratadas
        Exception ex = new Exception("Error");
        ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ocorreu um erro inesperado", response.getBody().get("error"));
    }
}
