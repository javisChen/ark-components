package com.ark.component.common.util.assemble;

import com.ark.component.common.util.assemble.processor.ICollectionProcessor;
import com.ark.component.common.util.assemble.processor.Processor;

public interface QueryResult<IN, RS> {

    ICollectionProcessor<IN, RS> collection();

    Processor<IN, RS> object();
}
