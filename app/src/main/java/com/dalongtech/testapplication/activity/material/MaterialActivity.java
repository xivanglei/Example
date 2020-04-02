package com.dalongtech.testapplication.activity.material;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.BaseFragmentAdapter;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MaterialActivity extends SimpleActivity {

    @BindView(R.id.dl_main_drawer)
    DrawerLayout dl_main_drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nv_main_navigation)
    NavigationView nv_main_navigation;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;


    @Override
    protected int getLayoutById() {
        return R.layout.activity_material;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        initToolbar();
        initViewPager();
    }

    @Override
    protected void initEvent() {
        nv_main_navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //下面这句是把菜单子项设为可选中，选中后就会改变背景色，对于这里的菜单上面部分由于包含在group里，所以本来就是可选的
                //加上这句就把下面的不是group里的菜单也设为可选
                item.setCheckable(true);
                String title = item.getTitle().toString();
                ToastUtil.show(title);
                //可以专门设置关闭哪个方向的drawerLayout 也可以closeDrawers();关闭全部
                dl_main_drawer.closeDrawer(Gravity.START);
                return true;
            }
        });
    }

    @OnClick(R.id.fb_test)
    public void enterNext(View view) {
        Snackbar.make(view, "点击成功", 2000).show();
    }

//    //给菜单设置点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //系统的左上角的home键
            case android.R.id.home:
                //打开DrawerLayout,START方向
                dl_main_drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab == null) return;
        //为左上角的home键设置图标，默认是左箭头图标
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        //让左上角home 键显示出来
        ab.setDisplayHomeAsUpEnabled(true);
        collapsing_toolbar.setTitle("多啦A梦");
    }

    private void initViewPager() {
        List<String> titles = new ArrayList<>();
        titles.add("精选");
        titles.add("体育");
        titles.add("巴萨");
        titles.add("购物");
//        titles.add("明星");
//        titles.add("视频");
//        titles.add("健康");
//        titles.add("励志");
//        titles.add("图文");
//        titles.add("本地");
//        titles.add("动漫");
//        titles.add("搞笑");
//        titles.add("精选");

        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            fragments.add(new ListFragment());
        }
        BaseFragmentAdapter fragmentAdapteradapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewpager.setAdapter(fragmentAdapteradapter);
        tabs.setupWithViewPager(viewpager);
    }
}
