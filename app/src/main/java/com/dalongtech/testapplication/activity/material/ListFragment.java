package com.dalongtech.testapplication.activity.material;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleFragment;

import butterknife.BindView;

/**
 * Author:xianglei
 * Date: 2020-04-02 08:14
 * Description:
 */
public class ListFragment extends SimpleFragment {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @Override
    public int getLayoutById() {
        return R.layout.fragment_list;
    }

    @Override
    protected void initViewAndData() {
        recycler_view.setLayoutManager(new LinearLayoutManager(mContext));
        CardMainAdapter cardMainAdapter = new CardMainAdapter();
        cardMainAdapter.bindToRecyclerView(recycler_view);
        cardMainAdapter.replaceData("sss", "aaa", "ddd", "sss", "aaa", "ddd", "sss", "aaa", "ddd", "sss", "aaa", "ddd");
    }
}
