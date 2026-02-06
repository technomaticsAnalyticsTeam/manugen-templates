package ru.leyman.manugen.templates.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.leyman.manugen.templates.domain.enums.Action;

@Entity
@Getter @Setter
@NoArgsConstructor
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    private Template template;

    @Enumerated(EnumType.ORDINAL)
    private Action action;

    public UserPermission(Long userId, Template template, Action action) {
        this.userId = userId;
        this.template = template;
        this.action = action;
    }

}
