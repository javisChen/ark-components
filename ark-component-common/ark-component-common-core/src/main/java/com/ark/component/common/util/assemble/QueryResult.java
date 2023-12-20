package com.ark.component.common.util.assemble;

import com.ark.component.common.util.assemble.processor.Processor;

import java.util.List;

public interface QueryResult<IN, RS> {

    Processor<IN, List<RS>> collection();

    Processor<IN, RS> object();
}
