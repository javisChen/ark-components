package com.ark.component.web.autoconfigure;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import com.ark.component.web.advice.CommonResponseBodyAdvice;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Web自动装配
 */
@AutoConfigureBefore(RequestMappingHandlerAdapter.class)
@Slf4j
public class WebAutoConfiguration {

    public WebAutoConfiguration() {
        log.info("enable [ark-component-web-spring-boot-starter]");
    }

    @Bean
    public CommonResponseBodyAdvice commonResponseBodyAdvice() {
        return new CommonResponseBodyAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openApi(Environment environment) {
        String application = environment.getProperty("spring.application.name");

        Contact contact = new Contact();
        contact.setEmail("javischen9548@foxmail.com");
        contact.setName("Jc");
        contact.setUrl("https://github.com/javisChen");

        Info info = new Info()
                .title(application)
                .contact(contact)
                .termsOfService("/" + application)
                .description(application)
                .summary(application)
                .version("v1.0.0");
        return new OpenAPI()
                .info(info);
    }

    /**
     * 使用FastJSON作为应用的HTTP消息转换器
     */
    @Bean
    @ConditionalOnMissingBean(HttpMessageConverters.class)
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setCharset(Charset.defaultCharset());
        List<MediaType> supportedMediaTypes = new ArrayList<>(14);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        supportedMediaTypes.add(MediaType.APPLICATION_XML);
        supportedMediaTypes.add(MediaType.IMAGE_GIF);
        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
        supportedMediaTypes.add(MediaType.IMAGE_PNG);
        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
        supportedMediaTypes.add(MediaType.TEXT_HTML);
        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        supportedMediaTypes.add(MediaType.TEXT_XML);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);

        /*
            统一数据响应的规范
            WriteNullListAsEmpty：如果集合为空，默认写回空集合给前端，即：[]
            WriteNullStringAsEmpty：如果字符串，默认写回空串给前端，即：""
            WriteLongAsString：把Long类型转换成String类型，解决前端精度丢失的问题
            LargeObject：fastjson2限制了序列化大小为64M，超过了就会OOM，启用后即可
         */
        JSON.config(
                JSONWriter.Feature.WriteNullListAsEmpty,
                JSONWriter.Feature.WriteNullStringAsEmpty,
                JSONWriter.Feature.WriteLongAsString, // 把Long类型转换成String类型，解决前端精度丢失问题
                JSONWriter.Feature.LargeObject);
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return (registry) -> registry.config().commonTags("application", applicationName);
    }


}