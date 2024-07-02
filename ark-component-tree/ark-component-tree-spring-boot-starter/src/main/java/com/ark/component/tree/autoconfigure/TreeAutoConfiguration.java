package com.ark.component.tree.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@ComponentScan(basePackages = "com.ark.component.tree")
public class TreeAutoConfiguration {

    public TreeAutoConfiguration() {
        log.info("enable [ark-component-tree-spring-boot-starter]");
    }

}
