package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TokenCache {
    public static Logger logger= LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX="token_";
    //LRU算法
    //initialcapacity缓存初始化容量
    private static LoadingCache<String,String>loadCache= CacheBuilder.newBuilder().initialCapacity(1000)
            .maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
                @Override
                //默认数据加载实现,调用get取值时，如果key的值为空，则调用该方法加载
               public String load(String s) throws Exception {
                    return "null";
                }
            });
    public static void setKey(String key,String value){
        loadCache.put(key, value);
    }
    public static String  getKey(String key){
        String value=null;
        try {
            value=loadCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        }catch (Exception e){
            logger.error("logcache get a error",e);
        }
        return null;
    }

}
