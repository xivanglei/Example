package net.xianglei.testapplication.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import net.xianglei.testapplication.R;
import net.xianglei.testapplication.base.SimpleActivity;
import net.xianglei.testapplication.component.AudioRecordManager;
import net.xianglei.testapplication.receiver.BluetoothConnectionReceiver;
import net.xianglei.testapplication.utils.LogUtil;

import java.io.FileInputStream;
import java.io.IOException;

import butterknife.OnClick;

public class VoicePlayActivity extends SimpleActivity {

//    /** 通话 */
//    public static final int STREAM_VOICE_CALL = AudioSystem.STREAM_VOICE_CALL;
//    /** 系统声音 */
//    public static final int STREAM_SYSTEM = AudioSystem.STREAM_SYSTEM;
//    /** 铃声 */
//    public static final int STREAM_RING = AudioSystem.STREAM_RING;
//    /** 音乐 */
//    public static final int STREAM_MUSIC = AudioSystem.STREAM_MUSIC;
//    /** 闹铃声 */
//    public static final int STREAM_ALARM = AudioSystem.STREAM_ALARM;
//    /** 通知音 */
//    public static final int STREAM_NOTIFICATION = AudioSystem.STREAM_NOTIFICATION;
//    /** @hide 蓝牙电话 */
//    public static final int STREAM_BLUETOOTH_SCO = AudioSystem.STREAM_BLUETOOTH_SCO;
//    /** @hide 强制的系统声音 */
//    public static final int STREAM_SYSTEM_ENFORCED = AudioSystem.STREAM_SYSTEM_ENFORCED;
//    /** DTMF拨号音 */
//    public static final int STREAM_DTMF = AudioSystem.STREAM_DTMF;
//    /** @hide 文本识别音 */
//    public static final int STREAM_TTS = AudioSystem.STREAM_TTS;

//    isBluetoothA2dpOn()：檢查A2DPAudio音頻輸出是否通過藍牙耳機；
//
//    isSpeakerphoneOn()：檢查揚聲器是否打開；
//
//    isWiredHeadsetOn()：檢查線控耳機是否連着；注意這個方法只是用來判斷耳機是否是插入狀態，並不能用它的結果來判定當前的Audio是通過耳機輸出的，這還依賴於其他條件。
//
//    setSpeakerphoneOn(boolean on)：直接選擇外放揚聲器發聲；
//
//    setBluetoothScoOn(boolean on)：要求使用藍牙SCO耳機進行通訊；

//    MODE_NORMAL : 普通模式，既不是鈴聲模式也不是通話模式
//    MODE_RINGTONE : 鈴聲模式
//    MODE_IN_CALL : 通話模式
//    MODE_IN_COMMUNICATION : 通信模式，包括音/視頻,VoIP通話.(3.0加入的，與通話模式類似)

    String mVoiceUrl = "/storage/emulated/0/tencent/MobileQQ/pddata/app/offline/html5/3242/typeWriter/aaa.mp3";

    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private BluetoothConnectionReceiver mAudioNoisyReceiver;

    @Override
    protected int getLayoutById() {
        return R.layout.activity_voice_play;
    }

