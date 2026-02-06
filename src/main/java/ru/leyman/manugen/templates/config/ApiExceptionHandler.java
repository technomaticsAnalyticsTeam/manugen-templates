package ru.leyman.manugen.templates.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.leyman.manugen.templates.domain.error.NotFoundException;

@Log4j2
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Void> notFoundException(NotFoundException e) {
        log.warn(e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Void> exception(Exception e) {
        log.error(e);
        return ResponseEntity.internalServerError().build();
    }

}
