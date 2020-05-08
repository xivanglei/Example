package com.dalongtech.testapplication.base;

import android.content.Context;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Author:xianglei
 * Date: 2019-09-24 08:35
 * Description:
 */
public interface AdapterItemProvider<T> {
    int getLayoutId();
    void convert(Context context, BaseViewHolder helper, T item);
}
