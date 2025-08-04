package com.marcos.dev.zentasks.zen_task_api.tasks.exceptions;

import com.marcos.dev.zentasks.zen_task_api.tasks.dtos.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Captura exceções lançadas pelos controllers de forma global
 * e as converte em respostas HTTP padronizadas.
 */
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * Manipulador específico para a nossa ResourceNotFoundException.
     * Será acionado sempre que essa exceção for lançada por qualquer parte do código.
     *
     * @param ex A exceção capturada.
     * @return Uma ResponseEntity com status 404 (NOT_FOUND) e um corpo de erro padronizado.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso nao encontrado",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions of type BusinnesRuleException and transforms them into an HTTP response
     * with a standardized error body. This method is triggered whenever a BusinnesRuleException
     * is thrown in the application context.
     *
     * @param ex the BusinnesRuleException instance that was thrown
     * @return a ResponseEntity containing an ErrorResponseDTO with HTTP status 400 (BAD_REQUEST)
     *         and details about the business rule violation
     */

    @ExceptionHandler(BusinnesRuleException.class)
    public ResponseEntity<ErrorResponseDTO> handlerBusinnesRuleException(BusinnesRuleException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Regra de negocio invalida",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions of type DataConflictException and converts them into an HTTP response
     * with a standardized error body. This method is triggered whenever a DataConflictException
     * is thrown in the application context.
     *
     * @param ex the DataConflictException instance that was thrown
     * @return a ResponseEntity containing an ErrorResponseDTO with HTTP status 409 (CONFLICT)
     *         and details about the conflict
     */
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handlerDataConflictException(DataConflictException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Recurso ja existe!",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handlerForbiddenAccessException(ForbiddenAccessException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acesso negado!",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.FORBIDDEN);
    }
}
