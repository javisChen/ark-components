package com.kt.component.db.autoconfigure;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.kt.component.db.handelr.FieldAutoFillObjectHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
@Configuration
public class KTDatabaseAutoConfiguration {

//
//    @Bean
//    @ConditionalOnMissingBean(MapperScannerConfigurer.class)
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
//        //可以通过环境变量获取你的mapper路径,这样mapper扫描可以通过配置文件配置了
//        scannerConfigurer.setBasePackage("noot.data.module.**.mapper");
//        return scannerConfigurer;
//    }

    /**
     * 分页插件
     */
    @Bean
    @ConditionalOnMissingBean(PaginationInnerInterceptor.class)
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }

    /**
     * 自动填充公共字段
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new FieldAutoFillObjectHandler();
    }


}
