package ru.leyman.manugen.templates.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.leyman.manugen.templates.domain.dto.TemplateDto;
import ru.leyman.manugen.templates.service.TemplateService;

import java.util.List;

@RestController
@RequestMapping("templates")
@RequiredArgsConstructor
public class TemplatesController {

    private final TemplateService templateService;

    @GetMapping("{id}")
    public TemplateDto find(@PathVariable Long id) {
        return templateService.find(id);
    }

    @GetMapping("all")
    public List<TemplateDto> findAll() {
        return templateService.findAll();
    }

    @PostMapping
    public TemplateDto create(@RequestBody TemplateDto templateDto) {
        return templateService.create(templateDto);
    }

    @PutMapping
    public TemplateDto update(@RequestBody TemplateDto templateDto) {
        return templateService.update(templateDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        templateService.delete(id);
    }

}
