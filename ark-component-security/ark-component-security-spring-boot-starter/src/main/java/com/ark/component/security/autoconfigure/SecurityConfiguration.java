package com.ark.component.security.autoconfigure;

import com.ark.component.security.core.configurers.ResourceServerHttpConfigurer;
import com.ark.component.security.core.configurers.CommonHttpConfigurer;
import com.ark.component.security.core.context.repository.ResourceServerContextRepository;
import com.ark.component.security.core.token.generate.JwtTokenGenerator;
import com.ark.component.security.core.token.generate.TokenGenerator;
import com.ark.component.security.core.token.issuer.TokenIssuer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfiguration {

    @Bean
    public TokenGenerator tokenGenerator(JwtEncoder jwtEncoder) {
        return new JwtTokenGenerator(jwtEncoder);
    }

    @Bean
    public TokenIssuer tokenIssuer(TokenGenerator tokenGenerator) {
        return new TokenIssuer(tokenGenerator);
    }

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
