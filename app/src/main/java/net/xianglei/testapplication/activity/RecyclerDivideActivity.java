package net.xianglei.testapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.BaseAdapter;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.utils.GlideUtil;
import net.xianglei.testapplication.utils.LogUtil;
import net.xianglei.testapplication.utils.ScreenUtil;
import net.xianglei.testapplication.widget.DividerGridItemDecoration;
import net.xianglei.testapplication.widget.DividerItemDecoration;

import butterknife.BindView;
import butterknife.OnClick;

public class RecyclerDivideActivity extends SimpleActivity {

    @BindView(R.id.rv_content)
    RecyclerView rv_content;

    LinearLayoutManager mLinearLayoutManager;

    private String mImageUrl = "https://rongcloud-file.cn.ronghub.com/application_octet-stream__RC-2019-10-21_8228_1571652938959.mp4?attname=40dfc95cc9fb4ba497b445d10441782f.mp4&e=2147483647&token=CddrKW5AbOMQaDRwc3ReDNvo3-sL_SO1fSUBKV3H:YV8sUH1aAnYaVrh-7iJx0iB31N0=";

    @Override
    protected int getLayoutById() {
        return R.layout.activity_recycler_divide;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {

    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    @OnClick(R.id.btn_test)
    public void test() {
        for(int i = mLinearLayoutManager.findFirstVisibleItemPosition(); i <= mLinearLayoutManager.findLastVisibleItemPosition(); i++) {
            LogUtil.d(i);
        }
    }

    @OnClick(R.id.btn_linearLayout)
    public void initLinearLayout() {
        BaseAdapter<String> adapter = new BaseAdapter<String>(R.layout.item_question) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_nickname, item);
            }
        };
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        rv_content.setLayoutManager(mLinearLayoutManager);
        //可以理解为放弃滑动权，交给上层的ScrollView
        rv_content.setNestedScrollingEnabled(false);
        adapter.bindToRecyclerView(rv_content);
        adapter.replaceData(new String[]{"1", "2", "3","4", "5", "6","7", "8", "9","10", "11", "12","13", "14", "15", "16", "17", "18","19", "20", "21","22", "23", "24","25", "26", "27","28", "29", "30", "31", "32", "33","34", "35", "36","37", "38", "39","40", "41", "42","43", "44", "45"});
        rv_content.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, Color.parseColor("#00ff2e22"), ScreenUtil.dp2px(50)));
    }

    @OnClick(R.id.btn_gridLayout)
    public void initGridLayout() {
        BaseAdapter<String> adapter = new BaseAdapter<String>(R.layout.item_simple_option) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_option, item);
                GlideUtil.loadImage(mContext, mImageUrl, helper.getView(R.id.iv_image));
                helper.addOnClickListener(R.id.iv_image);
            }
        };
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BigImageActivity.start(mContext, view, mImageUrl);
            }
        });
        rv_content.setLayoutManager(new GridLayoutManager(mContext, 3));
        //可以理解为放弃滑动权，交给上层的ScrollView
        rv_content.setNestedScrollingEnabled(false);
        adapter.bindToRecyclerView(rv_content);
        adapter.replaceData(new String[]{"1", "2", "3","4", "5", "6","7", "8", "9","10", "11", "12","13", "14", "15", "16", "17", "18","19", "20", "21","22", "23", "24","25", "26", "27","28", "29", "30", "31", "32", "33","34", "35", "36","37", "38", "39","40", "41", "42","43", "44", "45"});
        rv_content.addItemDecoration(new DividerGridItemDecoration(this, Color.parseColor("#ff2e22"), ScreenUtil.dp2px(1)));
    }
}
