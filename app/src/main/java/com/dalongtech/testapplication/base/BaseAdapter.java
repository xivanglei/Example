package com.dalongtech.testapplication.base;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Author:xianglei
 * Date: 2018/12/26 3:20 PM
 * Description:RecyclerViewAdapter基类
 */
public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {


    public BaseAdapter() {
        super(null);
    }

    public BaseAdapter(List<T> list) {
        super(list);
    }
    public BaseAdapter(int layoutResId) {
        super(layoutResId, new ArrayList<T>());
    }
    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public void replaceData(List<T> list) {
        if(list != null && list.size() > 0) {
            super.replaceData(list);
        }
    }

    public void replaceData(T... list) {
        List<T> data = new ArrayList<>();
        for(T t : list) {
            data.add(t);
        }
        if(data != null && data.size() > 0) {
            replaceData(data);
        }
    }

    public void remove() {
        remove(mData.size() - 1);
    }
}
