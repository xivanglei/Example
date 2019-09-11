//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.xianglei.testapplication.widget.rong_photoview;

import android.content.Context;
import android.os.Build.VERSION;

public final class VersionedGestureDetector {
    public VersionedGestureDetector() {
    }

    public static GestureDetector newInstance(Context context, OnGestureListener listener) {
        int sdkVersion = VERSION.SDK_INT;
        Object detector;
        if (sdkVersion < 5) {
            detector = new CupcakeGestureDetector(context);
        } else if (sdkVersion < 8) {
            detector = new EclairGestureDetector(context);
        } else {
            detector = new FroyoGestureDetector(context);
        }

        ((GestureDetector)detector).setOnGestureListener(listener);
        return (GestureDetector)detector;
    }
}
