package springApp2.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER, ROLE_ADMIN;

    //  Возвращает роль в строковом виде
    @Override
    public String getAuthority() {
        return name();
    }
}
