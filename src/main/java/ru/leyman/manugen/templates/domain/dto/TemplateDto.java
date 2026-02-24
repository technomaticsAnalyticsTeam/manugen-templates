package ru.leyman.manugen.templates.domain.dto;

import ru.leyman.manugen.templates.domain.entity.Field;

import java.util.List;

public record TemplateDto(Long id, String name, String description, String category,
                          String filename, List<Field> fields) {
}
