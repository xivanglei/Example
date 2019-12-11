package net.xianglei.customkeyboard.popwindow;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import net.xianglei.customkeyboard.R;

/**
 * Created by xianglei on 2018/4/24.
 */

public class KeyboardPreviewPop extends PopupWindow {

    private Activity context;
    private TextView mTvPreview;
    private String mShowText;


    public KeyboardPreviewPop(final Activity context) {
        this(context, null);
        this.context = context;
        this.initPopupWindow();

    }

    public KeyboardPreviewPop(Activity context, AttributeSet attrs) {
        this(context, attrs, R.attr.popupWindowStyle);
    }
    public KeyboardPreviewPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    private void initPopupWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.pop_keyboard_preview, null);
        mTvPreview = contentView.findViewById(R.id.tv_preview);
        if(mShowText != null) mTvPreview.setText(mShowText);
        this.setContentView(contentView);
        this.setWidth(dp2px(80));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
    }

    public void setText(String string) {
        mShowText = string;
        if(mTvPreview != null) mTvPreview.setText(string);
    }

    public void showPopupWindow(View parent) {
        if (parent.getVisibility() == View.GONE) {
            this.showAtLocation(parent, 0, 0, 0);
        } else {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            this.showAtLocation(parent, 0, location[0] + parent.getWidth() / 2 - this.getWidth() / 2, location[1] - dp2px(100));
        }
    }

    private int dp2px(float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }


}
