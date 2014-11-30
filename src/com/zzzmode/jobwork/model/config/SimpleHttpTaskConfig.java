package com.zzzmode.jobwork.model.config;

import com.zzzmode.jobwork.http.HttpExecutor;
import com.zzzmode.jobwork.http.HttpResponse;
import com.zzzmode.jobwork.model.TaskConfig;
import com.zzzmode.jobwork.task.OnHttpTaskListener;
import com.zzzmode.jobwork.utils.JsonUtil;
import com.zzzmode.jobwork.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的http请求配置
 * Created by zl on 2014/11/25.
 */
public class SimpleHttpTaskConfig  extends TaskConfig<SimpleHttpTaskConfig> {

    static Logger logger= LogManager.getLogger(SimpleHttpTaskConfig.class);

    private String url;
    private String requestMethod;
    private Map<String,String> requestParams;
    private String proxyIp;
    private int proxyPort;
    private String userAgent;
    private Map<String,String> headerParams;

    private OnHttpTaskListener listener;

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

    public void setOnHttpTaskListener(OnHttpTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public String format2String() {
        return JsonUtil.getGson().toJson(this);
    }

    @Override
    public SimpleHttpTaskConfig getConfig() {
        return this;
    }

    @Override
    public HttpResponse call() throws Exception {
        if(StringUtils.isEmpty(url)){
            throw new NullPointerException("http request url not null !!!");
        }

        return new HttpExecutor(this,listener).execute();
    }


    public void setDefaultHeader(){
        logger.debug("now  setDefaultHeader..");
        setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        setHeader("Accept-Encoding", "gzip,deflate,sdch");
        setHeader("Cache-Control", "max-age=0");
        setHeader("DNT", "1");
        setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
    }

    public void setHeader(String key,String value){
        if(headerParams == null){
            headerParams=new HashMap<>(5);
        }
        headerParams.put(key,value);
    }

    public String getHost(){
        if(!StringUtils.isEmpty(url)){
            Pattern p = Pattern.compile("(http://|https://)?([^/]*)",Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(url);
            return m.find()?m.group(2):url;
        }
        return null;
    }

    public void setDefaultReferer(){
        String host=getHost();
        if(host != null){
            logger.debug("set default Referer is:"+host);
            setHeader("Referer", "http://" + host+"/");
        }
    }
}
