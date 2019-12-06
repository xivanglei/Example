package net.xianglei.customkeyboard.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.AttributeSet;

import net.xianglei.customkeyboard.R;

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

    private void drawText(Canvas canvas, Keyboard.Key key, String label) {
        if(TextUtils.isEmpty(label)) return;
        Rect bounds = new Rect();
        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.getTextBounds(label, 0, label.length(), bounds);
        canvas.drawText(label, key.x + (key.width / 2), (key.y + key.height / 2) + bounds.height() / 2, paint);
    }

    private void drawKey(Keyboard.Key key, Canvas canvas) {
        switch (key.codes[0]) {
            case Keyboard.KEYCODE_DELETE:
                drawKeyBackground(R.drawable.dl_press_key_delete, canvas, key);
                break;
        }
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
}
