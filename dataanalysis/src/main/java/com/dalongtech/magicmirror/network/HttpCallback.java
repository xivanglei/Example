package com.dalongtech.magicmirror.network;

/**
 * Author:xianglei
 * Date: 2020-01-02 15:30
 * Description:
 */
public interface HttpCallback {
    void success(String data);
    void failed(int code, String message);
}
