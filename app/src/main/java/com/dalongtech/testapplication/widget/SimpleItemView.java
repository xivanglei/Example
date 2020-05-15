package com.dalongtech.testapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dalongtech.testapplication.R;

/**
 * Created by yby on 2019/2/14.
 */

public class SimpleItemView extends LinearLayout {

    private TextView mContentText;
    private TextView mTipText;
    private ImageView mOpenImg;
    private View mLineView;
    private ImageView mIvIcon;

    private Context mContext;

    public SimpleItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
        initAttrs(attrs);
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_simple_item,this,true);
        setOrientation(LinearLayout.VERTICAL);
        mContentText = findViewById(R.id.simple_item_content_text);
        mTipText = findViewById(R.id.simple_item_tip_text);
        mOpenImg = findViewById(R.id.simple_item_open_img);
        mLineView = findViewById(R.id.simple_item_divider);
        mIvIcon = findViewById(R.id.iv_icon);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray mTypedArray = mContext.obtainStyledAttributes(attrs,R.styleable.cloud_simple_item_view);
        if (mTypedArray != null) {
            int iconRes = mTypedArray.getResourceId(R.styleable.cloud_simple_item_view_simple_icon_res, 0);
            if(iconRes != 0) {
                mIvIcon.setVisibility(VISIBLE);
                mIvIcon.setImageResource(iconRes);
            }
            String mContent = mTypedArray.getString(R.styleable.cloud_simple_item_view_simple_content_text);
            if (!TextUtils.isEmpty(mContent)) {
                mContentText.setText(mContent);
            }
            String mTip = mTypedArray.getString(R.styleable.cloud_simple_item_view_simple_tip_text);
            if (!TextUtils.isEmpty(mTip)) {
                mTipText.setText(mTip);
            }

            int mDividerVisibility = mTypedArray.getInt(R.styleable.cloud_simple_item_view_simple_divider_visibility,View.GONE);
            mLineView.setVisibility(mDividerVisibility);

            int mTipVisibility = mTypedArray.getInt(R.styleable.cloud_simple_item_view_simple_tip_text_visibility,View.GONE);
            if (View.GONE != mTipVisibility) {
                mTipText.setVisibility(mTipVisibility);
            }

            int mOpenImgVisibility = mTypedArray.getInt(R.styleable.cloud_simple_item_view_simple_open_img_visibility,View.VISIBLE);
            if (View.VISIBLE != mOpenImgVisibility) {
                mOpenImg.setVisibility(mOpenImgVisibility);
            }

            mTypedArray.recycle();
        }
    }

    public TextView getTipText() {
        return mTipText;
    }

    public void setTip(String tip) {
        if (!TextUtils.isEmpty(tip) && mTipText != null) {
            mTipText.setText(tip);
        }
    }

    public void setOpenImgVisibility(int visibility) {
        if (mOpenImg != null) {
            mOpenImg.setVisibility(visibility);
        }
    }
}
