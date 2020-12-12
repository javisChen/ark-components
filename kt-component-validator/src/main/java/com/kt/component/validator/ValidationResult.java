package com.kt.component.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author JarvisChen
 * @desc
 * @date 2018-06-08
 * @time 上午12:22
 */
@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {

    private String defaultMsg;
    private List<FieldError> fieldErrors;

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {
        private String field;
        private String msg;
    }
}
