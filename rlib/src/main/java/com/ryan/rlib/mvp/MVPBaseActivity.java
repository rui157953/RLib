package com.ryan.rlib.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class MVPBaseActivity<V extends IView, P extends IPresenter<V>> extends AppCompatActivity {
    protected P mPresenter;
    protected V mView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mView = createView();
        mPresenter.attachView(mView);
    }
    
    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
    
    protected abstract P createPresenter();
    
    protected abstract V createView();
}