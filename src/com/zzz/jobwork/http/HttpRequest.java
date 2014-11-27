package com.zzz.jobwork.http;


import com.squareup.okhttp.*;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.task.OnHttpTaskListener;
import com.zzz.jobwork.utils.CacheManager;
import com.zzz.jobwork.utils.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * http 任务请求
 * Created by zl on 2014/11/25.
 */
public class HttpRequest {
    private static Logger logger= LogManager.getLogger(HttpRequest.class);

    private SimpleHttpTaskConfig config;

    private OnHttpTaskListener listener;
    public HttpRequest(SimpleHttpTaskConfig config){
        this.config=config;
    }

    public HttpRequest(SimpleHttpTaskConfig config,OnHttpTaskListener listener){
        this.config=config;
        this.listener=listener;
    }

    private  OkHttpClient creatHttpClient(){
        String key=config.format2String();
        OkHttpClient client= CacheManager.getInstance().getOkHttpClient(key);
        if(client != null){
            logger.debug("httpclient return from cache");
            return client;
        }

        logger.debug("httpclient return from new instance");
        client=new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);  //超时20s
        if(StringUtils.isEmpty(config.getProxyIp()) && config.getProxyPort() >0 ){
            Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyIp(), config.getProxyPort()));
            client.setProxy(proxy);
            logger.debug("httpclient set proxy:"+config.getProxyIp()+":"+proxy);
        }
        CacheManager.getInstance().putOkHttpClient(key,client);
        return client;
    }


    public Response execute(){
        if(listener != null){
            listener.onStart();
        }
        Response response = null;
        try {
            boolean retry = false;
            int r = 3;
            OkHttpClient client=creatHttpClient();
            do {
                logger.debug(" http request " + config.getUrl());
                response= client.newCall(getRequest()).execute();
                logger.debug(" http resopnse "+response);
                if (response.isSuccessful()) {
                    retry = false;
                    if (listener != null)
                        listener.onSuccess(response);
                } else {
                    if (r < 3) {
                        r++;
                        retry = true;
                        if (listener != null)
                            listener.onRetry(r);
                    } else {
                        retry = false;
                        if (listener != null)
                            listener.onError(response, null);
                    }
                }
            } while (retry);
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
        }finally {
            if(listener != null)
             listener.onFinsh();
        }
        return response;


    }
    
    
    private Request getRequest(){
        if("get".equalsIgnoreCase(config.getRequestMethod())){
            return creatHttpGet();
        }else if("post".equalsIgnoreCase(config.getRequestMethod())){
            return creatHttpPost();
        }else {
            //默认使用http get
            return creatHttpGet();
        }

    }

    private Request.Builder getBuilder(){
        Request.Builder builder=new Request.Builder();
        if (config.getHeaderParams() != null) {
            for (String s : config.getHeaderParams().keySet()) {
                builder.addHeader(s, config.getHeaderParams().get(s));
            }
        }

        return builder;
    }



    private Request creatHttpGet(){
        return getBuilder()
                .url(parseGetParams(config.getUrl(), config.getRequestParams()))
                .build();
    }


    private Request creatHttpPost(){

        return getBuilder()
                .url(config.getUrl())
                .post(getPostEntity(config.getRequestParams()))
                .build();
    }


    public static String parseGetParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            String value = "";
            for (String key : params.keySet()) {
                value = params.get(key);
                if (value == null)
                    value = "";
                sb.append(key + "=" + value).append("&");
            }
            if(sb.length() >= 1){
                sb.deleteCharAt(sb.length()-1);
            }
            return url + "?" + sb.toString();
        }


        return url;
    }



    /**
     * 创建POST提交实体

     */
    public static RequestBody getPostEntity(
            Map<String, String> parameter)  {
        try {
            FormEncodingBuilder formBody = new FormEncodingBuilder();
            if (parameter != null) {
                Set<Map.Entry<String, String>> entries = parameter.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    formBody.add(entry.getKey(),entry.getValue());
                }
                return formBody.build();
            }

        }catch (Exception e){
            logger.error(e);
        }

        return null;
    }
}
