package com.dalongtech.testapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.dalongtech.analysis.network.RequestUtils;
import com.dalongtech.analysis.utils.ANSThreadPool;
import com.dalongtech.analysis.utils.InternalAgent;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

public class GetMacActivity extends SimpleActivity {

    private String url = "http://116.62.6.159:18306/v1/testMac";

    @BindView(R.id.tv_mac_address)
    TextView tv_mac_address;
    @BindView(R.id.tv_manufacturer)
    TextView tv_manufacturer;
    @BindView(R.id.tv_model)
    TextView tv_model;
    @BindView(R.id.tv_android_version)
    TextView tv_android_version;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_get_mac;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        LogUtil.d(tv_mac_address.getText().toString());
        tv_mac_address.setText(InternalAgent.getMac(this));
        tv_manufacturer.setText(Build.MANUFACTURER);
        tv_model.setText(Build.MODEL);
        tv_android_version.setText(String.valueOf(Build.VERSION.SDK_INT));
        LogUtil.d(getData());
    }

    @OnClick(R.id.btn_send)
    public void sendData() {
        ANSThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestUtils.postRequest(url, getData(), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mac", tv_mac_address.getText().toString());
            jsonObject.put("mobileBrand", tv_manufacturer.getText().toString());
            jsonObject.put("mobileModel", tv_model.getText().toString());
            jsonObject.put("androidVersion", tv_android_version.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }
}
