package ru.leyman.manugen.templates.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.manugen.templates.service.TemplateFileService;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FilesController {

    private final TemplateFileService templateFileService;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody Resource download(@PathVariable Long id) {
        return templateFileService.download(id);
    }

    /**
     * Public endpoint for serving template files to generator service.
     * No authentication required - for internal service communication.
     */
    @GetMapping(value = "{id}/public", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody Resource downloadPublic(@PathVariable Long id) {
        return templateFileService.download(id);
    }

    @PostMapping("{id}")
    public void upload(@PathVariable Long id, @RequestBody MultipartFile file) {
        templateFileService.upload(id, file);
    }

}
