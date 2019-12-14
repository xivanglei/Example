package com.dalongtech.testapplication.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.BaseAdapter;
import com.dalongtech.testapplication.utils.GlideUtil;
import com.dalongtech.testapplication.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-09-15 17:40
 * Description:
 */
public class BTRecyclerAdapter extends BaseAdapter<List<String>> {

    public BTRecyclerAdapter() {
        super(R.layout.item_bt_recycler);
    }

    @Override
    protected void convert(BaseViewHolder helper, List<String> item) {
        if(item.size() > 0) {
            GlideUtil.loadImage(mContext, R.mipmap.ic_launcher, helper.getView(R.id.iv_one));
            helper.setText(R.id.tv_item, item.get(0));
            RecyclerView recyclerView = helper.getView(R.id.rv_multiply_layout);
            int i = 1;
            List<List<String>> lists = new ArrayList<>();
            while (i < item.size()) {
                List<String> list = new ArrayList<>();
                for (int j = 0; j < 9 && i < item.size(); j++, i++) {
                    list.add(item.get(i));
                    LogUtil.d("i = " + i + "  j = " + j + "  position = " + helper.getLayoutPosition());
                }
                lists.add(list);
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(new MultiplyAdapter(lists));
        }
    }
}
