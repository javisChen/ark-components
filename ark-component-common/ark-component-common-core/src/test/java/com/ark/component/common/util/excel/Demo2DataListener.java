package com.ark.component.common.util.excel;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
@Slf4j
public class Demo2DataListener implements ReadListener<Sheet2> {

    public String target ;

    private Map<String, String> map = new HashMap<>(3);

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<Sheet2> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */

    public Demo2DataListener(String target) {
        this.target = "/Users/chenjiawei/code/myself/ark/ark-components/ark-component-common/ark-component-common-core/src/test/resources/" + target;
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
        FileUtil.del(target);
        map.put("交易云", "jyy");
        map.put("营销云", "yxy");
        map.put("客服云", "kyf");
        map.put("组织架构", "auth");
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param demoDAO
     */
    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. It is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(Sheet2 data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        for (Sheet2 sheet1 : cachedDataList) {
            String content = String.format("update redex_center_data.bd_dict set applications = '%s', menu_page = '%s' where code = '%s';\n",
                    StringUtils.defaultIfBlank(getApplications(sheet1), ""),
                    StringUtils.defaultIfBlank(sheet1.getMenuPage(), ""),
                    sheet1.getCode());
            FileUtil.appendString(content, new File(target), Charset.defaultCharset());
        }
    }

    private String getApplications(Sheet2 sheet1) {
        String applications = sheet1.getApplications();
        if (StringUtils.isBlank(applications)) {
            return "";
        }
        StringJoiner stringJoiner = new StringJoiner(",");
        for (String s : StringUtils.split(applications, ",")) {
            String newElement = map.get(s);
            if (StringUtils.isNotBlank(newElement)) {
                stringJoiner.add(newElement);
            }
        }
        return stringJoiner.toString();
    }
}