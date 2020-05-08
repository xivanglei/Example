package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.adapter.SectionDemoAdapter;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.bean.SmallPinnedHeaderBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SectionListActivity extends SimpleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_section_list;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        SectionDemoAdapter adapter = new SectionDemoAdapter(getList());
        adapter.bindToRecyclerView(mRecyclerView);

    }

    private List<SectionEntity<SmallPinnedHeaderBean>> getList() {
        List<SectionEntity<SmallPinnedHeaderBean>> list = new ArrayList<>();
        SectionEntity<SmallPinnedHeaderBean> sectionEntity1 = new SectionEntity<SmallPinnedHeaderBean>(true, "我的账户"){};
        list.add(sectionEntity1);
        SmallPinnedHeaderBean bean1 = new SmallPinnedHeaderBean();
        bean1.setData("重置");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity2 = new SectionEntity<SmallPinnedHeaderBean>(bean1){};
        list.add(sectionEntity2);
        SmallPinnedHeaderBean bean2 = new SmallPinnedHeaderBean();
        bean2.setData("钱包");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity3 = new SectionEntity<SmallPinnedHeaderBean>(bean2){};
        list.add(sectionEntity3);
        SmallPinnedHeaderBean bean3 = new SmallPinnedHeaderBean();
        bean3.setData("会员特权");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity4 = new SectionEntity<SmallPinnedHeaderBean>(bean3){};
        list.add(sectionEntity4);
        SmallPinnedHeaderBean bean4 = new SmallPinnedHeaderBean();
        bean4.setData("商城");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity5 = new SectionEntity<SmallPinnedHeaderBean>(bean4){};
        list.add(sectionEntity5);
        SmallPinnedHeaderBean bean5 = new SmallPinnedHeaderBean();
        bean5.setData("多余");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity6 = new SectionEntity<SmallPinnedHeaderBean>(bean5){};
        list.add(sectionEntity6);


        SectionEntity<SmallPinnedHeaderBean> sectionEntity7 = new SectionEntity<SmallPinnedHeaderBean>(true, "我的福利"){};
        list.add(sectionEntity7);
        SmallPinnedHeaderBean bean6 = new SmallPinnedHeaderBean();
        bean6.setData("重置");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity8 = new SectionEntity<SmallPinnedHeaderBean>(bean6){};
        list.add(sectionEntity8);
        SmallPinnedHeaderBean bean7 = new SmallPinnedHeaderBean();
        bean7.setData("钱包");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity9 = new SectionEntity<SmallPinnedHeaderBean>(bean7){};
        list.add(sectionEntity9);
        SmallPinnedHeaderBean bean8 = new SmallPinnedHeaderBean();
        bean8.setData("会员特权");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity10 = new SectionEntity<SmallPinnedHeaderBean>(bean8){};
        list.add(sectionEntity10);
        SmallPinnedHeaderBean bean9 = new SmallPinnedHeaderBean();
        bean9.setData("商城");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity11 = new SectionEntity<SmallPinnedHeaderBean>(bean9){};
        list.add(sectionEntity11);
        SmallPinnedHeaderBean bean10 = new SmallPinnedHeaderBean();
        bean10.setData("多余");
        SectionEntity<SmallPinnedHeaderBean> sectionEntity12 = new SectionEntity<SmallPinnedHeaderBean>(bean10){};
        list.add(sectionEntity12);
        return list;
    }
}
