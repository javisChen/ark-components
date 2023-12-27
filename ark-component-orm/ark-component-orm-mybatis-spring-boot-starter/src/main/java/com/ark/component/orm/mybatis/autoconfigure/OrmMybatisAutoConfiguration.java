package com.ark.component.orm.mybatis.autoconfigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ark.component.orm.mybatis.handlers.BaseFieldAutoFillObjectHandler;
import com.ark.component.orm.mybatis.support.DefaultUserInfo;
import com.ark.component.orm.mybatis.support.ServiceContextUserInfo;
import com.ark.component.orm.mybatis.support.UserInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @ Description   :
 * @ Author        :  Javis
 * @ CreateDate    :  2020/11/09
 * @ Version       :  1.0
 */
public class OrmMybatisAutoConfiguration {

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }


    /**
     * 使用LoginUserContext填充基础字段
     */
    @Bean
    @ConditionalOnMissingBean(UserInfo.class)
    @ConditionalOnClass(name = "com.ark.component.security.base.user.LoginUserContext")
    public UserInfo serviceContextUserInfo() {
        return new ServiceContextUserInfo();
    }

    @Bean
    @ConditionalOnMissingBean(UserInfo.class)
    public UserInfo defaultUserInfo() {
        return new DefaultUserInfo();
    }

    /**
     * 自动填充公共字段
     */
    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler(UserInfo userInfo) {
        return new BaseFieldAutoFillObjectHandler(userInfo);
    }

}
