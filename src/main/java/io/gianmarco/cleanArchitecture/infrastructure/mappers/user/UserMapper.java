package io.gianmarco.cleanArchitecture.infrastructure.mappers.user;

import io.gianmarco.cleanArchitecture.domain.entities.User;
import io.gianmarco.cleanArchitecture.infrastructure.persistence.entities.user.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserJpaEntity toJpa(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        if (user.getId() != null) {
            // sólo copia id si ya existe
            entity.setId(user.getId());
        }
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setDisabled(user.isDisabled());
        entity.setRoles(user.getRoles());
        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        return User.restore(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getEmailVerified(),
            entity.getDisabled(),
            entity.getRoles(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
