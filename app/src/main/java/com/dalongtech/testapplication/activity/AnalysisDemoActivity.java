package com.dalongtech.testapplication.activity;

import android.os.Bundle;

import com.dalongtech.analysis.ANSAutoPageTracker;
import com.dalongtech.analysis.AnalysisAgent;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * Author:xianglei
 * Date: 2019-12-14 17:42
 * Description:
 */
public class AnalysisDemoActivity extends SimpleActivity implements ANSAutoPageTracker {

    //这里是自定义页面采集属性
    @Override
    public Map<String, Object> registerPageProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("3393939", 98989898);
        return map;
    }

    @Override
    public String registerPageUrl() {
        return "我是主页";
    }

    @Override
    protected int getLayoutById() {
        return R.layout.activity_analysis_demo;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
    }

    @OnClick(R.id.btn_send_event)
    public void sendEvent() {
        AnalysisAgent.track(this, "pppp");
    }

    @OnClick(R.id.btn_send_profile)
    public void sendProfile() {
        Map<String, Object> map = new HashMap<>();
        map.put("profileSet1", 989898);
        map.put("profileSet2", 878787);
        AnalysisAgent.profileSet(this, map);
    }

    @OnClick(R.id.btn_set_super_properties)
    public void setSuperProperties() {
        Map<String, Object> map = new HashMap<>();
        map.put("super1", 11111);
        map.put("super2", 22222);
        AnalysisAgent.registerSuperProperties(this, map);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        AnalysisAgent.alias(this, "我登录了", null);
    }
}
