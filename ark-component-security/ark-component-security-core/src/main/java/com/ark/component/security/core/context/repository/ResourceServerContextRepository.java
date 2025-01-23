package com.ark.component.security.core.context.repository;

import com.ark.component.security.base.authentication.AuthUser;
import com.ark.component.security.base.authentication.Token;
import com.ark.component.security.core.authentication.AuthenticatedToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.util.Assert;

/**
 * 子服务的安全上下文存储库
 * 认证流程说明：
 * 1. 用户请求首先经过网关
 * 2. 网关将请求转发给认证中心进行token校验
 * 3. 认证中心校验通过后，请求才会被转发到具体的资源服务
 * 4. 由于请求已经过认证中心校验，资源服务只需要：
 *    - 验证JWT token的合法性（签名验证）
 *    - 从JWT中提取用户信息
 * 这种设计的优势：
 * 1. 减少了重复的认证服务调用
 * 2. 提高了系统性能和可用性
 * 3. 即使认证中心暂时不可用，已认证的请求仍能正常处理
 * 注意：如果需要获取用户的最新权限，仍需要调用认证中心
 */
@Slf4j
public class ResourceServerContextRepository extends AbstractSecurityContextRepository {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final JwtDecoder jwtDecoder;

    public ResourceServerContextRepository(JwtDecoder jwtDecoder) {
        Assert.notNull(jwtDecoder, "JwtDecoder must not be null");
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // 子服务不需要实现保存逻辑，因为token的创建和存储由认证中心负责
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return readSecurityContextFromJwt(requestResponseHolder.getRequest());
    }

    /**
     * 从JWT中读取安全上下文
     * 由于请求已经过网关和认证中心的校验，这里只需要：
     * 1. 验证JWT的签名是否合法
     * 2. 从JWT中提取用户信息
     */
    private SecurityContext readSecurityContextFromJwt(HttpServletRequest request) {
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        String accessToken = resolveToken(request);
        
        if (StringUtils.isEmpty(accessToken)) {
            if (log.isDebugEnabled()) {
                log.debug("No access token found in request");
            }
            return context;
        }

        try {
            Jwt jwt = jwtDecoder.decode(accessToken);
            // 分别提取用户信息和令牌信息
            AuthUser authUser = extractAuthUserFromJwt(jwt);
            Token token = extractTokenFromJwt(jwt, accessToken);
            context.setAuthentication(AuthenticatedToken.authenticated(authUser, token));
            return context;
        } catch (Exception e) {
            log.error("Failed to load security context from JWT", e);
            return context;
        }
    }

    /**
     * 从JWT中提取用户信息
     * JWT中包含了基本的用户信息（userId、userCode、username等）
     */
    private AuthUser extractAuthUserFromJwt(Jwt jwt) {
        try {
            AuthUser authUser = new AuthUser();
            authUser.setUserId(Long.parseLong(jwt.getClaimAsString(AuthUser.USER_ID)));
            authUser.setUserCode(jwt.getClaimAsString(AuthUser.USER_CODE));
            authUser.setUsername(jwt.getClaimAsString(AuthUser.USERNAME));
            authUser.setIsSuperAdmin(jwt.getClaimAsBoolean("isSuperAdmin"));
            return authUser;
        } catch (Exception e) {
            log.error("Failed to extract user info from JWT", e);
            throw new BadCredentialsException("Invalid user info in token");
        }
    }

    /**
     * 从JWT中提取令牌信息
     * 包括访问令牌、刷新令牌、过期时间、应用信息等
     */
    private Token extractTokenFromJwt(Jwt jwt, String accessToken) {
        try {
            return Token.of(
                accessToken,
                jwt.getClaimAsString("refresh_token"),
                jwt.getExpiresAt() != null ? jwt.getExpiresAt().getEpochSecond() : null,
                jwt.getClaimAsString(Token.APP_CODE),
                jwt.getClaimAsString(Token.APP_TYPE)
            );
        } catch (Exception e) {
            log.error("Failed to extract token info from JWT", e);
            throw new BadCredentialsException("Invalid token info");
        }
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        return super.loadDeferredContext(request);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        // 子服务不需要实现，因为上下文的存储由认证中心负责
        return false;
    }
} 