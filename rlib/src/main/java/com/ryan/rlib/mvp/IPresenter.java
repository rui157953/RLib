package com.ryan.rlib.mvp;

public interface IPresenter<V extends IView> {

    void attachView(V view);
    
    V getView();

    boolean isViewAttached();

    void detachView();

    void recycle();
}
