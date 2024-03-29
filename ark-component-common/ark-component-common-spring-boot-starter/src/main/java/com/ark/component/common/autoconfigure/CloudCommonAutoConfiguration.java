package com.ark.component.common.autoconfigure;

import com.ark.component.common.util.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Slf4j
public class CloudCommonAutoConfiguration {

    public CloudCommonAutoConfiguration() {
        log.info("enable [ark-component-common-spring-boot-starter]");
    }

    @Bean
    public SpringUtils springUtils() {
        return new SpringUtils();
    }
}
