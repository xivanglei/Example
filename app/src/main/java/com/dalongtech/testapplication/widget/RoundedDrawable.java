//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dalongtech.testapplication.widget;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView.ScaleType;
import java.util.HashSet;
import java.util.Set;

public class RoundedDrawable extends Drawable {
    public static final String TAG = "RoundedDrawable";
    public static final int DEFAULT_BORDER_COLOR = -16777216;
    private final RectF mBounds = new RectF();
    private final RectF mDrawableRect = new RectF();
    private final RectF mBitmapRect = new RectF();
    private final Bitmap mBitmap;
    private final Paint mBitmapPaint;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final RectF mBorderRect = new RectF();
    private final Paint mBorderPaint;
    private final Matrix mShaderMatrix = new Matrix();
    private final RectF mSquareCornersRect = new RectF();
    private TileMode mTileModeX;
    private TileMode mTileModeY;
    private boolean mRebuildShader;
    private float mCornerRadius;
    private final boolean[] mCornersRounded;
    private boolean mOval;
    private float mBorderWidth;
    private ColorStateList mBorderColor;
    private ScaleType mScaleType;

    public RoundedDrawable(Bitmap bitmap) {
        this.mTileModeX = TileMode.CLAMP;
        this.mTileModeY = TileMode.CLAMP;
        this.mRebuildShader = true;
        this.mCornerRadius = 0.0F;
        this.mCornersRounded = new boolean[]{true, true, true, true};
        this.mOval = false;
        this.mBorderWidth = 0.0F;
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.mScaleType = ScaleType.FIT_CENTER;
        this.mBitmap = bitmap;
        this.mBitmapWidth = bitmap.getWidth();
        this.mBitmapHeight = bitmap.getHeight();
        this.mBitmapRect.set(0.0F, 0.0F, (float)this.mBitmapWidth, (float)this.mBitmapHeight);
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setStyle(Style.FILL);
        this.mBitmapPaint.setAntiAlias(true);
        this.mBorderPaint = new Paint();
        this.mBorderPaint.setStyle(Style.STROKE);
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setColor(this.mBorderColor.getColorForState(this.getState(), -16777216));
        this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
    }

    public static RoundedDrawable fromBitmap(Bitmap bitmap) {
        return bitmap != null ? new RoundedDrawable(bitmap) : null;
    }

