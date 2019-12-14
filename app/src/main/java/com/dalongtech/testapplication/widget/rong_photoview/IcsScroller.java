//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dalongtech.testapplication.widget.rong_photoview;

import android.annotation.TargetApi;
import android.content.Context;

@TargetApi(14)
public class IcsScroller extends GingerScroller {
    public IcsScroller(Context context) {
        super(context);
    }

    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }
}
