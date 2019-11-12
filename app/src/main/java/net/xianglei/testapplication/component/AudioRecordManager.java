package net.xianglei.testapplication.component;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.text.format.Time;

import net.xianglei.testapplication.utils.SDCardUtil;
import net.xianglei.testapplication.utils.Util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 录制音频的控制器
 * Created by @author dongkai on 2018/12/18.
 */
public class AudioRecordManager {

    private static final String TAG = "AudioRecordManager";
    static final String EXTENSION = ".amr";
    private volatile static AudioRecordManager INSTANCE;
    private MediaRecorder mediaRecorder;
    private AudioManager mAudioManager;
    private String audioFileName;
    private String voiceFilePath;
    private RecordStatus recordStatus = RecordStatus.STOP;
    private long startTime;

    public enum RecordStatus {
        READY,
        START,
        STOP
    }

    private AudioRecordManager() {

    }

    public static AudioRecordManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AudioRecordManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AudioRecordManager();
                }
            }
        }
        return INSTANCE;
    }

    public void init() {
        mAudioManager = (AudioManager) Util.getApp().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        changeToBluetooth();
        this.audioFileName = getVoiceFileName("heychat");
        recordStatus = RecordStatus.READY;
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public String startRecord() {
        if (recordStatus == RecordStatus.READY) {
            if (mediaRecorder != null) {
                mediaRecorder.release();
                mediaRecorder = null;
            }
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.setAudioChannels(1); // MONO
            // 128, still got same file
            // size.

            voiceFilePath = SDCardUtil.getPrivateCacheDir().getAbsolutePath() + File.separator + audioFileName;
            mediaRecorder.setOutputFile(voiceFilePath);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
            recordStatus = RecordStatus.START;

            startTime = new Date().getTime();
            return voiceFilePath;
        }
        return null;
    }

    public int stopRecord() {
        if (recordStatus == RecordStatus.START) {
            if (mediaRecorder != null) {
                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    if(mAudioManager != null) {
                        mAudioManager.abandonAudioFocus(null);
                        mAudioManager = null;
                    }
                    recordStatus = RecordStatus.STOP;
                    File file = new File(voiceFilePath);
                    if (file == null || !file.exists() || !file.isFile()) {
                        return 0;
                    }
                    if (file.length() == 0) {
                        file.delete();
                        return 0;
                    }
                    int seconds = (int) (System.currentTimeMillis() - startTime) ;
                    return seconds;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public void cancelRecord() {
        if (recordStatus == RecordStatus.START) {
            String file = audioFileName;
            stopRecord();
            File file1 = new File(file);
            file1.delete();
        }
    }

    /**
     * 获得录音的音量，范围 0-32767, 归一化到0 ~ 1
     *
     * @return
     */
    public float getMaxAmplitude() {
        if (recordStatus == RecordStatus.START) {
            return mediaRecorder.getMaxAmplitude() * 1.0f / 32768;
        }
        return 0;
    }

    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

    private void changeToBluetooth(){
        mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        mAudioManager.startBluetoothSco();
        mAudioManager.setBluetoothScoOn(true);
        mAudioManager.setSpeakerphoneOn(false);
    }
}
