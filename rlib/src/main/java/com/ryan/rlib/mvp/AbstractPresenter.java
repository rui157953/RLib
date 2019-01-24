package com.ryan.rlib.mvp;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class AbstractPresenter<V extends IView> implements IPresenter<V> {
    
    private Reference<V> mViewRef;//View 接口类型的弱引用
    
    public void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }
    
    public V getView() {
        return mViewRef.get();
    }
    
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }
    
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        recycle();
    }
    
    public abstract void recycle();
}
