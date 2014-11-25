package com.zzz.jobwork.model.config;

import com.zzz.jobwork.http.HttpRequest;
import com.zzz.jobwork.model.TaskConfig;
import com.zzz.jobwork.utils.JsonUtil;
import com.zzz.jobwork.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 简单的http请求配置
 * Created by zl on 2014/11/25.
 */
public class SimpleHttpTaskConfig  extends TaskConfig<SimpleHttpTaskConfig> {


    public static final String HTTP_TASK_CONFIG_PREFIX=SimpleHttpTaskConfig.class.getSimpleName();

    private String url;
    private String requestMethod;
    private Map<String,String> requestParams;
    private String proxyIp;
    private int proxyPort;
    private String userAgent;
    private Map<String,String> headerParams;


    public String getUrl() {
        return url;
    }

    public SimpleHttpTaskConfig(String url){
        this.url=url;
    }

    public SimpleHttpTaskConfig(){}

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String> requestParams) {
        this.requestParams = requestParams;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Map<String, String> getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    @Override
    public String format2String() {
        return StringUtils.preConfig(HTTP_TASK_CONFIG_PREFIX)+ JsonUtil.getGson().toJson(this);
    }

    @Override
    public SimpleHttpTaskConfig getConfig() {
        return this;
    }

    @Override
    public String call() throws Exception {
        return new HttpRequest(this).execute();
    }
}
