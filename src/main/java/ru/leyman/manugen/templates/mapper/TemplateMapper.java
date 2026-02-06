package ru.leyman.manugen.templates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.leyman.manugen.templates.domain.dto.TemplateDto;
import ru.leyman.manugen.templates.domain.entity.Template;

@Mapper
public interface TemplateMapper {

    TemplateDto map(Template template);

    Template map(TemplateDto templateDto);

    Template map(@MappingTarget Template template, TemplateDto templateDto);

}
