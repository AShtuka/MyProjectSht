package ua.com.osht.myproject.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, NONAME,  ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
