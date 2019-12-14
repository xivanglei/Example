package com.dalongtech.testapplication.adapter;

import com.chad.library.adapter.base.BaseViewHolder;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.BaseAdapter;
import com.dalongtech.testapplication.utils.GlideUtil;

import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-09-15 19:35
 * Description:
 */
public class MultiplyAdapter extends BaseAdapter<List<String>> {

    private int[] images = new int[] {R.id.iv_two, R.id.iv_three, R.id.iv_four, R.id.iv_five, R.id.iv_six, R.id.iv_seven, R.id.iv_eight, R.id.iv_nine, R.id.iv_ten};

    public MultiplyAdapter(List<List<String>> data) {
        super(R.layout.item_multy_bt_recycler, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, List<String> item) {
        int i = 0;
        while (i < item.size())
        for(int j = 0; j < 9 && i < item.size(); j++, i++) {
            GlideUtil.loadImage(mContext, R.mipmap.ic_launcher, helper.getView(images[j]));
        }
    }
}
