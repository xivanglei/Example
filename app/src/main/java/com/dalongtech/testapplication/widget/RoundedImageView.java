//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dalongtech.testapplication.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import com.dalongtech.testapplication.R;

public class RoundedImageView extends android.support.v7.widget.AppCompatImageView {
    private static final int TILE_MODE_UNDEFINED = -2;
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_MIRROR = 2;
    public static final String TAG = "RoundedImageView";
    public static final float DEFAULT_RADIUS = 0.0F;
    public static final float DEFAULT_BORDER_WIDTH = 0.0F;
    public static final TileMode DEFAULT_TILE_MODE;
    private static final ScaleType[] SCALE_TYPES;
    private final float[] mCornerRadii;
    private Drawable mBackgroundDrawable;
    private ColorStateList mBorderColor;
    private float mBorderWidth;
    private ColorFilter mColorFilter;
    private boolean mColorMod;
    private Drawable mDrawable;
    private boolean mHasColorFilter;
    private boolean mIsOval;
    private boolean mMutateBackground;
    private int mResource;
    private int mBackgroundResource;
    private ScaleType mScaleType;
    private TileMode mTileModeX;
    private TileMode mTileModeY;

    public RoundedImageView(Context context) {
        super(context);
        this.mCornerRadii = new float[]{0.0F, 0.0F, 0.0F, 0.0F};
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.mBorderWidth = 0.0F;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        this.mTileModeX = DEFAULT_TILE_MODE;
        this.mTileModeY = DEFAULT_TILE_MODE;
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCornerRadii = new float[]{0.0F, 0.0F, 0.0F, 0.0F};
        this.mBorderColor = ColorStateList.valueOf(-16777216);
        this.mBorderWidth = 0.0F;
        this.mColorFilter = null;
        this.mColorMod = false;
        this.mHasColorFilter = false;
        this.mIsOval = false;
        this.mMutateBackground = false;
        this.mTileModeX = DEFAULT_TILE_MODE;
        this.mTileModeY = DEFAULT_TILE_MODE;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, defStyle, 0);
        int index = a.getInt(R.styleable.RoundedImageView_android_scaleType, -1);
        if (index >= 0) {
            this.setScaleType(SCALE_TYPES[index]);
        } else {
            this.setScaleType(ScaleType.FIT_CENTER);
        }

