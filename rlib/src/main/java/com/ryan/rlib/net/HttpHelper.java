package com.ryan.rlib.net;

import java.util.Map;

import io.reactivex.Observable;

public class HttpHelper implements IHttpProcessor {
    private final static IHttpProcessor defaultProcessor = new OkHttpProcessor();
    private static HttpHelper mHttpHelper = new HttpHelper();
    private static IHttpProcessor sProcessor = defaultProcessor;
    
    public static void init(IHttpProcessor processor) {
        sProcessor = processor;
    }
    
    public static HttpHelper obtain() {
        return mHttpHelper;
    }
    
    private HttpHelper() {
    }
    
    @Override
    public <R> Observable<R> get(String url, Class<R> clazz) {
        return sProcessor.get(url, clazz);
    }
    
    @Override
    public <R> void get(String url, HttpCallback<R> callback) {
        sProcessor.get(url, callback);
    }
    
    @Override
    public <R> Observable<R> post(String url, Map<String, String> params, Class<R> clazz) {
        return sProcessor.post(url, params,clazz);
    }
    
    @Override
    public <R> void post(String url, Map<String, String> params, HttpCallback<R> callback) {
        sProcessor.post(url, params, callback);
    }
    
    @Override
    public void cancelTag(Object tag) {
        sProcessor.cancelTag(tag);
    }
    
    @Override
    public void cancelAll() {
        sProcessor.cancelAll();
    }
    
}
