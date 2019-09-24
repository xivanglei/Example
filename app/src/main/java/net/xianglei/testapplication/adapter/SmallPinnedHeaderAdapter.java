package net.xianglei.testapplication.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.BaseAdapter;
import net.xianglei.testapplication.bean.SmallPinnedHeaderBean;

import java.util.List;

/**
 * Author:xianglei
 * Date: 2019-09-15 13:49
 * Description:
 */
public class SmallPinnedHeaderAdapter extends BaseAdapter<SmallPinnedHeaderBean> {

    public static final int IS_HEAD = 10;
    private static final int IS_DATA = 11;

    public SmallPinnedHeaderAdapter() {
        super(R.layout.item_head_small_pinned);
        setMultiTypeDelegate(new MultiTypeDelegate<SmallPinnedHeaderBean>() {
            @Override
            protected int getItemType(SmallPinnedHeaderBean smallPinnedHeaderBean) {
                if(smallPinnedHeaderBean.isHead()) {
                    return IS_HEAD;
                } else {
                    return IS_DATA;
                }
            }
        });
        getMultiTypeDelegate()
                .registerItemType(IS_HEAD, R.layout.item_head_small_pinned)
                .registerItemType(IS_DATA, R.layout.item_head_small_pinned);
    }

    @Override
    protected void convert(BaseViewHolder helper, SmallPinnedHeaderBean item) {
        helper.setText(R.id.tv_item, item.getData());
        helper.setGone(R.id.fl_head, helper.getItemViewType() == IS_HEAD);
        if(helper.getItemViewType() == IS_HEAD) {
            helper.setText(R.id.tv_head, "我是一个头头" + item.getData());
        }
    }
}
