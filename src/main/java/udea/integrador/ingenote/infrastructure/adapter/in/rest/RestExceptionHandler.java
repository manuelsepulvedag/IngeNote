package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import udea.integrador.ingenote.application.exception.CredencialesInvalidasException;
import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ReglaDeNegocioException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> regla(ReglaDeNegocioException exception) { return Map.of("mensaje", exception.getMessage()); }
    @ExceptionHandler(CredencialesInvalidasException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> credenciales(CredencialesInvalidasException exception) { return Map.of("mensaje", exception.getMessage()); }
}
