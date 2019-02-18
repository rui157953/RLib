package com.ryan.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ryan.rlib.net.HttpCallback;
import com.ryan.rlib.net.HttpHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Observable<Chapters> observable = HttpHelper.obtain().get("http://wanandroid.com/wxarticle/chapters/json", Chapters.class);
        observable.subscribe(new Consumer<Chapters>() {
            @Override
            public void accept(Chapters chapters) throws Exception {
                Log.d("ryan111", chapters.toString());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("ryan", throwable.toString());
            }
        });
        
        Map<String, String> p = new HashMap<>();
        p.put("username", "ryan.chen");
        p.put("password", "123456");
        HttpHelper.obtain().post("http://www.wanandroid.com/user/login", p, new HttpCallback<UserInfo>() {
            @Override
            protected void onSuccess(UserInfo result) {
                Log.d("ryan111", result.toString());
            }
            
            @Override
            protected void onFail(String error) {
                Log.e("ryan", error);
            }
        });
    }
}
