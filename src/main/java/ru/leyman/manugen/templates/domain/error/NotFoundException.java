package ru.leyman.manugen.templates.domain.error;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> clazz, String searchParams) {
        super(String.format("Not found %s by %s", clazz.getSimpleName(), searchParams));
    }

}
