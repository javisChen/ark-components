package com.ark.component.context.core.resolver;

import com.ark.component.context.core.LoginUserContext;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.impl.JWTParser;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Payload;

/**
 * 从JWT解析出用户信息
 */
public class JwtUserResolver implements UserResolver {

    @Override
    public LoginUserContext resolve(String token) {
        DecodedJWT decode = JWT.decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJJQU0iLCJleHAiOjE2NzQyMDczNjUsInVzZXJpZCI6MSwianRpIjoiMDMzMzI5ZGItMDJlNi00N2FkLWE0NGUtY2NhZWRhYzU3NTZiIn0.HduFdq-e3WYU_MVzP1BcKSCj7J2WbX7jkSZyT_F1C9w");
        return null;
    }

    public static void main(String[] args) {
        DecodedJWT decode = JWT.decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJJQU0iLCJleHAiOjE2NzQyMDczNjUsInVzZXJpZCI6MSwianRpIjoiMDMzMzI5ZGItMDJlNi00N2FkLWE0NGUtY2NhZWRhYzU3NTZiIn0.HduFdq-e3WYU_MVzP1BcKSCj7J2WbX7jkSZyT_F1C9w");
        System.out.println(decode.getPayload());
        JWTParser jwtParser = new JWTParser();
        Payload x = jwtParser.parsePayload(decode.getPayload());
        System.out.println(x);
    }

}
