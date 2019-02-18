package com.ryan.sample;

import android.app.Activity;
import android.os.Bundle;

import com.ryan.rlib.net.HttpCallback;
import com.ryan.rlib.utils.NetworkUtil;

public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkUtil.getNetworkState(this);
    }
}
