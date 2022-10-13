package com.ark.component.openapi.http;


import cn.hutool.core.util.ReflectUtil;
import com.ark.component.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public class BaseHttpRequest {

    public String toQueryString() {
        StringBuilder stringBuilder = new StringBuilder();
        Field[] declaredFields = ReflectUtil.getFields(getClass());
        try {
            for (int i = 0, declaredFieldsLength = declaredFields.length; i < declaredFieldsLength; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                Object o = declaredField.get(this);
                QueryParam queryParam = declaredField.getAnnotation(QueryParam.class);
                if (i > 0) {
                    stringBuilder.append("&");
                }
                if (Objects.nonNull(queryParam)) {
                    stringBuilder.append(queryParam.name());
                } else {
                    stringBuilder.append(declaredField.getName());
                }
                stringBuilder.append("=");
                if (o != null) {
                    stringBuilder.append(o);
                }
            }
            String str = stringBuilder.toString();
            if (StringUtils.isNotBlank(str)) {
                return str;
            }
            return "";
        } catch (IllegalAccessException e) {
            throw new BizException(e.getMessage());
        }
    }

}
