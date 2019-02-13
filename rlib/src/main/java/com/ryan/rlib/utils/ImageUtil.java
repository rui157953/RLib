package com.ryan.rlib.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

public class ImageUtil {
    
    public static void loadImage(Object object, String url, ImageView imageView) {
        
        RequestManager requestManager;
        if (object instanceof View) {
            requestManager = Glide.with((View) object);
        } else if (object instanceof Fragment) {
            requestManager = Glide.with((Fragment) object);
        } else if (object instanceof FragmentActivity) {
            requestManager = Glide.with((FragmentActivity) object);
        } else if (object instanceof Activity) {
            requestManager = Glide.with((Activity) object);
        } else if (object instanceof Context) {
            requestManager = Glide.with((Context) object);
        } else {
            throw new IllegalArgumentException("The first param is wrong type of class.");
        }
        
        requestManager
                .load(url)
                .into(imageView);
    }
    
    public static void parseImage() {
    
    }
    
}
