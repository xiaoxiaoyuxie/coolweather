package com.example.coolwaether.util;

/**
 * Created by yangyongqiang on 16/9/5.
 */
public interface HttpCallBackListener {
    void success(String response);
    void failed(Exception e);
}
