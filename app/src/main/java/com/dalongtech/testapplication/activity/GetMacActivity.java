package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.dalongtech.analysis.utils.InternalAgent;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.SystemUtil;
import com.dalongtech.testapplication.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class GetMacActivity extends SimpleActivity {

    @BindView(R.id.tv_mac_address)
    TextView tv_mac_address;
    @Override
    protected int getLayoutById() {
        return R.layout.activity_get_mac;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        tv_mac_address.setText(InternalAgent.getMac(this));
    }

    @OnClick(R.id.btn_copy)
    public void copy() {
        if(tv_mac_address.getText() == null) return;
        SystemUtil.copyToClip(tv_mac_address.getText().toString());
        ToastUtil.show("已复制");
    }
}
