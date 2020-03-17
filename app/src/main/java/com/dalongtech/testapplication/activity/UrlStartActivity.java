package com.dalongtech.testapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.JsonUtil;
import com.dalongtech.testapplication.utils.LogUtil;

import java.util.Set;

public class UrlStartActivity extends SimpleActivity {

    @Override
    protected int getLayoutById() {
        return R.layout.activity_url_start;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if(intent != null && intent.getData() != null) {
            log(intent.getData());
        }

    }

    //test://xianglei/mypath?key=mykey&aaa=bbb
    private void log(Uri uri) {
        //test
        LogUtil.d(uri.getScheme());
        //xianglei
        LogUtil.d(uri.getHost());
        //mypath
        LogUtil.d(uri.getPath());
        //key=mykey&aaa=bbb
        LogUtil.d(uri.getQuery());
        //size = 2  key 与 aaa
        Set<String> queryNames = uri.getQueryParameterNames();
        // ["key","aaa"]
        LogUtil.d(JsonUtil.toJson(queryNames));
        // bbb
        LogUtil.d(uri.getQueryParameter("aaa"));
    }
}
