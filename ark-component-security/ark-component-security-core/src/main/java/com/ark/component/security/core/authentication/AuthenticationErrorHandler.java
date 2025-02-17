
package com.ark.component.security.core.authentication;


import com.ark.component.common.util.spring.SpringUtils;
import com.ark.component.dto.ServerResponse;
import com.ark.component.dto.SingleResponse;
import com.ark.component.security.core.exception.AuthException;
import com.ark.component.security.core.common.ResponseUtils;
import com.ark.component.security.core.exception.IllegalRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class AuthenticationErrorHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

	/**
	 * 认证失败的话Security会回调这个方法
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
			throws IOException {

		String applicationName = SpringUtils.getApplicationName();
		int httpStatusCode = HttpStatus.UNAUTHORIZED.value();
		ServerResponse responseBody = SingleResponse.error(applicationName, String.valueOf(httpStatusCode), "访问资源需要先进行身份验证");

		if (authenticationException instanceof AuthException authException) {
			httpStatusCode = authException.getHttpStatusCode();
		} else if (authenticationException instanceof IllegalRequestException) {
			httpStatusCode = HttpStatus.BAD_REQUEST.value();
		}

		responseBody.setMsg(authenticationException.getMessage());
		ResponseUtils.write(responseBody, response, httpStatusCode);

	}

	/**
	 * 认证通过但是访问资源权限不足的话Security就会回调这个方法
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		String applicationName = SpringUtils.getApplicationName();
		int forbid = org.springframework.http.HttpStatus.FORBIDDEN.value();
		ServerResponse responseBody = SingleResponse.error(applicationName, String.valueOf(forbid), "权限不足，请联系管理员授权");
		ResponseUtils.write(responseBody, response, forbid);

	}
}
