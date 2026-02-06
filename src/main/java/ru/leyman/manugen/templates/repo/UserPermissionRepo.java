package ru.leyman.manugen.templates.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.leyman.manugen.templates.domain.entity.UserPermission;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPermissionRepo extends JpaRepository<UserPermission, Long> {

    Optional<UserPermission> findByUserIdAndTemplateId(Long userId, Long templateId);

    List<UserPermission> findAllByUserId(Long userId);

}