    @Override
    protected void initViewAndData(Bundle savedInstanceState) {
        mAudioNoisyReceiver = new BluetoothConnectionReceiver();
        mAudioNoisyReceiver.setChangeStatusListener(new BluetoothConnectionReceiver.ChangeStatusListener() {
            @Override
            public void onChanged(boolean isConnect) {
                if(isConnect) {
//                    changeToBluetooth();
                } else {
//                    changeToReceiver();
                }
            }
        });
//藍牙狀態廣播監聽
        IntentFilter audioFilter = new IntentFilter();
        audioFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        audioFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mAudioNoisyReceiver, audioFilter);
    }

    @OnClick(R.id.btn_play)
    public void play() {
        initData();
    }

    @OnClick(R.id.btn_stop)
    public void stop() {
        reset();
    }

    @OnClick(R.id.btn_pause)
    public void pause() {
        if(isPlaying()) mMediaPlayer.pause();
    }

    @OnClick(R.id.btn_resume)
    public void resume() {
        if(mMediaPlayer != null) mMediaPlayer.start();
        changeToBluetooth();
    }

    @OnClick(R.id.btn_receiver)
    public void receiver() {
        changeToReceiver();
    }

    @OnClick(R.id.btn_speaker)
    public void speaker() {
        changeToSpeaker();
    }

    @OnClick(R.id.btn_bluetooth)
    public void bluetooth() {
        changeToBluetooth();
    }

    @OnClick(R.id.btn_record)
    public void voiceRecord() {
        AudioRecordManager.getInstance().init();
        mVoiceUrl = AudioRecordManager.getInstance().startRecord();
    }

    @OnClick(R.id.btn_stop_record)
    public void stopRecord() {
        AudioRecordManager.getInstance().stopRecord();
    }

    private void initData() {
        //1 初始化AudioManager对象
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //2 申请焦点
//        AUDIOFOCUS_GAIN //长时间获得焦点
//        AUDIOFOCUS_GAIN_TRANSIENT //短暂性获得焦点，用完应立即释放
//        AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK //短暂性获得焦点并降音，可混音播放
//        AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE //短暂性获得焦点，录音或者语音识别
        mAudioManager.requestAudioFocus(mAudioFocusChange, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        AssetFileDescriptor fileDescriptor;
        try {
            //3 获取音频文件,我从网上下载的歌曲，放到了assets目录下
//            fileDescriptor = this.getAssets().openFd("littlelucky.mp3");
            //4 实例化MediaPlayer对象
            mMediaPlayer = new MediaPlayer();
            //5 设置播放流类型
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            FileInputStream fis = new FileInputStream(mVoiceUrl);
            //6 设置播放源，有多个参数可以选择，具体参考相关文档，本文旨在介绍音频焦点
            mMediaPlayer.setDataSource(fis.getFD());
            //7 设置循环播放
//            mMediaPlayer.setLooping(true);
            //8 准备监听
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    changeToBluetooth();
                    //9 准备完成后自动播放
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mMediaPlayer.start();
                    changeToBluetooth();
                }
            });
            //10 异步准备
            mMediaPlayer.prepareAsync();
            changeToBluetooth();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusChange = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS:
                    //长时间丢失焦点,当其他应用申请的焦点为AUDIOFOCUS_GAIN时，
                    //会触发此回调事件，例如播放QQ音乐，网易云音乐等
                    //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                    LogUtil.d("AUDIOFOCUS_LOSS");
                    reset();
                    //释放焦点，该方法可根据需要来决定是否调用
                    //若焦点释放掉之后，将不会再自动获得
                    mAudioManager.abandonAudioFocus(mAudioFocusChange);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂性丢失焦点，当其他应用申请AUDIOFOCUS_GAIN_TRANSIENT或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE时，
                    //会触发此回调事件，例如播放短视频，拨打电话等。
                    //通常需要暂停音乐播放
                    pause();
                    LogUtil.d("AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //短暂性丢失焦点并作降音处理
                    LogUtil.d("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    //当其他应用申请焦点之后又释放焦点会触发此回调
                    //可重新播放音乐
                    LogUtil.d("AUDIOFOCUS_GAIN");
                    resume();
                    break;
            }
        }
    };

    private void reset() {
        this.resetMediaPlayer();
        this.resetAudioPlayManager();
    }

    private void resetAudioPlayManager() {
        if (this.mAudioManager != null) {
            this.mAudioManager.setMode(AudioManager.MODE_NORMAL);
            mAudioManager.abandonAudioFocus(mAudioFocusChange);
            mAudioFocusChange = null;
        }

//        this._powerManager = null;
        this.mAudioManager = null;
//        this._wakeLock = null;
//        this.mUriPlaying = null;
//        this._playListener = null;
    }

    private void resetMediaPlayer() {
        if (this.mMediaPlayer != null) {
            try {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.reset();
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
            } catch (IllegalStateException var2) {
                LogUtil.e("resetMediaPlayer", var2);
            }
        }

    }

    /**
     * 切換到外放
     */
    public void changeToSpeaker(){
//注意此處，藍牙未斷開時使用MODE_IN_COMMUNICATION而不是MODE_NORMAL
//        mAudioManager.setMode(bluetoothIsConnected ? AudioManager.MODE_IN_COMMUNICATION : AudioManager.MODE_NORMAL);
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.setSpeakerphoneOn(true);
    }

    /**
     * 切換到藍牙音箱
     */
    public void changeToBluetooth(){
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.startBluetoothSco();
        mAudioManager.setBluetoothScoOn(true);
        mAudioManager.setSpeakerphoneOn(false);
    }

    /************************************************************/
//注意：以下兩個方法還未驗證
/************************************************************/
    /**
     * 切換到耳機模式
     */
    public void changeToHeadset(){
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.setSpeakerphoneOn(false);
    }
    /**
     * 切換到聽筒
     */
    public void changeToReceiver(){
        mAudioManager.stopBluetoothSco();
        mAudioManager.setBluetoothScoOn(false);
        mAudioManager.setSpeakerphoneOn(false);
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reset();
        mContext.unregisterReceiver(mAudioNoisyReceiver);
    }
}
