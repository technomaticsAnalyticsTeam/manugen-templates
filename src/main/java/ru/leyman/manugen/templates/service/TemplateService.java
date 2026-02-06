package ru.leyman.manugen.templates.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.leyman.manugen.templates.domain.dto.TemplateDto;
import ru.leyman.manugen.templates.domain.entity.UserPermission;
import ru.leyman.manugen.templates.domain.error.UserPermissionNotFoundException;
import ru.leyman.manugen.templates.mapper.TemplateMapper;
import ru.leyman.manugen.templates.repo.TemplateRepo;
import ru.leyman.manugen.templates.repo.UserPermissionRepo;

import java.util.List;
import java.util.Set;

import static ru.leyman.manugen.templates.UserContext.getUserId;
import static ru.leyman.manugen.templates.domain.enums.Action.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepo templateRepo;
    private final UserPermissionRepo userPermissionRepo;
    private final TemplateMapper templateMapper;

    public TemplateDto find(Long id) {
        Long userId = getUserId();
        return userPermissionRepo.findByUserIdAndTemplateId(userId, id)
                .map(UserPermission::getTemplate)
                .map(templateMapper::map)
                .orElseThrow(() -> new UserPermissionNotFoundException(userId, id));
    }

    public List<TemplateDto> findAll() {
        Long userId = getUserId();
        return userPermissionRepo.findAllByUserId(userId).stream()
                .map(UserPermission::getTemplate)
                .map(templateMapper::map)
                .toList();
    }

    public TemplateDto create(TemplateDto templateDto) {
        Long userId = getUserId();
        var template = templateRepo.save(templateMapper.map(templateDto));
        userPermissionRepo.save(new UserPermission(userId, template, DELETE));
        return templateMapper.map(template);
    }

    public TemplateDto update(TemplateDto templateDto) {
        Long userId = getUserId();
        return userPermissionRepo.findByUserIdAndTemplateId(userId, templateDto.id())
                .filter(userPermission ->
                        Set.of(WRITE, SHARE, DELETE).contains(userPermission.getAction()))
                .map(userPermission -> templateMapper.map(userPermission.getTemplate(), templateDto))
                .map(template -> templateMapper.map(templateRepo.save(template)))
                .orElseThrow(() -> new UserPermissionNotFoundException(userId, templateDto.id()));
    }

    public void delete(Long id) {
        Long userId = getUserId();
        userPermissionRepo.findByUserIdAndTemplateId(userId, id)
                .filter(userPermission -> DELETE == userPermission.getAction())
                .ifPresentOrElse(userPermissionRepo::delete,
                        () -> {
                            throw new UserPermissionNotFoundException(userId, id);
                });
    }

}
