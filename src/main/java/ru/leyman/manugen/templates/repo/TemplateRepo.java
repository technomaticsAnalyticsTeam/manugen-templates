package ru.leyman.manugen.templates.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.leyman.manugen.templates.domain.entity.Template;

@Repository
public interface TemplateRepo extends JpaRepository<Template, Long> {

}
