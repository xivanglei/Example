package com.dalongtech.testapplication.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dalongtech.testapplication.R;
import com.dalongtech.testapplication.base.SimpleActivity;
import com.dalongtech.testapplication.utils.AnimatorUtil;
import com.dalongtech.testapplication.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

public class BigImageActivity extends SimpleActivity {

    private String mImageUrl = "";
    private static final String POS_ARRAY = "pos_array";
    private static final String IMAGE_URL = "image_url";
    private static final String POS_POSITION = "pos_position";

    private static final String POS_LEFT = "pos_left";
    private static final String POS_TOP = "pos_top";
    private static final String POS_RIGHT = "pos_right";
    private static final String POS_BOTTOM = "pos_bottom";



    private ImageView mImageView;
    private List<Integer> mPosArr;
    private int mOriginWidth;
    private float mRadio;
    private float[] mTransition = new float[2];

    @Override
    protected void beforeContentView() {
        overridePendingTransition(0, 0);
    }

    @Override
    protected int getLayoutById() {
        return R.layout.activity_big_image;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            mImageUrl = intent.getStringExtra(IMAGE_URL);
            mPosArr = intent.getIntegerArrayListExtra(POS_ARRAY);
        }

        mImageView = findViewById(R.id.imageView2);
        GlideUtil.loadImage(mContext, mImageUrl, mImageView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                computerAnimationParams();
                AnimatorUtil.showBigImageTransformAnim( mImageView, mRadio, mTransition[0], mTransition[1], true, null);
            }
        });

    }

    /**
     * 计算动画位移和缩放
     */
    private void computerAnimationParams() {
        if (mPosArr.size() != 4) {
            return;
        }
        int[] locat = new int[2];
        mImageView.getLocationOnScreen(locat);
        int left = mImageView.getLeft() + locat[0];
        int right = left + mImageView.getWidth();
        int top = mImageView.getTop() + locat[1];
        int bottom = top + mImageView.getHeight();
        int targetHeight = bottom - top;
        int targetWidth = right - left;
        int imageHeight = mPosArr.get(3) - mPosArr.get(2);
        mOriginWidth = mPosArr.get(1) - mPosArr.get(0);
        float radioWidth =  mOriginWidth / (float)targetWidth;
        float radioHeight = imageHeight / (float) targetHeight;
        mRadio = Math.max(radioHeight, radioWidth);
        mTransition[0] = (float) targetWidth / 2 - (((float)mPosArr.get(1) + mPosArr.get(0)) / 2) + left;
        mTransition[1] = (float) targetHeight / 2 - (((float)mPosArr.get(3) + mPosArr.get(2)) / 2) + top;
    }



    @Override
    public void onBackPressed() {
        AnimatorUtil.showBigImageTransformAnim(mImageView, mRadio, mTransition[0], mTransition[1], false, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }

    public static void start(Activity context, View view, String url) {
        Intent intent = new Intent(context, BigImageActivity.class);
        intent.putIntegerArrayListExtra(POS_ARRAY, getImagePos(view));
        intent.putExtra(IMAGE_URL, url);
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

    private static ArrayList<Integer> getImagePos(View view) {
        int[] locat = new int[2];
        view.getLocationOnScreen(locat);
        ArrayList<Integer> posArr = new ArrayList<>();
        posArr.add(locat[0]);
        posArr.add(locat[0] + view.getWidth());
        posArr.add(locat[1]);
        posArr.add(locat[1] + view.getHeight());
        return posArr;
    }
}
