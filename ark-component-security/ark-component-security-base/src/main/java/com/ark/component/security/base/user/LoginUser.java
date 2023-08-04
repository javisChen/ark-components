package com.ark.component.security.base.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Set;

@Setter
@Getter
public class LoginUser implements UserDetails, CredentialsContainer {

    public final static String JWT_CLAIM_USER_ID = "userId";
    public final static String JWT_CLAIM_USER_CODE = "userCode";
    public final static String JWT_CLAIM_USERNAME = "username";
    public final static String JWT_CLAIM_USER_IS_SUPER_ADMIN = "isSuperAdmin";

    private Long userId;
    private String userCode;
    private Boolean isSuperAdmin;
    private String password;
    private String username;
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
