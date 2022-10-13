//package com.ark.component.openapi.http;
//
//
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.http.HttpStatus;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//import org.apache.commons.collections4.MapUtils;
//import org.springframework.http.HttpMethod;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
//@Slf4j
//public abstract class AbstractHttpManager {
//
//    private final OkHttpClient okHttpClient = new OkHttpClient();
//
//    private final MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
//
//    public <REQ extends BaseHttpRequest, RESP> RESP post(String uri, Class<? extends RESP> respClass, REQ req) {
//        return internalRequest(HttpMethod.POST, uri, respClass, req);
//    }
//
//    public <RESP> RESP get(String uri, Class<? extends RESP> respClass) {
//        return get(uri, respClass, null);
//    }
//
//    public <REQ extends BaseHttpRequest, RESP> RESP get(String uri, Class<? extends RESP> respClass, REQ req) {
//        return internalRequest(HttpMethod.GET, uri, respClass, req);
//    }
//
//    protected <REQ extends BaseHttpRequest, RESP> RESP internalRequest(HttpMethod method, String uri, Class<? extends RESP> clazz, REQ request) {
//
//        String requestId = generateRequestId();
//        debugLog("[{}] [{}] START...", requestId, getServiceName());
//
//        // 上下文存储容器，贯穿整个请求的生命周期
//        Map<String, Object> context = new HashMap<>(16);
//        preHandle(context);
//
//        Response httpResponse = null;
//        try {
//            Request httpRequest = buildRequest(method, requestId, serverUrl() + uri, request, context);
//
//            httpResponse = okHttpClient.newCall(httpRequest).execute();
//
//            postHandle(context, httpResponse);
//
//            return parseResponseBody(clazz, requestId, httpResponse);
//        } catch (Exception e) {
//            log.error("[{}] [{}] ERROR: ", requestId, getServiceName(), e);
//            exceptionHandle(e);
//            throw new HttpApiException(e.getMessage());
//        } finally {
//            IoUtil.close(httpResponse);
//            debugLog("[{}] [{}] END...", requestId, getServiceName());
//        }
//    }
//
//    private <RESP> RESP parseResponseBody(Class<? extends RESP> clazz, String requestId, Response httpResponse) throws IOException {
//        ResponseBody responseBody = extractResponseBody(requestId, httpResponse);
//        String bodyString = responseBody.string();
//        debugLog("[{}] [{}] RESPONSE STATUS --> {}", requestId, getServiceName(), String.valueOf(httpResponse.code()));
//        debugLog("[{}] [{}] RESPONSE BODY --> {}", requestId, getServiceName(), bodyString);
//        return JSONObject.parseObject(bodyString, clazz);
//    }
//
//    private ResponseBody extractResponseBody(String requestId, Response httpResponse) {
//        if (httpResponse.code() != HttpStatus.HTTP_OK) {
//            log.error("[{}] [{}] RESPONSE Http Status Not Ok", requestId, getServiceName());
//            throw new HttpApiException("Http Status Not Ok");
//        }
//
//        ResponseBody responseBody = httpResponse.body();
//        if (responseBody == null) {
//            log.error("[{}] [{}] RESPONSE No Response", requestId, getServiceName());
//            throw new HttpApiException("No Response");
//        }
//        return responseBody;
//    }
//
//    private <REQ extends BaseHttpRequest> Request buildRequest(HttpMethod method, String requestId, String uri, REQ request, Map<String, Object> context) {
//
//        Request.Builder requestBuilder = new Request.Builder();
//
//        processHeaders(requestId, context, requestBuilder);
//
//        if (method.equals(HttpMethod.GET)) {
//            requestBuilder = requestBuilder.url(processQueryParams(uri, request)).get();
//        } else if (method.equals(HttpMethod.POST)) {
//            requestBuilder = requestBuilder.url(uri).post(processRequestBody(requestId, request));
//        }
//        Request httpRequest = requestBuilder.build();
//
//        debugLog("[{}] [{}] REQUEST URI --> {}", requestId, getServiceName(), httpRequest.url().toString());
//        return httpRequest;
//    }
//
//    protected <REQ extends BaseHttpRequest> RequestBody processRequestBody(String requestId, REQ request) {
//        String requestBodyString = JSONObject.toJSONString(request);
//        RequestBody requestBody = RequestBody.create(mediaType, requestBodyString);
//        debugLog("[{}] [{}] REQUEST BODY --> {}", requestId, getServiceName(), requestBodyString);
//        return requestBody;
//    }
//
//    protected <REQ extends BaseHttpRequest> String processQueryParams(String uri, REQ request) {
//        HttpUrl.Builder urlBuilder = HttpUrl.parse(uri).newBuilder();
//        if (Objects.nonNull(request)) {
//            urlBuilder.query(request.toQueryString());
//        }
//        rebuildHttpUrl(urlBuilder, request);
//        return urlBuilder.toString();
//    }
//
//    private void processHeaders(String requestId, Map<String, Object> context, Request.Builder requestBuilder) {
//        Map<String, String> headers = new HashMap<>(16);
//
//        Map<String, String> buildHeaders = buildHeaders(context);
//        if (MapUtils.isNotEmpty(buildHeaders)) {
//            headers.putAll(buildHeaders);
//        }
//        requestBuilder.headers(Headers.of(headers));
//        debugLog("[{}] [{}] REQUEST HEADERS --> {}", requestId, getServiceName(), headers.toString());
//    }
//
//    private void debugLog(String s, Object... params) {
//        if (log.isDebugEnabled()) {
//            log.debug(s, params);
//        }
//    }
//
//    /**
//     * 对接系统表示
//     * @return
//     */
//    protected abstract String getServiceName();
//
//    protected abstract String serverUrl();
//
//    /**
//     * 生成请求id
//     * 默认使用UUID
//     */
//    protected String generateRequestId() {
//        return IdUtil.fastUUID();
//    }
//
//    /**
//     * 请求前置处理，开始处理请求流程就触发，可以初始化相关数据存储到上下文中
//     * @param context 上下文容器
//     */
//    protected void preHandle(Map<String, Object> context) {
//
//    }
//
//    /**
//     * 请求后置处理，接收到请求响应后触发
//     * @param context 上下文容器
//     * @param httpResponse 响应对象
//     */
//    protected void postHandle(Map<String, Object> context, Response httpResponse) {
//
//    }
//
//    /**
//     * 请求过程出现Exception触发
//     */
//    protected void exceptionHandle(Exception e) {
//
//    }
//
//    /**
//     * 请求Header构建钩子
//     * @param context 上下文容器
//     */
//    protected Map<String, String> buildHeaders(Map<String, Object> context) {
//        return null;
//    }
//
//    /**
//     * 构建HttpUrl
//     */
//    protected <REQ extends BaseHttpRequest> void rebuildHttpUrl(HttpUrl.Builder urlBuilder, REQ request) {
//    }
//}
