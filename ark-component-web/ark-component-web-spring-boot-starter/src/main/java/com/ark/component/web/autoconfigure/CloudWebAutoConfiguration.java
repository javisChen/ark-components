package com.ark.component.web.autoconfigure;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ark.component.web.advice.CommonResponseBodyAdvice;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Web自动装配
 */
@AutoConfigureBefore(RequestMappingHandlerAdapter.class)
@Slf4j
public class CloudWebAutoConfiguration {

    public CloudWebAutoConfiguration() {
        log.info("enable [ark-component-web-spring-boot-starter]");
    }

    @Bean
    public CommonResponseBodyAdvice commonResponseBodyAdvice() {
        return new CommonResponseBodyAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI docket() {
        Info info = new Info()
                .title("Your API Title")
                .description("Your API Description")
                .version("1.0");
        return new OpenAPI()
                .info(info)
                ;
    }
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(new ApiInfoBuilder()
//                        .title("接口文档")
//                        .description("接口文档")
//                        .contact(new Contact("victor", "", ""))
//                        .version("1.0")
//                        .build())
//                //分组名称
//                .select()
//                //这里指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage("com.ark"))
//                .paths(PathSelectors.any())
//                .build()
//                .globalOperationParameters(getGlobalOperationParameters());
//    }

//    private List<Parameter> getGlobalOperationParameters() {
//        ParameterBuilder parameterBuilder = new ParameterBuilder();
//        Parameter accessTokenParam = parameterBuilder.name("X-Authorization")
//                .description("访问令牌")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build();
//        List<Parameter> parameters = new ArrayList<>(1);
//        parameters.add(accessTokenParam);
//        return parameters;
//    }

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

        // 解决Long返回前端精度丢失的问题
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }

}