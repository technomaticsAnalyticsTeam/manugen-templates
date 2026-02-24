package ru.leyman.manugen.templates.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.leyman.manugen.templates.service.FieldsAttributeConverter;

import java.util.List;

@Entity
@Getter @Setter
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String category;

    private String filename;

    @Convert(converter = FieldsAttributeConverter.class)
    private List<Field> fields;

}
