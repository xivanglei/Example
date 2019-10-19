package net.xianglei.testapplication.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.BaseAdapter;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.widget.CustomLinearLayoutManager;
import net.xianglei.testapplication.widget.flexiblelayout.FlexibleLayout;
import net.xianglei.testapplication.widget.flexiblelayout.callback.OnReadyPullListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FlexibleLayoutActivity extends SimpleActivity {

    @BindView(R.id.rv_content)
    RecyclerView rv_content;
    @BindView(R.id.flexible_layout)
    FlexibleLayout flexible_layout;

    private BaseAdapter<String> mAdapter;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_flexible_layout;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        mAdapter = new BaseAdapter<String>(R.layout.item_simple_option) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_option, item);

            }
        };
        View header = LayoutInflater.from(this).inflate(R.layout.head_flexible, null);
        View headerImage = header.findViewById(R.id.iv);
        mAdapter.addHeaderView(header);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rv_content.setLayoutManager(layoutManager);
        mAdapter.bindToRecyclerView(rv_content);
        mAdapter.replaceData(new String[]{"1", "2", "3","4", "5", "6","7", "8", "9","10", "11", "12","13", "14", "15", "16", "17", "18","19", "20", "21","22", "23", "24","25", "26", "27","28", "29", "30", "31", "32", "33","34", "35", "36","37", "38", "39","40", "41", "42","43", "44", "45"});
        flexible_layout.setReadyListener(new OnReadyPullListener() {
            @Override
            public boolean isReady() {
                return layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
            }
        });
        flexible_layout.setHeader(headerImage);
        flexible_layout.setRefreshable(true);
        View refreshView = LayoutInflater.from(this).inflate(R.layout.refresh_layout, null);
        flexible_layout.setRefreshView(refreshView, () -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //刷新完成后需要调用onRefreshComplete()通知FlexibleLayout
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            flexible_layout.onRefreshComplete();
                        }
                    });
                }
            }).start();
        });
    }

    private List<String> getStrList() {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add("项目" + i);
        }
        return list;
    }
}
