package com.ark.component.security.autoconfigure;

import com.ark.component.security.core.configurers.CommonHttpConfigurer;
import com.ark.component.security.core.configurers.ResourceServerHttpConfigurer;
import com.ark.component.security.core.context.repository.ResourceServerContextRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain serviceSecurityFilterChain(HttpSecurity httpSecurity, JwtDecoder jwtDecoder) throws Exception {
        httpSecurity
                .securityContext(c ->
                        c.securityContextRepository(new ResourceServerContextRepository(jwtDecoder))
                )
                .with(new CommonHttpConfigurer(), configurer -> {})
                .with(new ResourceServerHttpConfigurer(), configurer -> {});
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
