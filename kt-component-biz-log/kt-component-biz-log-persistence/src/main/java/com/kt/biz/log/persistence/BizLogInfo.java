package com.kt.biz.log.persistence

;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * 业务日志记录
 */
@Data
@Builder
public class BizLogInfo {

    private Long bizId;
    private String operator;
    private String content;
    private String type;
    private String nodeText;
    private String fileFieldName;
    private String fileType;
    private String extension;
    private String clientType;
    private Long createPersonId;
    private String createPerson;
    private Long instanceId;
    private Long tenantId;
    private LocalDateTime createTime;
    private Map<String, Object> variables;

}
