package net.xianglei.testapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.BaseAdapter;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.widget.ObservableScrollView;

import butterknife.BindView;

/**
 * 实现吸顶效果
 */
public class ScrollConflictActivity extends SimpleActivity {

    @BindView(R.id.sv_content)
    ObservableScrollView sv_content;
    @BindView(R.id.rv_recycler)
    RecyclerView rv_recycler;
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.ll_list)
    LinearLayout ll_list;
    @BindView(R.id.fl_top)
    FrameLayout fl_top;
    @BindView(R.id.fl_middle)
    FrameLayout fl_middle;
    @BindView(R.id.tv_group_and_friend)
    TextView tv_group_and_friend;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_scroll_conflict;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        BaseAdapter<String> adapter = new BaseAdapter<String>(R.layout.item_simple_option) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_option, item);
            }
        };
        rv_recycler.setLayoutManager(new LinearLayoutManager(mContext));
        //可以理解为放弃滑动权，交给上层的ScrollView
        rv_recycler.setNestedScrollingEnabled(false);
        adapter.bindToRecyclerView(rv_recycler);
        adapter.replaceData(new String[]{"1", "2", "3","4", "5", "6","7", "8", "9","10", "11", "12","13", "14", "15", "16", "17", "18","19", "20", "21","22", "23", "24","25", "26", "27","28", "29", "30", "31", "32", "33","34", "35", "36","37", "38", "39","40", "41", "42","43", "44", "45"});
        sv_content.post(() -> sv_content.scrollTo(0, 0));

    }

    @Override
    protected void initEvent() {
        sv_content.setOnScrollListener(new ObservableScrollView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(ObservableScrollView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE) {
                    int scrollY = view.getScrollY();
                    if(scrollY < tv_search.getHeight()) {
                        if(scrollY > tv_search.getHeight() / 2) {
                            view.smoothScrollTo(0, tv_search.getHeight());
                        } else {
                            view.smoothScrollTo(0, 0);
                        }
                    }
                }
            }

            @Override
            public void onScroll(ObservableScrollView view, boolean isTouchScroll, int l, int t, int oldl, int oldt) {
                if(t >= (tv_search.getHeight() + ll_list.getHeight())) {
                    if(tv_group_and_friend.getParent() != fl_top) {
                        fl_middle.removeView(tv_group_and_friend);
                        fl_top.addView(tv_group_and_friend);
                        //可以用这句话解决滑动冲突 true表示recyclerView滑动 false表示recyclerView不滑
                        rv_recycler.setNestedScrollingEnabled(true);
                    }
                } else if(t < (tv_search.getHeight() + ll_list.getHeight())) {
                    if(tv_group_and_friend.getParent() != fl_middle) {
                        fl_top.removeView(tv_group_and_friend);
                        fl_middle.addView(tv_group_and_friend);
                        rv_recycler.setNestedScrollingEnabled(false);
                    }
                }
            }
        });

    }
}
