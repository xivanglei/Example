package com.dalongtech.testapplication.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.BaseSectionAdapter;
import com.dalongtech.testapplication.bean.SmallPinnedHeaderBean;

import java.util.List;

/**
 * Author:xianglei
 * Date: 2020-05-08 17:10
 * Description:
 */
public class SectionDemoAdapter extends BaseSectionAdapter<SectionEntity<SmallPinnedHeaderBean>> {

    public SectionDemoAdapter(List<SectionEntity<SmallPinnedHeaderBean>> data) {
        super(R.layout.item_section_demo, R.layout.head_section_demo, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SectionEntity<SmallPinnedHeaderBean> item) {
        helper.setText(R.id.tv_head, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionEntity<SmallPinnedHeaderBean> item) {
        helper.setText(R.id.tv_content, item.t.getData());
    }
}
