package net.xianglei.testapplication.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.xianglei.testapplication.utils.LogUtil;

/**
 * Author:xianglei
 * Date: 2019-11-12 10:09
 * Description:蓝牙广播
 */
public class BluetoothConnectionReceiver extends BroadcastReceiver {

    private ChangeStatusListener mChangeStatusListener;

    @Override
    public void onReceive(Context context, Intent intent){
        if (BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(intent.getAction())) { //藍牙連接狀態
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, -1);
            //連接或失聯，切換音頻輸出（到藍牙、或者強制仍然揚聲器外放）
            if (state == BluetoothAdapter.STATE_CONNECTED) {
                mChangeStatusListener.onChanged(true);
                LogUtil.d("连接");
            } else if(state == BluetoothAdapter.STATE_DISCONNECTED) {
                LogUtil.d("失联");
                mChangeStatusListener.onChanged(false);
            }
        } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){ //本地藍牙打開或關閉
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            if (state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF) {
//斷開，切換音頻輸出
                LogUtil.d("断开");
                mChangeStatusListener.onChanged(false);
            }
        }
    }

    public void setChangeStatusListener(ChangeStatusListener changeStatusListener) {
        mChangeStatusListener = changeStatusListener;
    }

    public interface ChangeStatusListener {
        void onChanged(boolean isConnect);
    }
}
