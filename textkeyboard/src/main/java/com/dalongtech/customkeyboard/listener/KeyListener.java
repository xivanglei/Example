package com.dalongtech.customkeyboard.listener;

/**
 * Author:xianglei
 * Date: 2019-12-05 14:34
 * Description:
 */
public interface KeyListener {
    void onPress(int code);
    void onRelease(int code);
    void onHide(int hideType);
    void onKeyClickEvent(String eventCode);
}
