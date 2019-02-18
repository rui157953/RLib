package com.ryan.rlib.net;

public interface ICallback {
    void success(String string);
    void fail(Throwable throwable);
}
