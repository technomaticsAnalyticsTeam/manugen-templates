package ru.leyman.manugen.templates.domain.error;

import ru.leyman.manugen.templates.domain.entity.UserPermission;

public class UserPermissionNotFoundException extends NotFoundException{

    public UserPermissionNotFoundException(Long userId, Long templateId) {
        super(UserPermission.class, String.format("userId = %d, templateId = %d", userId, templateId));
    }

}
