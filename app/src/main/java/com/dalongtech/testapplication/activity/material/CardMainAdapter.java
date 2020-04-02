package com.dalongtech.testapplication.activity.material;

import com.chad.library.adapter.base.BaseViewHolder;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.BaseAdapter;
import com.dalongtech.testapplication.utils.ToastUtil;

/**
 * Author:xianglei
 * Date: 2020-04-02 08:20
 * Description:
 */
public class CardMainAdapter extends BaseAdapter<String> {

    public CardMainAdapter() {
        super(R.layout.item_card_main);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.itemView.setOnClickListener(v -> ToastUtil.show("奔跑在孤傲的路上"));
    }
}
