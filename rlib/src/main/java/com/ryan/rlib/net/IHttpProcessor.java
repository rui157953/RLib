package com.ryan.rlib.net;

import java.util.Map;

import io.reactivex.Observable;

interface IHttpProcessor {
    <R> Observable<R> get(String url, Class<R> clazz);
    
    <R> void get(String url, HttpCallback<R> callback);
    
    <R> Observable<R> post(String url, Map<String, String> params, Class<R> clazz);
    
    <R> void post(String url, Map<String, String> params, HttpCallback<R> callback);
    
    void cancelTag(Object tag);
    
    void cancelAll();
}
