package com.zzz.jobwork.utils;

import com.squareup.okhttp.OkHttpClient;

public class CacheManager {
    
    private BaseCache newsCache;  
  
      
    private static CacheManager instance;  
    private static Object lock = new Object();  
      
    public CacheManager() {  
        //这个根据配置文件来，初始BaseCache而已;  
        newsCache = new BaseCache("OkHttpClient",18000);
    }  
      
    public static CacheManager getInstance(){  
        if (instance == null){  
            synchronized( lock ){
                if (instance == null){  
                    instance = new CacheManager();  
                }  
            }  
        }  
        return instance;  
    }  
  
    public void putOkHttpClient(String key,OkHttpClient news) {
        newsCache.put(key,news);
    }  
  
    public void removeOkHttpClient(String key) {
        // TODO 自动生成方法存根  
        newsCache.remove(key);
    }  
  
    public OkHttpClient getOkHttpClient(String key) {
        try {
            return (OkHttpClient) newsCache.get(key);
        } catch (Exception e) {  

        }

        return null;
    }  
  
    public void removeAllNews() {  
        newsCache.removeAll();
    }  
  
}  
