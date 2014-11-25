package com.zzz.jobwork.http;


import com.google.gson.annotations.Expose;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2014/11/25.
 */
public class HttpRequest {

    private SimpleHttpTaskConfig config;

    public HttpRequest(SimpleHttpTaskConfig config){
        this.config=config;
    }

    private static Map<String,SoftReference<DefaultHttpClient>> cache=new Hashtable<String, SoftReference<DefaultHttpClient>>();
    private  DefaultHttpClient creatHttpClient(){

        SoftReference<DefaultHttpClient> ref=cache.get(config.format2String());
        if (ref != null && ref.get() !=null){
            return ref.get();
        }

        DefaultHttpClient client=new DefaultHttpClient();

        if(StringUtils.isEmpty(config.getProxyIp()) && config.getProxyPort() >0 ){
            HttpHost proxy = new HttpHost(config.getProxyIp(), config.getProxyPort());
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        cache.remove(config.format2String());
        cache.put(config.format2String(),new SoftReference<DefaultHttpClient>(client));

        return client;
    }




    public String execute() throws Exception{
        HttpUriRequest request=getRequest();
        System.out.println(" http request "+config.getUrl());
        if(request != null){
            addHeader(request);
            HttpResponse response=creatHttpClient().execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                return EntityUtils.toString(response.getEntity());
            }

        }
        return null;
    }
    
    
    private HttpUriRequest getRequest(){
        if("get".equalsIgnoreCase(config.getRequestMethod())){
            return creatHttpGet();
        }else if("post".equalsIgnoreCase(config.getRequestMethod())){
            return creatHttpPost();
        }else {
            //默认使用http get
            return creatHttpGet();
        }

    }




    private HttpGet creatHttpGet(){
        HttpGet httpGet = new HttpGet(parseGetParams(config.getUrl(),config.getRequestParams()));

        return httpGet;
    }

    private void addHeader(HttpUriRequest request){
        if (config.getHeaderParams() != null) {
            for (String s : config.getHeaderParams().keySet()) {
                request.addHeader(s, config.getHeaderParams().get(s));
            }
        }
    }

    private HttpPost creatHttpPost(){
        HttpPost post=new HttpPost(config.getUrl());
        post.setEntity(getPostEntity(config.getRequestParams()));
        return post;
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
    public static UrlEncodedFormEntity getPostEntity(
            Map<String, String> parameter)  {
        try {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            // 贴吧签到需要 ie=utf-8&kw=xxx&tbs=989
            if (parameter != null) {
                for (String s : parameter.keySet()) {
                    formparams.add(new BasicNameValuePair(s, parameter.get(s)));
                }
            }
            return new UrlEncodedFormEntity(formparams,
                    "UTF-8");
        }catch (Exception e){

        }

        return null;
    }
}
