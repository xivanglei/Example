package com.dalongtech.testapplication.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.component.ActionQueue;
import com.dalongtech.testapplication.component.DialogHelper;

import butterknife.OnClick;

/**
 * Author:xianglei
 * Date: 2019-12-19 17:03
 * Description:
 */
public class ActionQueueActivity extends SimpleActivity {

    private ActionQueue mActionQueue;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_action_queue;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        mActionQueue = new ActionQueue();
    }

    @OnClick(R.id.btn_show_dialog_one)
    public void showDialogOne() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionQueue.enqueue(new ActionQueue.ActionExe() {
                    @Override
                    public void action() {
                        DialogHelper.getInstance().showSingleDialog(mContext, "我是弹窗1", new DialogHelper.OnDefaultCallback() {
                            @Override
                            public void onRightClickListener(View v) {
                                DialogHelper.getInstance().dismiss();
                                mActionQueue.executingNext();
                            }
                        });
                    }
                });
            }
        }, 3000);
    }

    @OnClick(R.id.btn_show_dialog_two)
    public void showDialogTwo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionQueue.enqueue(new ActionQueue.ActionExe() {
                    @Override
                    public void action() {
                        DialogHelper.getInstance().showSingleDialog(mContext, "我是弹窗2", new DialogHelper.OnDefaultCallback() {
                            @Override
                            public void onRightClickListener(View v) {
                                DialogHelper.getInstance().dismiss();
                                mActionQueue.executingNext();
                            }
                        });
                    }
                });
            }
        }, 3000);
    }
}
