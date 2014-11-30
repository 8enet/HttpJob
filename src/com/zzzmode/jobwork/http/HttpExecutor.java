package com.zzzmode.jobwork.http;


import com.zzzmode.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzzmode.jobwork.task.OnHttpTaskListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * http 任务请求
 * Created by zl on 2014/11/25.
 */
public class HttpExecutor {
    private static Logger logger= LogManager.getLogger(HttpExecutor.class);

    private SimpleHttpTaskConfig config;

    private OnHttpTaskListener listener;
    public HttpExecutor(SimpleHttpTaskConfig config){
        this.config=config;
    }

    public HttpExecutor(SimpleHttpTaskConfig config, OnHttpTaskListener listener){
        this.config=config;
        this.listener=listener;
    }

//    public static OkHttpClient client=new OkHttpClient();
//
//    private  OkHttpClient creatHttpClient(){
//        String key=config.format2String();
//        OkHttpClient client= CacheManager.getInstance().getOkHttpClient(key);
//        if(client != null){
//            logger.debug("httpclient return from cache");
//            return client;
//        }
//
//        logger.debug("httpclient return from new instance");
//        client=new OkHttpClient();
//
//
//        client.setConnectTimeout(20, TimeUnit.SECONDS);  //超时20s
//        if(StringUtils.isEmpty(config.getProxyIp()) && config.getProxyPort() >0 ){
//            Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getProxyIp(), config.getProxyPort()));
//            client.setProxy(proxy);
//            logger.debug("httpclient set proxy:"+config.getProxyIp()+":"+proxy);
//        }
//        CacheManager.getInstance().putOkHttpClient(key,client);
//        return client;
//    }

    public HttpResponse execute(){
        if(listener != null){
            listener.onStart();
        }
        HttpResponse response =new HttpResponse();
        try {
            boolean retry = false;
            int r = 3;
            do {
                logger.debug(" http request " + config.getUrl());
                HttpRequest req=doRequest();
                logger.debug(" http resopnse "+response);
                if (req.ok()) {
                    response.statCode=200;
                    response.body=req.body();
                    retry = false;
                    req.disconnect();
                    if (listener != null)
                        listener.onSuccess(response);
                } else {
                    if (r < 3) {
                        r++;
                        retry = true;
                        if (listener != null)
                            listener.onRetry(r);
                    } else {
                        response.statCode=req.code();
                        response.body=req.body();
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

    private HttpRequest doRequest(){
        if("get".equalsIgnoreCase(config.getRequestMethod())){
            return doGet();
        }else if("post".equalsIgnoreCase(config.getRequestMethod())){
            return doPost();
        }else {
            //默认使用http get
            return doGet();
        }

    }

    private HttpRequest doGet(){

        return HttpRequest.get(config.getUrl(),config.getRequestParams(),true)
                .headers(config.getHeaderParams())
                .useProxy(config.getProxyIp(), config.getProxyPort());
    }

    private HttpRequest doPost(){
        return HttpRequest.post(config.getUrl()).form(config.getRequestParams())
                .headers(config.getHeaderParams())
                .useProxy(config.getProxyIp(), config.getProxyPort());


    }

}
