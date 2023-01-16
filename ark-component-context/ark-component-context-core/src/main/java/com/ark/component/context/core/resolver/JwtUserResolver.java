package com.ark.component.context.core.resolver;

import com.ark.component.security.base.user.LoginUserContext;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.ark.component.context.core.contants.ContextConstants.JWT_SIGN_SECRET;

/**
 * 从JWT解析出用户信息
 */
@Slf4j
public class JwtUserResolver implements UserResolver {

    @Override
    public LoginUserContext resolve(String token) {
        DecodedJWT decodedJWT = parseToken(token);
        if (decodedJWT != null) {
            Map<String, Claim> claims = decodedJWT.getClaims();
            LoginUserContext context = new LoginUserContext();
            context.setUserId(claims.get("userId").asLong());
            context.setUserCode(claims.get("userCode").asString());
            context.setUserName(claims.get("userName").asString());
            context.setIsSuperAdmin(claims.get("isSuperAdmin").asBoolean());
            context.setAccessToken(token);
            context.setExpires(decodedJWT.getExpiresAt().getTime());
            return context;
        }
        return null;
    }

    /**
     * 验证Token
     * @return 验证通过就返回JWT结构体
     */
    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SIGN_SECRET);
            return JWT.require(algorithm).build().verify(token);
        } catch (JWTVerificationException e) {
            log.error("JWT验签失败", e);
            return null;
        }
    }

    /**
     * 验证Token
     * @return 验证通过就返回JWT结构体
     */
    public DecodedJWT parseToken(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTVerificationException e) {
            log.error("JWT验签失败", e);
            return null;
        }
    }

}
