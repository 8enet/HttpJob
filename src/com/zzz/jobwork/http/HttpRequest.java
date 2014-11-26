package com.zzz.jobwork.http;


import com.squareup.okhttp.*;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.task.OnHttpTaskListener;
import com.zzz.jobwork.utils.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

/**
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

    private static Map<String,SoftReference<OkHttpClient>> cache=new Hashtable<String, SoftReference<OkHttpClient>>();

    private  OkHttpClient creatHttpClient(){

        SoftReference<OkHttpClient> ref=cache.get(config.format2String());
        if (ref != null && ref.get() !=null){
            logger.debug("httpclient return from cache");
            return ref.get();
        }

        logger.debug("httpclient return from new instance");
        OkHttpClient client=null;


        if(StringUtils.isEmpty(config.getProxyIp()) && config.getProxyPort() >0 ){
//            HttpHost proxy = new HttpHost(config.getProxyIp(), config.getProxyPort());
//            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
//            client=HttpClients.custom().setRoutePlanner(routePlanner).build();
            Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyIp(), config.getProxyPort()));
            logger.debug("httpclient set proxy:"+config.getProxyIp()+":"+proxy);
        }else {
            client=new OkHttpClient();
        }
        cache.remove(config.format2String());
        cache.put(config.format2String(),new SoftReference<OkHttpClient>(client));
        return client;
    }




    public Response execute(){
        if(listener != null){
            listener.onStart();
        }

        OkHttpClient client=creatHttpClient();


        Response response = null;
        try {
            boolean retry = false;
            int r = 0;
            do {

                Request request = getRequest();

                logger.debug(" http request " + config.getUrl());

//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//                        logger.debug(response);
//                    }
//                });
               response= client.newCall(request).execute();

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

            System.out.println(response);

        }catch (Exception e){
            e.printStackTrace();
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
//
//    private void addHeader(HttpUriRequest request){
//        if (config.getHeaderParams() != null) {
//            for (String s : config.getHeaderParams().keySet()) {
//                request.addHeader(s, config.getHeaderParams().get(s));
//            }
//        }
//    }
//
    private Request creatHttpPost(){

        return getBuilder()
                .url(config.getUrl())
                .post(getPostEntity(config.getRequestParams()))
                .build();
    }


    public static String parseGetParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
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
        }
        //System.out.println(url + "?" + sb.toString());
        if(sb.length() >0)
        return url + "?" + sb.toString();
        else
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

        }

        return null;
    }
}
