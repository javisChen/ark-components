package com.ark.component.security.base.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Set;

@Setter
@Getter
public class AuthUser implements UserDetails, CredentialsContainer {

    public static final String USER_ID = "userId";
    public static final String USER_CODE = "userCode";
    public static final String USERNAME = "username";
    
    private Long userId;
    private String userCode;
    private Boolean isSuperAdmin;
    private String password;
    private String username;
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
