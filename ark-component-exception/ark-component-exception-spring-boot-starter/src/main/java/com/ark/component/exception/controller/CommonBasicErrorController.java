package com.ark.component.exception.controller;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basic global error {@link Controller @Controller}, rendering {@link ErrorAttributes}.
 * More specific errors can be handled either using Spring MVC abstractions (e.g.
 * {@code @ExceptionHandler}) or by adding servlet
 * {@link AbstractServletWebServerFactory#setErrorPages server error pages}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @author Michael Stummvoll
 * @author Stephane Nicoll
 * @author Scott Frederick
 * @since 1.0.0
 * @see ErrorAttributes
 * @see ErrorProperties
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CommonBasicErrorController extends BasicErrorController {

	public CommonBasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
	}

	public CommonBasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, errorProperties, errorViewResolvers);
	}

	@RequestMapping
	@Override
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		HttpStatus status = getStatus(request);
		if (status == HttpStatus.NO_CONTENT) {
			return new ResponseEntity<>(status);
		}
		Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
		String error = ((String) body.get("error"));
		String path = ((String) body.get("path"));
		Map<String, Object> response = new HashMap<>(4);
		response.put("data", null);
		response.put("code", "404");
		response.put("msg", "[" + path + "] " + error);
		response.put("service", SpringUtil.getApplicationName());
		response.put("traceId", "");
		return new ResponseEntity<>(response, status);
	}


}
