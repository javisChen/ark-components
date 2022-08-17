package com.kt.biz.log.parse.parser;

import com.kt.biz.log.parse.ParseException;

import java.util.Map;

public interface TemplateParser {

    String parse(String template) throws ParseException;

    String parse(String template, Map<String, Object> variables) throws ParseException;

}
