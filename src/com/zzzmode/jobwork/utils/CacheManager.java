package com.zzzmode.jobwork.utils;

import com.squareup.okhttp.OkHttpClient;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CacheManager {

    static Logger logger= LogManager.getLogger(CacheManager.class);
    private static CacheManager instance;  

    private Cache cache;
    private net.sf.ehcache.CacheManager cacheManager;

    private CacheManager() {
        cacheManager= net.sf.ehcache.CacheManager.create();
        cache = new Cache("OkHttpClient", 5000, false, false, 5*60, 1);
        cacheManager.addCache(cache);
    }  
      
    public static CacheManager getInstance(){  
        if (instance == null){  
            synchronized( CacheManager.class ){
                if (instance == null){
                    logger.debug("init EHCache mamager ");
                    instance = new CacheManager();  
                }  
            }  
        }  
        return instance;  
    }  
  
    public void putOkHttpClient(String key,OkHttpClient client) {
        Element element = new Element(key,client);
        cache.put(element);
        logger.debug("put "+key+"  to cache!");
    }  
  
    public void removeOkHttpClient(String key) {
        cache.remove(key);
    }  
  
    public OkHttpClient getOkHttpClient(String key) {
        try {
            Element value = cache.get(key);
            if(value != null){
                logger.debug("get "+ key+"  from cache!");
                return (OkHttpClient)value.getObjectValue();
            }
        } catch (Exception e) {  
            e.printStackTrace();
            logger.error(e);
        }

        return null;
    }  
  
    public void removeAllClient() {
        cacheManager.clearAll();
    }  
  
}  
