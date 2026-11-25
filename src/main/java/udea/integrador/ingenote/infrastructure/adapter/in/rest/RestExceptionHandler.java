package udea.integrador.ingenote.infrastructure.adapter.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import udea.integrador.ingenote.application.exception.AlmacenamientoException;
import udea.integrador.ingenote.application.exception.CredencialesInvalidasException;
import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ReglaDeNegocioException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> regla(ReglaDeNegocioException exception) {
        return Map.of("mensaje", exception.getMessage());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> credenciales(CredencialesInvalidasException exception) {
        return Map.of("mensaje", exception.getMessage());
    }

    @ExceptionHandler(AlmacenamientoException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    Map<String, String> almacenamiento(AlmacenamientoException exception) {
        return Map.of("mensaje", exception.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    Map<String, String> archivoMuyGrande() {
        return Map.of("mensaje", "El archivo supera el tamaño máximo permitido de 20 MB");
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> parametroFaltante(Exception exception) {
        return Map.of("mensaje", "Falta un campo obligatorio en la petición: " + exception.getMessage());
    }
}