package net.xianglei.customkeyboard.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.AttributeSet;

import net.xianglei.customkeyboard.R;
import net.xianglei.customkeyboard.constants.KeyConst;

/**
 * Author:xianglei
 * Date: 2019-12-03 10:36
 * Description:自定义软键盘View
 */
public class DLKeyBoardView extends KeyboardView {

    private Context mContext;

    public DLKeyBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DLKeyBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Keyboard KeyBoard = getKeyboard();
        for(Keyboard.Key key : KeyBoard.getKeys()) {
            drawKey(key, canvas);
        }

    }

    private void drawFunctionKeyText(Canvas canvas, Keyboard.Key key) {
        switch (key.codes[0]) {
            case Keyboard.KEYCODE_SHIFT:
            case KeyConst.KEY_LANGUAGE:
            case KeyConst.KEY_SYMBOL:
            case KeyConst.KEY_LINE_FEED:
            case Keyboard.KEYCODE_DONE:
            case KeyConst.KEY_BACK:
            case KeyConst.KEY_PREVIOUS_PAGE:
            case KeyConst.KEY_NEXT_PAGE:
                drawText(canvas, key, key.label.toString());
                break;
        }
    }

    private void drawText(Canvas canvas, Keyboard.Key key, String label) {
        if(TextUtils.isEmpty(label)) return;
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(sp2px(14));
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.getTextBounds(label, 0, label.length(), bounds);
        canvas.drawText(label, key.x + (key.width / 2), (key.y + key.height / 2) + bounds.height() / 2, paint);
    }

    private void drawKey(Keyboard.Key key, Canvas canvas) {
        drawFunctionKeyBackground(key, canvas);
        drawFunctionKeyText(canvas, key);
        switch (key.codes[0]) {
            case Keyboard.KEYCODE_DELETE:
                drawKeyBackground(R.drawable.dl_press_key_delete, canvas, key);
                break;
            case Keyboard.KEYCODE_CANCEL:
                drawKeyBackground(R.drawable.dl_key_cancel, canvas, key);
                break;
            case KeyConst.KEY_FUNCTION_WIN:
                drawKeyBackground(R.drawable.dl_key_win, canvas, key);
                break;
        }
    }

    private void drawFunctionKeyBackground(Keyboard.Key key, Canvas canvas) {
        switch (key.codes[0]) {
            case Keyboard.KEYCODE_SHIFT:
            case Keyboard.KEYCODE_DELETE:
            case KeyConst.KEY_LANGUAGE:
            case KeyConst.KEY_SYMBOL:
            case KeyConst.KEY_LINE_FEED:
            case Keyboard.KEYCODE_DONE:
            case KeyConst.KEY_BACK:
            case KeyConst.KEY_FUNCTION_WIN:
            case KeyConst.KEY_PREVIOUS_PAGE:
            case KeyConst.KEY_NEXT_PAGE:
                drawKeyBackground(R.drawable.dl_key_bg_function, canvas, key);
                drawStroke(key, canvas);
                break;
        }
    }

    private void drawStroke(Keyboard.Key key, Canvas canvas) {
        Paint paint = new Paint();
        paint.setStrokeWidth(dp2px(0.6f));
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#6f6f6f"));
        paint.setAntiAlias(true);
        RectF rectF = new RectF();
        rectF.left = key.x + dp2px(1.6f);
        rectF.right = key.x + key.width - dp2px(1.6f);
        rectF.top = key.y + dp2px(4.1f);
        rectF.bottom = key.y + key.height;
        canvas.drawRoundRect(rectF, dp2px(5), dp2px(5), paint);
    }

    private void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {
        Drawable npd = (Drawable) mContext.getResources().getDrawable(
                drawableId);
        if (key.codes[0] != 0) {
            int[] drawableState = key.getCurrentDrawableState();
            npd.setState(drawableState);
        }
        npd.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
        npd.draw(canvas);
    }

    private int dp2px(float dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public int sp2px(float spValue) {
        float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }
}
