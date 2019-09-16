package net.xianglei.testapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.adapter.BTRecyclerAdapter;
import net.xianglei.testapplication.base.SimpleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BTRecyclerActivity extends SimpleActivity {

    @BindView(R.id.rv_content)
    RecyclerView rv_content;

    private BTRecyclerAdapter mBTRecyclerAdapter;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_btrecycler;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        rv_content.setLayoutManager(new LinearLayoutManager(mContext));
        mBTRecyclerAdapter = new BTRecyclerAdapter();
        mBTRecyclerAdapter.bindToRecyclerView(rv_content);
        mBTRecyclerAdapter.setNewData(getList());
    }

    private List<List<String>> getList() {
        List<List<String>> lists = new ArrayList<>();
        for(int i = 2; i < 23; i++) {
            List<String> list = new ArrayList<>();
            for(int j = i; j > 0; j--) {
                list.add("我是第" + i + "组" + "第" + j + "项");
            }
            lists.add(list);
        }
        return lists;
    }
}
