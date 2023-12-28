package com.ark.component.security.core.context.repository;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONArray;
import com.ark.component.cache.CacheService;
import com.ark.component.security.base.user.LoginUser;
import com.ark.component.security.core.authentication.LoginAuthenticationToken;
import com.ark.component.security.core.config.SecurityConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.context.HttpRequestResponseHolder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class RedisSecurityContextRepository extends AbstractSecurityContextRepository {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final CacheService cacheService;
    private final JwtDecoder jwtDecoder;
    private final List<Object> hashKeys = List.of(
            "userId",
            "userCode",
            "isSuperAdmin",
            "password",
            "username",
            "authorities",
            "accountNonExpired",
            "accountNonLocked",
            "credentialsNonExpired",
            "enabled");

    public RedisSecurityContextRepository(CacheService cacheService, JwtDecoder jwtDecoder) {
        this.cacheService = cacheService;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication  = context.getAuthentication();

        // logout的时候authentication就会为空
        if (authentication == null) {
            return;
        }

        LoginAuthenticationToken loginAuthenticationToken = (LoginAuthenticationToken)authentication;

        LoginUser loginUser = loginAuthenticationToken.getLoginUser();

        String accessToken = loginAuthenticationToken.getAccessToken();

        Map<String, Object> map = BeanUtil.beanToMap(loginUser, false, true);
        map.put("authorities", loginAuthenticationToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        cacheService.hMSet(RedisKeyUtils.createAccessTokenKey(accessToken), map, SecurityConstants.TOKEN_EXPIRES_SECONDS);

        cacheService.set(RedisKeyUtils.createUserIdKey(loginUser.getUserId()), accessToken, SecurityConstants.TOKEN_EXPIRES_SECONDS);

    }

    private SecurityContext readSecurityContextFromCache(HttpServletRequest request) {
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        String accessToken = resolveToken(request);
        if (StringUtils.isEmpty(accessToken)) {
            return context;
        }
        List<Object> values = cacheService.hMGet(RedisKeyUtils.createAccessTokenKey(accessToken), hashKeys);
        if (CollectionUtils.isEmpty(values)) {
            return context;
        }
        if (CollectionUtils.isEmpty(values.stream().filter(Objects::nonNull).toList())) {
            return context;
        }

        Jwt jwt = decodeToken(accessToken);
        Object userCode = jwt.getClaim(LoginUser.JWT_CLAIM_USER_CODE);
        Object username = jwt.getClaim(LoginUser.JWT_CLAIM_USERNAME);
        Object userId = jwt.getClaim(LoginUser.JWT_CLAIM_USER_ID);
        Object isSuperAdmin = jwt.getClaim(LoginUser.JWT_CLAIM_USER_IS_SUPER_ADMIN);
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(Long.parseLong(userId.toString()));
        loginUser.setUserCode(String.valueOf(userCode));
        loginUser.setIsSuperAdmin((Boolean) isSuperAdmin);
        loginUser.setUsername(String.valueOf(username));
        // 权限从缓存中取
        JSONArray authorities = (JSONArray) values.get(5);
        loginUser.setAuthorities(authorities.stream()
                .map(item -> new SimpleGrantedAuthority((String) item))
                .collect(Collectors.toUnmodifiableSet()));
        // LoginUser loginUser = convert(values);
        context.setAuthentication(new LoginAuthenticationToken(loginUser, accessToken));
        return context;
    }

    private Jwt decodeToken(String accessToken) {
        Jwt jwt = null;
        try {
            jwt = jwtDecoder.decode(accessToken);
        } catch (JwtException e) {
            log.error("解析JWT失败", e);
            throw new BadCredentialsException("无效凭证");
        }
        return jwt;
    }

    private LoginUser convert(List<Object> objects) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(Long.parseLong(objects.get(0).toString()));
        loginUser.setUserCode(String.valueOf(objects.get(1)));
        loginUser.setIsSuperAdmin((Boolean) objects.get(2));
        loginUser.setUsername(String.valueOf(objects.get(4)));
        JSONArray authorities = (JSONArray) objects.get(5);
        loginUser.setAuthorities(authorities.stream()
                .map(item -> new SimpleGrantedAuthority((String) item))
                .collect(Collectors.toUnmodifiableSet()));
        loginUser.setAccountNonExpired((Boolean) objects.get(6));
        loginUser.setAccountNonLocked((Boolean) objects.get(7));
        loginUser.setCredentialsNonExpired((Boolean) objects.get(8));
        loginUser.setEnabled((Boolean) objects.get(9));
        return loginUser;
    }

    protected String resolveToken(HttpServletRequest request) {
        return bearerTokenResolver.resolve(request);
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return readSecurityContextFromCache(requestResponseHolder.getRequest());
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String token = resolveToken(request);
        return cacheService.get(RedisKeyUtils.createAccessTokenKey(token), LoginUser.class) != null;
    }


}
