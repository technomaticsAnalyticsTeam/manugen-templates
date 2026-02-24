package ru.leyman.manugen.templates.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.leyman.manugen.templates.domain.entity.UserPermission;
import ru.leyman.manugen.templates.domain.error.UserPermissionNotFoundException;
import ru.leyman.manugen.templates.repo.TemplateRepo;
import ru.leyman.manugen.templates.repo.UserPermissionRepo;

import java.io.FileNotFoundException;
import java.util.Set;

import static ru.leyman.manugen.templates.utils.UserContext.getUserId;
import static ru.leyman.manugen.templates.domain.enums.Action.*;

@Service
@RequiredArgsConstructor
public class TemplateFileService {

    private final UserPermissionRepo userPermissionRepo;
    private final TemplateRepo templateRepo;
    private final FileService fileService;

    public void upload(Long id, MultipartFile file) {
        Long userId = getUserId();
        userPermissionRepo.findByUserIdAndTemplateId(userId, id)
                .filter(userPermission ->
                        Set.of(WRITE, SHARE, DELETE).contains(userPermission.getAction()))
                .map(UserPermission::getTemplate)
                .ifPresentOrElse(template -> {
                            template.setFilename(fileService.upload(file, template.getFilename()));
                            templateRepo.save(template);
                        },
                        () -> {
                            throw new UserPermissionNotFoundException(userId, id);
                        });
    }

    public Resource download(Long id) {
        var userId = getUserId();
        return userPermissionRepo.findByUserIdAndTemplateId(userId, id)
                .map(UserPermission::getTemplate)
                .map(template -> {
                    try {
                        return fileService.download(template.getFilename());
                    } catch (FileNotFoundException e) {
                        template.setFilename(null);
                        templateRepo.save(template);
                        return null;
                    }
                })
                .orElseThrow(() -> new UserPermissionNotFoundException(userId, id));
    }

}
