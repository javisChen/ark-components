package com.ark.component.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Slf4j
@Order(Integer.MAX_VALUE)
@Component
public class SwaggerPrinter implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            Environment env = event.getApplicationContext().getEnvironment();
            String ip = InetAddress.getLocalHost().getHostAddress();
            int port = ((ServletWebServerApplicationContext) event.getApplicationContext()).getWebServer().getPort();
            String contextPath = env.getProperty("server.servlet.context-path", "");
            String applicationName = env.getProperty("spring.application.name", "");

            log.info("""
                            ----------------------------------------------------------
                            \tApplication '{}' is running! Access URLs:
                            \t----------------------------------------------------------
                            \tLocal: \t\thttp://localhost:{}{}
                            \tExternal: \thttp://{}:{}{}
                            \tSwagger UI: \thttp://{}:{}{}/swagger-ui/index.html
                            \tOpenAPI 3: \thttp://{}:{}{}/v3/api-docs
                            \tKnife4j UI: \thttp://{}:{}{}/doc.html
                            ----------------------------------------------------------""",
                    applicationName,
                    port, contextPath,
                    ip, port, contextPath,
                    ip, port, contextPath,
                    ip, port, contextPath,
                    ip, port, contextPath);

        } catch (Exception e) {
            log.error("Failed to output Swagger information", e);
        }
    }
} 