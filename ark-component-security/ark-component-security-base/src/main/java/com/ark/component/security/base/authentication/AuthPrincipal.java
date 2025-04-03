package com.ark.component.security.base.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Set;

@Setter
@Getter
public class AuthPrincipal implements UserDetails, CredentialsContainer {

    public static final String PRINCIPAL_ID = "principalId";
    public static final String PRINCIPAL_CODE = "principalCode";
    public static final String PRINCIPAL_NAME = "principalName";
    
    private Long principalId;
    private String principalCode;
    private Boolean isAdmin;
    private String credentials;
    private String principalName;
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;

    @Override
    public void eraseCredentials() {
        this.credentials = null;
    }

    @Override
    public String getPassword() {
        return this.credentials;
    }

    @Override
    public String getUsername() {
        return this.principalName;
    }
}
