package com.ark.biz.log.autoconfigure;

import com.ark.biz.log.core.BizLogProcessor;
import com.ark.biz.log.core.aspect.BizLogRecordAspect;
import com.ark.biz.log.parse.parser.SpelTemplateParser;
import com.ark.biz.log.parse.parser.TemplateParser;
import com.ark.biz.log.parse.support.DefaultOperatorService;
import com.ark.biz.log.parse.support.IOperatorService;
import com.ark.biz.log.persistence.BizLogRepository;
import com.ark.biz.log.persistence.DefaultBizLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ComponentScan(basePackages = "com.ark.component.biz.log.biz-log.*")
public class BizLogAutoConfiguration {

    public BizLogAutoConfiguration() {
        log.info("enable [ark-component-biz-log-spring-boot-starter]");
    }

    @ConditionalOnMissingBean
    @Bean
    public IOperatorService operatorService() {
        return new DefaultOperatorService();
    }

    @ConditionalOnMissingBean
    @Bean
    public BizLogRepository bizLogRepository() {
        return new DefaultBizLogRepository();
    }

    @ConditionalOnMissingBean
    @Bean
    public TemplateParser templateParser() {
        return new SpelTemplateParser();
    }

    @ConditionalOnMissingBean
    @Bean
    public BizLogRecordAspect bizLogRecordAspect(BizLogProcessor processor) {
        return new BizLogRecordAspect(processor);
    }

    @ConditionalOnMissingBean
    @Bean
    public BizLogProcessor bizLogProcessor(TemplateParser templateParser,
                                              BizLogRepository bizLogRepository,
                                              IOperatorService operatorService) {
        return new BizLogProcessor(true, templateParser, bizLogRepository, operatorService);
    }

}
