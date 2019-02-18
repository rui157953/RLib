package com.ryan.rlib.net;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

public abstract class HttpCallback<R> implements ICallback {
    @Override
    public void success(String string) {
        Class<?> clazz = analysisClassInfo(this);
        Gson gson = new Gson();
        R result = (R) gson.fromJson(string, clazz);
        
        onSuccess(result);
    }
    
    @Override
    public void fail(Throwable throwable) {
        onFail(throwable.toString());
    }
    
    protected abstract void onSuccess(R result);
    
    protected abstract void onFail(String error);
    
    private Class<?> analysisClassInfo(Object object) {
        Type type = object.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = Objects.requireNonNull(parameterizedType).getActualTypeArguments();
        
        return (Class<?>) actualTypeArguments[0];
    }
}