        float cornerRadiusOverride = (float)a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius, -1);
        this.mCornerRadii[0] = (float)a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_top_left, -1);
        this.mCornerRadii[1] = (float)a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_top_right, -1);
        this.mCornerRadii[2] = (float)a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_bottom_right, -1);
        this.mCornerRadii[3] = (float)a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_corner_radius_bottom_left, -1);
        boolean any = false;
        int tileMode = 0;

        int tileModeX;
        for(tileModeX = this.mCornerRadii.length; tileMode < tileModeX; ++tileMode) {
            if (this.mCornerRadii[tileMode] < 0.0F) {
                this.mCornerRadii[tileMode] = 0.0F;
            } else {
                any = true;
            }
        }

        if (!any) {
            if (cornerRadiusOverride < 0.0F) {
                cornerRadiusOverride = 0.0F;
            }

            tileMode = 0;

            for(tileModeX = this.mCornerRadii.length; tileMode < tileModeX; ++tileMode) {
                this.mCornerRadii[tileMode] = cornerRadiusOverride;
            }
        }

        this.mBorderWidth = (float)a.getDimensionPixelSize(R.styleable.RoundedImageView_riv_border_width, -1);
        if (this.mBorderWidth < 0.0F) {
            this.mBorderWidth = 0.0F;
        }

        this.mBorderColor = a.getColorStateList(R.styleable.RoundedImageView_riv_border_color);
        if (this.mBorderColor == null) {
            this.mBorderColor = ColorStateList.valueOf(-16777216);
        }

        this.mMutateBackground = a.getBoolean(R.styleable.RoundedImageView_riv_mutate_background, false);
        this.mIsOval = a.getBoolean(R.styleable.RoundedImageView_riv_oval, false);
        tileMode = a.getInt(R.styleable.RoundedImageView_riv_tile_mode, -2);
        if (tileMode != -2) {
            this.setTileModeX(parseTileMode(tileMode));
            this.setTileModeY(parseTileMode(tileMode));
        }

        tileModeX = a.getInt(R.styleable.RoundedImageView_riv_tile_mode_x, -2);
        if (tileModeX != -2) {
            this.setTileModeX(parseTileMode(tileModeX));
        }

        int tileModeY = a.getInt(R.styleable.RoundedImageView_riv_tile_mode_y, -2);
        if (tileModeY != -2) {
            this.setTileModeY(parseTileMode(tileModeY));
        }

        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(true);
        if (this.mMutateBackground) {
            super.setBackgroundDrawable(this.mBackgroundDrawable);
        }

        a.recycle();
    }

    private static TileMode parseTileMode(int tileMode) {
        switch(tileMode) {
        case 0:
            return TileMode.CLAMP;
        case 1:
            return TileMode.REPEAT;
        case 2:
            return TileMode.MIRROR;
        default:
            return null;
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.invalidate();
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setScaleType(ScaleType scaleType) {
        assert scaleType != null;

        if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            switch(scaleType) {
            case CENTER:
            case CENTER_CROP:
            case CENTER_INSIDE:
            case FIT_CENTER:
            case FIT_START:
            case FIT_END:
            case FIT_XY:
                super.setScaleType(ScaleType.FIT_XY);
                break;
            default:
                super.setScaleType(scaleType);
            }

            this.updateDrawableAttrs();
            this.updateBackgroundDrawableAttrs(false);
            this.invalidate();
        }

    }

    public void setImageDrawable(Drawable drawable) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromDrawable(drawable);
        this.updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageBitmap(Bitmap bm) {
        this.mResource = 0;
        this.mDrawable = RoundedDrawable.fromBitmap(bm);
        this.updateDrawableAttrs();
        super.setImageDrawable(this.mDrawable);
    }

    public void setImageResource(@DrawableRes int resId) {
        if (this.mResource != resId) {
            this.mResource = resId;
            this.mDrawable = this.resolveResource();
            this.updateDrawableAttrs();
            super.setImageDrawable(this.mDrawable);
        }

    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.setImageDrawable(this.getDrawable());
    }

    private Drawable resolveResource() {
        Resources rsrc = this.getResources();
        if (rsrc == null) {
            return null;
        } else {
            Drawable d = null;
            if (this.mResource != 0) {
                try {
                    d = rsrc.getDrawable(this.mResource);
                } catch (Exception var4) {
                    Log.w("RoundedImageView", "Unable to find resource: " + this.mResource, var4);
                    this.mResource = 0;
                }
            }

            return RoundedDrawable.fromDrawable(d);
        }
    }

    public void setBackground(Drawable background) {
        this.setBackgroundDrawable(background);
    }

    public void setBackgroundResource(@DrawableRes int resId) {
        if (this.mBackgroundResource != resId) {
            this.mBackgroundResource = resId;
            this.mBackgroundDrawable = this.resolveBackgroundResource();
            this.setBackgroundDrawable(this.mBackgroundDrawable);
        }

    }

    public void setBackgroundColor(int color) {
        this.mBackgroundDrawable = new ColorDrawable(color);
        this.setBackgroundDrawable(this.mBackgroundDrawable);
    }

    private Drawable resolveBackgroundResource() {
        Resources rsrc = this.getResources();
        if (rsrc == null) {
            return null;
        } else {
            Drawable d = null;
            if (this.mBackgroundResource != 0) {
                try {
                    d = rsrc.getDrawable(this.mBackgroundResource);
                } catch (Exception var4) {
                    Log.w("RoundedImageView", "Unable to find resource: " + this.mBackgroundResource, var4);
                    this.mBackgroundResource = 0;
                }
            }

            return RoundedDrawable.fromDrawable(d);
        }
    }

    private void updateDrawableAttrs() {
        this.updateAttrs(this.mDrawable, this.mScaleType);
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
        if (this.mMutateBackground) {
            if (convert) {
                this.mBackgroundDrawable = RoundedDrawable.fromDrawable(this.mBackgroundDrawable);
            }

            this.updateAttrs(this.mBackgroundDrawable, ScaleType.FIT_XY);
        }

    }

    public void setColorFilter(ColorFilter cf) {
        if (this.mColorFilter != cf) {
            this.mColorFilter = cf;
            this.mHasColorFilter = true;
            this.mColorMod = true;
            this.applyColorMod();
            this.invalidate();
        }

    }

    private void applyColorMod() {
        if (this.mDrawable != null && this.mColorMod) {
            this.mDrawable = this.mDrawable.mutate();
            if (this.mHasColorFilter) {
                this.mDrawable.setColorFilter(this.mColorFilter);
            }
        }

    }

    private void updateAttrs(Drawable drawable, ScaleType scaleType) {
        if (drawable != null) {
            if (drawable instanceof RoundedDrawable) {
                ((RoundedDrawable)drawable).setScaleType(scaleType).setBorderWidth(this.mBorderWidth).setBorderColor(this.mBorderColor).setOval(this.mIsOval).setTileModeX(this.mTileModeX).setTileModeY(this.mTileModeY);
                if (this.mCornerRadii != null) {
                    ((RoundedDrawable)drawable).setCornerRadius(this.mCornerRadii[0], this.mCornerRadii[1], this.mCornerRadii[2], this.mCornerRadii[3]);
                }

                this.applyColorMod();
            } else if (drawable instanceof LayerDrawable) {
                LayerDrawable ld = (LayerDrawable)drawable;
                int i = 0;

                for(int layers = ld.getNumberOfLayers(); i < layers; ++i) {
                    this.updateAttrs(ld.getDrawable(i), scaleType);
                }
            }

        }
    }

    /** @deprecated */
    @Deprecated
    public void setBackgroundDrawable(Drawable background) {
        this.mBackgroundDrawable = background;
        this.updateBackgroundDrawableAttrs(true);
        super.setBackgroundDrawable(this.mBackgroundDrawable);
    }

    public float getCornerRadius() {
        return this.getMaxCornerRadius();
    }

    public float getMaxCornerRadius() {
        float maxRadius = 0.0F;
        float[] var2 = this.mCornerRadii;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            float r = var2[var4];
            maxRadius = Math.max(r, maxRadius);
        }

        return maxRadius;
    }

    public float getCornerRadius(int corner) {
        return this.mCornerRadii[corner];
    }

    public void setCornerRadiusDimen(@DimenRes int resId) {
        float radius = this.getResources().getDimension(resId);
        this.setCornerRadius(radius, radius, radius, radius);
    }

    public void setCornerRadiusDimen(int corner, @DimenRes int resId) {
        this.setCornerRadius(corner, (float)this.getResources().getDimensionPixelSize(resId));
    }

    public void setCornerRadius(float radius) {
        this.setCornerRadius(radius, radius, radius, radius);
    }

    public void setCornerRadius(int corner, float radius) {
        if (this.mCornerRadii[corner] != radius) {
            this.mCornerRadii[corner] = radius;
            this.updateDrawableAttrs();
            this.updateBackgroundDrawableAttrs(false);
            this.invalidate();
        }
    }

    public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        if (this.mCornerRadii[0] != topLeft || this.mCornerRadii[1] != topRight || this.mCornerRadii[2] != bottomRight || this.mCornerRadii[3] != bottomLeft) {
            this.mCornerRadii[0] = topLeft;
            this.mCornerRadii[1] = topRight;
            this.mCornerRadii[3] = bottomLeft;
            this.mCornerRadii[2] = bottomRight;
            this.updateDrawableAttrs();
            this.updateBackgroundDrawableAttrs(false);
            this.invalidate();
        }
    }

    public float getBorderWidth() {
        return this.mBorderWidth;
    }

    public void setBorderWidth(@DimenRes int resId) {
        this.setBorderWidth(this.getResources().getDimension(resId));
    }

    public void setBorderWidth(float width) {
        if (this.mBorderWidth != width) {
            this.mBorderWidth = width;
            this.updateDrawableAttrs();
            this.updateBackgroundDrawableAttrs(false);
            this.invalidate();
        }
    }

    @ColorInt
    public int getBorderColor() {
        return this.mBorderColor.getDefaultColor();
    }

    public void setBorderColor(@ColorInt int color) {
        this.setBorderColor(ColorStateList.valueOf(color));
    }

    public ColorStateList getBorderColors() {
        return this.mBorderColor;
    }

    public void setBorderColor(ColorStateList colors) {
        if (!this.mBorderColor.equals(colors)) {
            this.mBorderColor = colors != null ? colors : ColorStateList.valueOf(-16777216);
            this.updateDrawableAttrs();
            this.updateBackgroundDrawableAttrs(false);
            if (this.mBorderWidth > 0.0F) {
                this.invalidate();
            }

        }
    }

    public boolean isOval() {
        return this.mIsOval;
    }

    public void setOval(boolean oval) {
        this.mIsOval = oval;
        this.updateDrawableAttrs();
        this.updateBackgroundDrawableAttrs(false);
        this.invalidate();
    }

    public TileMode getTileModeX() {
        return this.mTileModeX;
    }

    public void setTileModeX(TileMode tileModeX) {
        if (this.mTileModeX != tileModeX) {
            this.mTileModeX = tileModeX;
            this.updateDrawableAttrs();
            this.updateBackgroundDrawableAttrs(false);
            this.invalidate();
        }
    }

    public TileMode getTileModeY() {
        return this.mTileModeY;
    }

    public void setTileModeY(TileMode tileModeY) {
        if (this.mTileModeY != tileModeY) {
            this.mTileModeY = tileModeY;
            this.updateDrawableAttrs();
            this.updateBackgroundDrawableAttrs(false);
            this.invalidate();
        }
    }

    public boolean mutatesBackground() {
        return this.mMutateBackground;
    }

    public void mutateBackground(boolean mutate) {
        if (this.mMutateBackground != mutate) {
            this.mMutateBackground = mutate;
            this.updateBackgroundDrawableAttrs(true);
            this.invalidate();
        }
    }

    static {
        DEFAULT_TILE_MODE = TileMode.CLAMP;
        SCALE_TYPES = new ScaleType[]{ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE};
    }
}
