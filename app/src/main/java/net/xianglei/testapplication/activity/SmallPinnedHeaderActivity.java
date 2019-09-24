package net.xianglei.testapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.adapter.SmallPinnedHeaderAdapter;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.bean.SmallPinnedHeaderBean;
import net.xianglei.testapplication.utils.LogUtil;
import net.xianglei.testapplication.widget.pinnedheader.SmallPinnedHeaderItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SmallPinnedHeaderActivity extends SimpleActivity {

    @BindView(R.id.rv_content)
    RecyclerView rv_content;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_small_pinned_header;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        SmallPinnedHeaderAdapter adapter = new SmallPinnedHeaderAdapter();
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(rv_content);
        adapter.setNewData(getData());

        rv_content.addItemDecoration(
                new SmallPinnedHeaderItemDecoration
                .Builder(R.id.fl_head, SmallPinnedHeaderAdapter.IS_HEAD)
                        .enableDivider(false)
                        .disableHeaderClick(true)
                        .setCoverListener(new SmallPinnedHeaderItemDecoration.CoverListener() {
                            @Override
                            public void isCover(boolean isCover, int position) {
                                LogUtil.d(isCover +  "     " + position);
                                View view = adapter.getViewByPosition(position, R.id.fl_head);
                                if(view != null) view.setVisibility(isCover ? View.GONE : View.VISIBLE);
                            }
                        }).create());
    }

    private List<SmallPinnedHeaderBean> getData() {
        List<SmallPinnedHeaderBean> list = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            SmallPinnedHeaderBean bean = new SmallPinnedHeaderBean();
            bean.setData(String.valueOf(i));
            if(i % 7 == 0) {
                bean.setHead(true);
            }
            list.add(bean);
        }
        return list;
    }
}
