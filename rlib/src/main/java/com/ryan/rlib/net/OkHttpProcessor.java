package com.ryan.rlib.net;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpProcessor implements IHttpProcessor {
    
    private OkHttpClient mClient;
    
    public OkHttpProcessor() {
        this(null, null);
    }
    
    public OkHttpProcessor(Context context) {
        this(context, null);
    }
    
    public OkHttpProcessor(Context context, InputStream... certificates) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        
        if (context != null) {
            File sdcache = context.getExternalCacheDir();
            int cacheSize = 10 * 1024 * 1024;
            builder.cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        }
        
        if (certificates != null) {
            builder.sslSocketFactory(getCertificates(certificates));
        }
        
        mClient = builder.build();
    }
    
    @Override
    public <R> Observable<R> get(final String url, final Class<R> clazz) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        return call(Observable.just(request), clazz);
    }
    
    @Override
    public <R> void get(String url, final HttpCallback<R> callback) {
        Observable<R> observable = get(url, (Class<R>) analysisClassInfo(callback));
        deliveryCallback(observable, callback);
    }
    
    @Override
    public <R> Observable<R> post(final String url, final Map<String, String> params, Class<R> clazz) {
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            builder.add(key, params.get(key));
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        
        return call(Observable.just(request), clazz);
    }
    
    @Override
    public <R> void post(String url, Map<String, String> params, HttpCallback<R> callback) {
        Observable<R> observable = post(url, params, (Class<R>) analysisClassInfo(callback));
        deliveryCallback(observable, callback);
    }
    
    @Override
    public void cancelTag(Object tag) {
    
    }
    
    @Override
    public void cancelAll() {
    
    }
    
    private SSLSocketFactory getCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            //证书密码（应该是客户端证书密码）
            keyManagerFactory.init(keyStore, "123456".toCharArray());
            
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private <R> Observable<R> call(Observable<Request> observable, final Class<R> clazz) {
        return observable.map(new Function<Request, Response>() {
            @Override
            public Response apply(Request request) throws Exception {
                return mClient.newCall(request).execute();
            }
        }).flatMap(new Function<Response, ObservableSource<R>>() {
            @Override
            public ObservableSource<R> apply(Response response) throws Exception {
                ResponseBody body = response.body();
                String string = body.string();
                R result = new Gson().fromJson(string, clazz);
                return Observable.just(result);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    
    private Class<?> analysisClassInfo(Object object) {
        Type type = object.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = Objects.requireNonNull(parameterizedType).getActualTypeArguments();
        
        return (Class<?>) actualTypeArguments[0];
    }
    
    private <R> void deliveryCallback(Observable<R> observable, final HttpCallback callback) {
        observable.subscribe(new Consumer<R>() {
            @Override
            public void accept(R r) throws Exception {
                callback.onSuccess(r);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                callback.fail(throwable);
            }
        });
    }
}
