package com.marcos.dev.zentasks.zen_task_api.common.exceptions;

import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.ErrorResponseDTO;
import com.marcos.dev.zentasks.zen_task_api.modules.tasks.Application.dtos.ValidationErrorResponseDTO;
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

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponseDTO> handlerBusinnesRuleException(BusinessRuleException ex) {
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

    /**
     * Handles exceptions of type ForbiddenAccessException and transforms them into an HTTP response
     * with a standardized error body. This method is triggered whenever a ForbiddenAccessException
     * is thrown in the application context.
     *
     * @param ex the ForbiddenAccessException instance that was thrown
     * @return a ResponseEntity containing an ErrorResponseDTO with HTTP status 403 (FORBIDDEN)
     *         and details about the forbidden access attempt
     */

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

    /**
     * Handles exceptions of type InvalidInputException and transforms them into an HTTP response
     * with a standardized error body. This method is triggered whenever an InvalidInputException
     * is thrown in the application context.
     *
     * @param ex the InvalidInputException instance that was thrown
     * @return a ResponseEntity containing an ErrorResponseDTO with HTTP status 400 (BAD_REQUEST)
     *         and details about the invalid input
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponseDTO> handlerInvalidInputException(InvalidInputException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Dados invalidos",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handlerBadRequestException(BadRequestException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Dados invalidos",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDTO> handlerInvalidTokenException(InvalidTokenException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Token Invalido!",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<ErrorResponseDTO> handlerTokenGenerationException(TokenGenerationException ex) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro ao gerar o token!",
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handlerUserValidationException(UserValidationException ex) {
        ValidationErrorResponseDTO errorResponseDTO = new ValidationErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                ex.getMessage(),
                LocalDateTime.now(),
                ex.getErrors()
        );

        return new ResponseEntity<>(
                errorResponseDTO,
                HttpStatus.BAD_REQUEST
        );
    }
}
