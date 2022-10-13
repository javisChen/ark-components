package com.ark.biz.log.parse.parser;


import com.ark.biz.log.parse.ParseException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * 基于Spring EL实现模板解析
 */
public class SpelTemplateParser implements TemplateParser {

    //创建表达式解析器。
    private final ExpressionParser parser = new SpelExpressionParser();

    @Override
    public String parse(String template) throws ParseException {
        return parse(template, null);
    }

    @Override
    public String parse(String template, Map<String, Object> variables) throws ParseException {
        //通过evaluationContext.setVariable可以在上下文中设定变量。
        EvaluationContext context = new StandardEvaluationContext();
        if (variables != null && !variables.isEmpty()) {
            variables.forEach(context::setVariable);
        }

        //解析表达式，如果表达式是一个模板表达式，需要为解析传入模板解析器上下文。
        try {
            Expression expression = parser.parseExpression(template, new TemplateParserContext());

            //使用Expression.getValue()获取表达式的值，这里传入了Evaluation上下文，第二个参数是类型参数，表示返回值的类型。
            return expression.getValue(context, String.class);
        } catch (EvaluationException e) {
            throw new ParseException(String.format("业务日志模板解析错误，模板：[%s]", template), e);
        }
    }
}