    public static Drawable fromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                return drawable;
            }

            if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable)drawable;
                int num = ld.getNumberOfLayers();

                for(int i = 0; i < num; ++i) {
                    Drawable d = ld.getDrawable(i);
                    ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d));
                }
                return ld;
            }

            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundedDrawable(bm);
            }
        }

        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        } else {
            int width = Math.max(drawable.getIntrinsicWidth(), 2);
            int height = Math.max(drawable.getIntrinsicHeight(), 2);

            Bitmap bitmap;
            try {
                bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            } catch (Exception var5) {
                var5.printStackTrace();
                Log.w("RoundedDrawable", "Failed to create bitmap from drawable!");
                bitmap = null;
            }

            return bitmap;
        }
    }

    public Bitmap getSourceBitmap() {
        return this.mBitmap;
    }

    public boolean isStateful() {
        return this.mBorderColor.isStateful();
    }

    protected boolean onStateChange(int[] state) {
        int newColor = this.mBorderColor.getColorForState(state, 0);
        if (this.mBorderPaint.getColor() != newColor) {
            this.mBorderPaint.setColor(newColor);
            return true;
        } else {
            return super.onStateChange(state);
        }
    }

    private void updateShaderMatrix() {
        float scale;
        float dx;
        float dy;
        switch(this.mScaleType) {
        case CENTER:
            this.mBorderRect.set(this.mBounds);
            this.mBorderRect.inset(this.mBorderWidth / 2.0F, this.mBorderWidth / 2.0F);
            this.mShaderMatrix.reset();
            this.mShaderMatrix.setTranslate((float)((int)((this.mBorderRect.width() - (float)this.mBitmapWidth) * 0.5F + 0.5F)), (float)((int)((this.mBorderRect.height() - (float)this.mBitmapHeight) * 0.5F + 0.5F)));
            break;
        case CENTER_CROP:
            this.mBorderRect.set(this.mBounds);
            this.mBorderRect.inset(this.mBorderWidth / 2.0F, this.mBorderWidth / 2.0F);
            this.mShaderMatrix.reset();
            dx = 0.0F;
            dy = 0.0F;
            if ((float)this.mBitmapWidth * this.mBorderRect.height() > this.mBorderRect.width() * (float)this.mBitmapHeight) {
                scale = this.mBorderRect.height() / (float)this.mBitmapHeight;
                dx = (this.mBorderRect.width() - (float)this.mBitmapWidth * scale) * 0.5F;
            } else {
                scale = this.mBorderRect.width() / (float)this.mBitmapWidth;
                dy = (this.mBorderRect.height() - (float)this.mBitmapHeight * scale) * 0.5F;
            }

            this.mShaderMatrix.setScale(scale, scale);
            this.mShaderMatrix.postTranslate((float)((int)(dx + 0.5F)) + this.mBorderWidth / 2.0F, (float)((int)(dy + 0.5F)) + this.mBorderWidth / 2.0F);
            break;
        case CENTER_INSIDE:
            this.mShaderMatrix.reset();
            if ((float)this.mBitmapWidth <= this.mBounds.width() && (float)this.mBitmapHeight <= this.mBounds.height()) {
                scale = 1.0F;
            } else {
                scale = Math.min(this.mBounds.width() / (float)this.mBitmapWidth, this.mBounds.height() / (float)this.mBitmapHeight);
            }

            dx = (float)((int)((this.mBounds.width() - (float)this.mBitmapWidth * scale) * 0.5F + 0.5F));
            dy = (float)((int)((this.mBounds.height() - (float)this.mBitmapHeight * scale) * 0.5F + 0.5F));
            this.mShaderMatrix.setScale(scale, scale);
            this.mShaderMatrix.postTranslate(dx, dy);
            this.mBorderRect.set(this.mBitmapRect);
            this.mShaderMatrix.mapRect(this.mBorderRect);
            this.mBorderRect.inset(this.mBorderWidth / 2.0F, this.mBorderWidth / 2.0F);
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
            break;
        case FIT_CENTER:
        default:
            this.mBorderRect.set(this.mBitmapRect);
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, ScaleToFit.CENTER);
            this.mShaderMatrix.mapRect(this.mBorderRect);
            this.mBorderRect.inset(this.mBorderWidth / 2.0F, this.mBorderWidth / 2.0F);
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
            break;
        case FIT_END:
            this.mBorderRect.set(this.mBitmapRect);
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, ScaleToFit.END);
            this.mShaderMatrix.mapRect(this.mBorderRect);
            this.mBorderRect.inset(this.mBorderWidth / 2.0F, this.mBorderWidth / 2.0F);
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
            break;
        case FIT_START:
            this.mBorderRect.set(this.mBitmapRect);
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBounds, ScaleToFit.START);
            this.mShaderMatrix.mapRect(this.mBorderRect);
            this.mBorderRect.inset(this.mBorderWidth / 2.0F, this.mBorderWidth / 2.0F);
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
            break;
        case FIT_XY:
            this.mBorderRect.set(this.mBounds);
            this.mBorderRect.inset(this.mBorderWidth / 2.0F, this.mBorderWidth / 2.0F);
            this.mShaderMatrix.reset();
            this.mShaderMatrix.setRectToRect(this.mBitmapRect, this.mBorderRect, ScaleToFit.FILL);
        }

        this.mDrawableRect.set(this.mBorderRect);
    }

    protected void onBoundsChange(@NonNull Rect bounds) {
        super.onBoundsChange(bounds);
        this.mBounds.set(bounds);
        this.updateShaderMatrix();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (this.mRebuildShader) {
            BitmapShader bitmapShader = new BitmapShader(this.mBitmap, this.mTileModeX, this.mTileModeY);
            if (this.mTileModeX == TileMode.CLAMP && this.mTileModeY == TileMode.CLAMP) {
                bitmapShader.setLocalMatrix(this.mShaderMatrix);
            }
            this.mBitmapPaint.setShader(bitmapShader);
            this.mRebuildShader = false;
        }
        if (this.mOval) {
            if (this.mBorderWidth > 0.0F) {
                canvas.drawOval(this.mDrawableRect, this.mBitmapPaint);
                canvas.drawOval(this.mBorderRect, this.mBorderPaint);
            } else {
                canvas.drawOval(this.mDrawableRect, this.mBitmapPaint);
            }
        } else if (any(this.mCornersRounded)) {
            float radius = this.mCornerRadius;
            if (this.mBorderWidth > 0.0F) {
                canvas.drawRoundRect(this.mDrawableRect, radius, radius, this.mBitmapPaint);
                canvas.drawRoundRect(this.mBorderRect, radius, radius, this.mBorderPaint);
                this.redrawBitmapForSquareCorners(canvas);
                this.redrawBorderForSquareCorners(canvas);
            } else {
                canvas.drawRoundRect(this.mDrawableRect, radius, radius, this.mBitmapPaint);
                this.redrawBitmapForSquareCorners(canvas);
            }
        } else {
            canvas.drawRect(this.mDrawableRect, this.mBitmapPaint);
            if (this.mBorderWidth > 0.0F) {
                canvas.drawRect(this.mBorderRect, this.mBorderPaint);
            }
        }
    }

    private void redrawBitmapForSquareCorners(Canvas canvas) {
        if (!all(this.mCornersRounded)) {
            if (this.mCornerRadius != 0.0F) {
                float left = this.mDrawableRect.left;
                float top = this.mDrawableRect.top;
                float right = left + this.mDrawableRect.width();
                float bottom = top + this.mDrawableRect.height();
                float radius = this.mCornerRadius;
                if (!this.mCornersRounded[0]) {
                    this.mSquareCornersRect.set(left, top, left + radius, top + radius);
                    canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
                }

                if (!this.mCornersRounded[1]) {
                    this.mSquareCornersRect.set(right - radius, top, right, radius);
                    canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
                }

                if (!this.mCornersRounded[2]) {
                    this.mSquareCornersRect.set(right - radius, bottom - radius, right, bottom);
                    canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
                }

                if (!this.mCornersRounded[3]) {
                    this.mSquareCornersRect.set(left, bottom - radius, left + radius, bottom);
                    canvas.drawRect(this.mSquareCornersRect, this.mBitmapPaint);
                }

            }
        }
    }

    private void redrawBorderForSquareCorners(Canvas canvas) {
        if (!all(this.mCornersRounded)) {
            if (this.mCornerRadius != 0.0F) {
                float left = this.mDrawableRect.left;
                float top = this.mDrawableRect.top;
                float right = left + this.mDrawableRect.width();
                float bottom = top + this.mDrawableRect.height();
                float radius = this.mCornerRadius;
                float offset = this.mBorderWidth / 2.0F;
                if (!this.mCornersRounded[0]) {
                    canvas.drawLine(left - offset, top, left + radius, top, this.mBorderPaint);
                    canvas.drawLine(left, top - offset, left, top + radius, this.mBorderPaint);
                }

                if (!this.mCornersRounded[1]) {
                    canvas.drawLine(right - radius - offset, top, right, top, this.mBorderPaint);
                    canvas.drawLine(right, top - offset, right, top + radius, this.mBorderPaint);
                }

                if (!this.mCornersRounded[2]) {
                    canvas.drawLine(right - radius - offset, bottom, right + offset, bottom, this.mBorderPaint);
                    canvas.drawLine(right, bottom - radius, right, bottom, this.mBorderPaint);
                }

                if (!this.mCornersRounded[3]) {
                    canvas.drawLine(left - offset, bottom, left + radius, bottom, this.mBorderPaint);
                    canvas.drawLine(left, bottom - radius, left, bottom, this.mBorderPaint);
                }

            }
        }
    }

    public int getOpacity() {
        return -3;
    }

    public int getAlpha() {
        return this.mBitmapPaint.getAlpha();
    }

    public void setAlpha(int alpha) {
        this.mBitmapPaint.setAlpha(alpha);
        this.invalidateSelf();
    }

    public ColorFilter getColorFilter() {
        return this.mBitmapPaint.getColorFilter();
    }

    public void setColorFilter(ColorFilter cf) {
        this.mBitmapPaint.setColorFilter(cf);
        this.invalidateSelf();
    }

    public void setDither(boolean dither) {
        this.mBitmapPaint.setDither(dither);
        this.invalidateSelf();
    }

    public void setFilterBitmap(boolean filter) {
        this.mBitmapPaint.setFilterBitmap(filter);
        this.invalidateSelf();
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public float getCornerRadius(int corner) {
        return this.mCornersRounded[corner] ? this.mCornerRadius : 0.0F;
    }

    public RoundedDrawable setCornerRadius(float radius) {
        this.setCornerRadius(radius, radius, radius, radius);
        return this;
    }

    public RoundedDrawable setCornerRadius(int corner, float radius) {
        if (radius != 0.0F && this.mCornerRadius != 0.0F && this.mCornerRadius != radius) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        } else {
            if (radius == 0.0F) {
                if (only(corner, this.mCornersRounded)) {
                    this.mCornerRadius = 0.0F;
                }

                this.mCornersRounded[corner] = false;
            } else {
                if (this.mCornerRadius == 0.0F) {
                    this.mCornerRadius = radius;
                }

                this.mCornersRounded[corner] = true;
            }

            return this;
        }
    }

    public RoundedDrawable setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        Set<Float> radiusSet = new HashSet(4);
        radiusSet.add(topLeft);
        radiusSet.add(topRight);
        radiusSet.add(bottomRight);
        radiusSet.add(bottomLeft);
        radiusSet.remove(0.0F);
        if (radiusSet.size() > 1) {
            throw new IllegalArgumentException("Multiple nonzero corner radii not yet supported.");
        } else {
            if (!radiusSet.isEmpty()) {
                float radius = (Float)radiusSet.iterator().next();
                if (Float.isInfinite(radius) || Float.isNaN(radius) || radius < 0.0F) {
                    throw new IllegalArgumentException("Invalid radius value: " + radius);
                }

                this.mCornerRadius = radius;
            } else {
                this.mCornerRadius = 0.0F;
            }

            this.mCornersRounded[0] = topLeft > 0.0F;
            this.mCornersRounded[1] = topRight > 0.0F;
            this.mCornersRounded[2] = bottomRight > 0.0F;
            this.mCornersRounded[3] = bottomLeft > 0.0F;
            return this;
        }
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public RoundedDrawable setBorderWidth(float width) {
        this.mBorderWidth = width;
        this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
        return this;
    }

    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    public RoundedDrawable setBorderColor(@ColorInt int color) {
        return this.setBorderColor(ColorStateList.valueOf(color));
    }

    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    public RoundedDrawable setBorderColor(ColorStateList colors) {
        this.mBorderColor = colors != null ? colors : ColorStateList.valueOf(0);
        this.mBorderPaint.setColor(this.mBorderColor.getColorForState(this.getState(), -16777216));
        return this;
    }

    public boolean isOval() {
        return this.mOval;
    }

    public RoundedDrawable setOval(boolean oval) {
        this.mOval = oval;
        return this;
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public RoundedDrawable setScaleType(ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ScaleType.FIT_CENTER;
        }

        if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            this.updateShaderMatrix();
        }

        return this;
    }

    public TileMode getTileModeX() {
        return this.mTileModeX;
    }

    public RoundedDrawable setTileModeX(TileMode tileModeX) {
        if (this.mTileModeX != tileModeX) {
            this.mTileModeX = tileModeX;
            this.mRebuildShader = true;
            this.invalidateSelf();
        }

        return this;
    }

    public TileMode getTileModeY() {
        return this.mTileModeY;
    }

    public RoundedDrawable setTileModeY(TileMode tileModeY) {
        if (this.mTileModeY != tileModeY) {
            this.mTileModeY = tileModeY;
            this.mRebuildShader = true;
            this.invalidateSelf();
        }

        return this;
    }

    private static boolean only(int index, boolean[] booleans) {
        int i = 0;

        for(int len = booleans.length; i < len; ++i) {
            if (booleans[i] != (i == index)) {
                return false;
            }
        }

        return true;
    }

    private static boolean any(boolean[] booleans) {
        boolean[] var1 = booleans;
        int var2 = booleans.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            boolean b = var1[var3];
            if (b) {
                return true;
            }
        }

        return false;
    }

    private static boolean all(boolean[] booleans) {
        boolean[] var1 = booleans;
        int var2 = booleans.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            boolean b = var1[var3];
            if (b) {
                return false;
            }
        }

        return true;
    }

    public Bitmap toBitmap() {
        return drawableToBitmap(this);
    }
}
