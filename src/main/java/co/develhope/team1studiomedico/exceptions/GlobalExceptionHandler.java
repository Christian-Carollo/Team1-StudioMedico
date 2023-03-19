package co.develhope.team1studiomedico.exceptions;

import co.develhope.team1studiomedico.dto.ResponseErrorDTO;
import co.develhope.team1studiomedico.dto.ResponseValidationErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * GlobalExceptionHandler è uno speciale controller che consente di gestire le eccezioni
 * nell'intera applicazione in un unico componente di gestione globale mediante relativi metodi @ExceptionHandler.
 * Può essere visto come un interceptor di eccezioni lanciate da metodi annotati con @RequestMapping e simili.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Metodo che gestisce le eccezioni EntityNotFoundException
     * @param e oggetto eccezione di tipo EntityNotFoundException
     * @return response con status di errore 404
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseErrorDTO(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(), e.getMessage(), request.getRequestURI()));
    }

    /**
     * Metodo che gestisce le eccezioni RuntimeException
     * @param e oggetto eccezione di tipo RuntimeException
     * @return response con status di errore 400
     */
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, DateTimeException.class})
    public ResponseEntity handleIllegalArgumentTypeMismatchParseException(RuntimeException e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseErrorDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage(), request.getRequestURI()));
    }

    /**
     * Metodo che gestisce le eccezioni NullPointerException
     * @param e oggetto eccezione di tipo NullPointerException
     * @return response con status di errore 500
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseErrorDTO(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Internal Server Error - null object", request.getRequestURI()));
    }

    /**
     * Metodo che gestisce le eccezioni DataAccessException
     * @param e oggetto eccezioni di tipo DataAccessException
     * @return response con status di errore 500
     */
    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataIntegrityViolationException.class})
    public ResponseEntity handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        if(e instanceof DataIntegrityViolationException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseErrorDTO(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Database persistence statement error", request.getRequestURI()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ResponseErrorDTO(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBeanValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, List<String>> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            if(!errors.containsKey(fieldName)) {
                errors.put(fieldName, new ArrayList<>());
            }
        });
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            if(errors.containsKey(fieldName)) {
                errors.get(fieldName).add(errorMessage);
            }
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseValidationErrorDTO(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(), "Field validation failed", request.getRequestURI(), errors));
    }

}
