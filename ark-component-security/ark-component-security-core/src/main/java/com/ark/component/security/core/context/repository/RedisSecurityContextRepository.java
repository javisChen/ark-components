/**
 * 从Redis加载认证用户信息
 *
 * @param accessToken 访问令牌
 * @return 认证用户信息，如果未找到则返回null
 */
private AuthUser loadAuthUserFromRedis(String accessToken) {
    List<Object> values = cacheService.hMGet("auth",
            RedisKeyUtils.createAccessTokenKey(accessToken), hashKeys);

    if (!isValidValues(values)) {
        log.warn("No security context found in Redis for token: {}", accessToken);
        return null;
    }

    return assemble(values);
} 