package com.ark.component.oss.core.strategy;

import cn.hutool.core.collection.CollUtil;
import com.ark.component.oss.api.OssType;
import com.ark.component.oss.api.strategy.OssStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OssStrategyFactory {

    private Map<OssType, OssStrategy> strategyHolder;

    public OssStrategyFactory(List<OssStrategy> ossStrategies) {
        if (CollUtil.isNotEmpty(ossStrategies)) {
            strategyHolder = new HashMap<>(ossStrategies.size());
            for (OssStrategy ossStrategy : ossStrategies) {
                strategyHolder.put(ossStrategy.ossType(), ossStrategy);
            }
        }
    }

    private OssStrategy get(OssType ossType) {
        return strategyHolder.get(ossType);
    }

}
