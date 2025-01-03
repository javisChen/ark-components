package com.ark.component.security.core.context.repository;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSONArray;
import com.ark.component.cache.CacheService;
import com.ark.component.security.base.user.AuthUser;
import com.ark.component.security.core.authentication.AuthenticatedToken;
import com.ark.component.security.core.common.RedisKeyUtils;
import com.ark.component.security.core.common.SecurityConstants;
import com.ark.component.security.core.userdetails.LoginUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis安全上下文存储库
 * 负责在Redis中存储和获取用户认证信息
 *
 * @author JC
 */
@Slf4j
public class RedisSecurityContextRepository extends AbstractSecurityContextRepository {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final BearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
    private final CacheService cacheService;

    /**
     * LoginUser对象的属性列表，用于Redis hash结构存储
     */
    private final List<Object> hashKeys = List.of(
            AuthUser.USER_ID,
            AuthUser.USER_CODE,
            AuthUser.IS_SUPER_ADMIN,
            "password",
            AuthUser.USERNAME,
            "authorities",
            "accountNonExpired",
            "accountNonLocked",
            "credentialsNonExpired",
            "enabled");

    public RedisSecurityContextRepository(CacheService cacheService, LoginUserDetailsService loginUserDetailsService) {
        Assert.notNull(cacheService, "CacheService must not be null");
        Assert.notNull(loginUserDetailsService, "LoginUserDetailsService must not be null");
        this.cacheService = cacheService;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = context.getAuthentication();

        // logout时authentication为空，直接返回
        if (authentication == null) {
            if (log.isDebugEnabled()) {
                log.debug("Authentication is null, skip saving context");
            }
            return;
        }

        try {
            AuthenticatedToken authenticatedToken = (AuthenticatedToken) authentication;
            AuthUser authUser = authenticatedToken.getAuthUser();
            String accessToken = authenticatedToken.getAccessToken();

            if (log.isDebugEnabled()) {
                log.debug("Saving security context for user: {}", authUser.getUsername());
            }

            // 将LoginUser对象转换为Map并存储到Redis
            Map<String, Object> map = BeanUtil.beanToMap(authUser, false, true);
            map.put("authorities", authenticatedToken.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));

            cacheService.hMSet(RedisKeyUtils.createAccessTokenKey(accessToken), 
                    map, SecurityConstants.TOKEN_EXPIRES_SECONDS);

            // 存储用户ID到token的映射关系
            cacheService.set(RedisKeyUtils.createUserIdKey(authUser.getUserId()),
                    accessToken, SecurityConstants.TOKEN_EXPIRES_SECONDS, TimeUnit.SECONDS);

            if (log.isDebugEnabled()) {
                log.debug("Successfully saved security context for user: {}", authUser.getUsername());
            }
        } catch (Exception e) {
            log.error("Failed to save security context", e);
            throw new IllegalStateException("Failed to save security context", e);
        }
    }

    private SecurityContext readSecurityContextFromCache(HttpServletRequest request) {
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        String accessToken = resolveToken(request);
        
        if (StringUtils.isEmpty(accessToken)) {
            if (log.isDebugEnabled()) {
                log.debug("No access token found in request");
            }
            return context;
        }

        try {
            if (log.isDebugEnabled()) {
                log.debug("Loading security context for token: {}", accessToken);
            }

            List<Object> values = cacheService.hMGet("auth", 
                    RedisKeyUtils.createAccessTokenKey(accessToken), hashKeys);
            
            if (CollectionUtils.isEmpty(values) || 
                CollectionUtils.isEmpty(values.stream().filter(Objects::nonNull).collect(Collectors.toList()))) {
                if (log.isDebugEnabled()) {
                    log.debug("No security context found for token: {}", accessToken);
                }
                return context;
            }

            AuthUser authUser = assemble(values);
            context.setAuthentication(AuthenticatedToken.authenticated(authUser, accessToken, "", 0L));

            if (log.isDebugEnabled()) {
                log.debug("Successfully loaded security context for user: {}", authUser.getUsername());
            }

            return context;
        } catch (Exception e) {
            log.error("Failed to load security context", e);
            return context;
        }
    }

    /**
     * 从请求中解析访问令牌
     */
    protected String resolveToken(HttpServletRequest request) {
        return bearerTokenResolver.resolve(request);
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return readSecurityContextFromCache(requestResponseHolder.getRequest());
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        return super.loadDeferredContext(request);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String token = resolveToken(request);
        return cacheService.get(RedisKeyUtils.createAccessTokenKey(token), AuthUser.class) != null;
    }

    /**
     * 将Redis中的数据组装成LoginUser对象
     */
    private AuthUser assemble(List<Object> objects) {
        AuthUser authUser = new AuthUser();
        authUser.setUserId(Long.parseLong(objects.get(0).toString()));
        authUser.setUserCode(String.valueOf(objects.get(1)));
        authUser.setIsSuperAdmin((Boolean) objects.get(2));
        authUser.setUsername(String.valueOf(objects.get(4)));
        
        JSONArray authorities = (JSONArray) objects.get(5);
        authUser.setAuthorities(authorities.stream()
                .map(item -> new SimpleGrantedAuthority((String) item))
                .collect(Collectors.toUnmodifiableSet()));
                
        authUser.setAccountNonExpired((Boolean) objects.get(6));
        authUser.setAccountNonLocked((Boolean) objects.get(7));
        authUser.setCredentialsNonExpired((Boolean) objects.get(8));
        authUser.setEnabled((Boolean) objects.get(9));
        return authUser;
    }
}
