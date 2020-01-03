package com.dalongtech.analysis.network;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.dalongtech.analysis.aesencrypt.EncryptAgent;
import com.dalongtech.analysis.constants.Constants;
import com.dalongtech.analysis.constants.ExtraConst;
import com.dalongtech.analysis.constants.KeyConst;
import com.dalongtech.analysis.constants.TypeConst;
import com.dalongtech.analysis.database.TableAllInfo;
import com.dalongtech.analysis.process.AgentProcess;
import com.dalongtech.analysis.strategy.BaseSendStatus;
import com.dalongtech.analysis.strategy.PolicyManager;
import com.dalongtech.analysis.utils.CheckUtils;
import com.dalongtech.analysis.utils.CommonUtils;
import com.dalongtech.analysis.utils.LogPrompt;
import com.dalongtech.analysis.utils.LogUtil;
import com.dalongtech.analysis.utils.MD5Util;
import com.dalongtech.analysis.utils.SharedUtil;
import com.dalongtech.analysis.utils.WebSocketAESUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Copyright © 2018 EGuan Inc. All rights reserved.
 * @Description: 上传管理
 * @Version: 1.0
 * @Create: 2018/2/3 17:31
 * @Author: Wang-X-C
 */

public class UploadManager {

    private Context mContext;
    private SendHandler mHandler;
    private int uploadData = 0x01;
    private int updateTime = 0x03;
    private String spv = "";
    private static final int LENGTH_COUNT = 4;

    private UploadManager() {
        HandlerThread thread = new HandlerThread(Constants.THREAD_NAME, Thread.MIN_PRIORITY);
        thread.start();
        mHandler = new SendHandler(thread.getLooper());
    }

    public static UploadManager getInstance(Context context) {
        if (Holder.INSTANCE.mContext == null && context != null) {
            Holder.INSTANCE.mContext = context;
        }
        return Holder.INSTANCE;
    }

    /**
     * flush接口调用
     * SP_POLICY_NO 0=智能 1实时发送 2间隔发送
     */
    public void flushSendManager() {
        if (CommonUtils.isMainProcess(mContext)) {
            long servicePolicyNo = SharedUtil.getLong(mContext, Constants.SP_POLICY_NO, -1L);
            if (servicePolicyNo == -1 || servicePolicyNo == 1) {
                sendUploadMessage();
            }
        } else {
            LogPrompt.processFailed();
        }
    }

    /**
     * 判断 发送数据
     */
    public void sendManager(String type, JSONObject sendData) {
        if (CommonUtils.isEmpty(sendData)) {
            return;
        }
        dbCacheCheck();
        TableAllInfo.getInstance(mContext).insert(sendData.toString(), type);
        LogUtil.d(TableAllInfo.getInstance(mContext).selectCount());
        if (CommonUtils.isMainProcess(mContext)) {
            BaseSendStatus sendStatus = PolicyManager.getPolicyType(mContext);
            if (sendStatus.isSend(mContext)) {
                sendUploadMessage();
                LogUtil.d("开始发送");
            }
        } else {
            LogPrompt.processFailed();
        }
    }

    private void dbCacheCheck() {
        long maxCount = AgentProcess.getInstance(mContext).getMaxCacheSize();
        long count = TableAllInfo.getInstance(mContext).selectCount();
        LogUtil.d(count + "--最大缓存--" + maxCount);
        if (maxCount <= count) {
            TableAllInfo.getInstance(mContext).delete(Constants.DELETE_COUNT);
        }
    }


    /**
     * 发送实时消息
     */
    private void sendUploadMessage() {
        if (mHandler.hasMessages(uploadData)) {
            mHandler.removeMessages(uploadData);
        }
        Message msg = Message.obtain();
        msg.what = uploadData;
        mHandler.sendMessage(msg);

    }

    /**
     * 发送delay消息
     */
    public void sendUploadDelayedMessage(long time) {
        if (mHandler.hasMessages(uploadData)) {
            mHandler.removeMessages(uploadData);
        }
        Message msg = Message.obtain();
        msg.what = uploadData;
        mHandler.sendMessageDelayed(msg, time);
    }

    /**
     * 发送获取网络时间消息
     */
    public void sendGetTimeMessage() {
        if (!mHandler.hasMessages(updateTime)) {
            Message msg = Message.obtain();
            msg.what = updateTime;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 处理数据压缩,上传和返回值解析
     */
    private class SendHandler extends Handler {

        private SendHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                String url = CommonUtils.getUrl(mContext) + ExtraConst.URL_EVENT;
                if (!CommonUtils.isEmpty(url)) {
                    int what = msg.what;
                    if (what == uploadData) {
                        uploadData(url);
                    } else if (what == updateTime) {
                        calibrationTime(url);
                    }
                } else {
                    LogPrompt.showErrLog(LogPrompt.URL_ERR);
                }
            } catch (Throwable throwable) {
            }
        }
    }

    /**
     * 数据上传
     */
    private void uploadData(String url) throws IOException, JSONException {
        if (CommonUtils.isNetworkAvailable(mContext)) {
            JSONArray eventArray = TableAllInfo.getInstance(mContext).select();
//            Log.d("祥雷", "uploadData: " + String.valueOf(eventArray));
            // 上传数据检查校验
            eventArray = checkUploadData(eventArray);
            if (!CommonUtils.isEmpty(eventArray)) {
                LogPrompt.showSendMessage(url, eventArray);
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("data", eventArray);
                encryptDataAndRequest(url, String.valueOf(eventArray));
            } else {
                TableAllInfo.getInstance(mContext).deleteData();
            }
        } else {
            LogPrompt.networkErr();
        }
    }

    /**
     * 检查上传数据，过滤异常数据
     */
    private JSONArray checkUploadData(JSONArray eventArray) throws JSONException {
        JSONArray newEventArray = null;
        if (eventArray != null) {
            newEventArray = new JSONArray();
            JSONObject eventInfo, xContext;
            for (int i = 0; i < eventArray.length(); i++) {
                eventInfo = eventArray.optJSONObject(i);
                // 过滤异常数据
                eventInfo = CheckUtils.checkField(eventInfo);
                if (eventInfo != null) {
                    long timeStamp = eventInfo.optLong(Constants.TIME_STAMP);
                    eventInfo.put(Constants.TIME_STAMP, calibrationTime(timeStamp));
                    xContext = eventInfo.optJSONObject(Constants.X_CONTEXT);
                    if (xContext != null && xContext.has(Constants.TIME_CALIBRATED)) {
                        xContext.put(Constants.TIME_CALIBRATED, Constants.isCalibration);
                    }
                }
                newEventArray.put(eventInfo);
            }
        }
        return newEventArray;
    }

    /**
     * 校准timeStamp时间
     */
    private long calibrationTime(long time) {
        if (Constants.isTimeCheck) {
            time += Constants.diffTime;
        }
        return time;
    }

    /**
     * 获取时间差值
     */
    private void calibrationTime(String url) {
        // 获取网络时间
        long serverTime = RequestUtils.getRequest(url);
        if (serverTime != 0) {
            // 计算网络时间与本地时间差值
            long currentTime = System.currentTimeMillis();
            long diff = serverTime - currentTime;
            long absDiff = Math.abs(diff);
            if (absDiff > Constants.ignoreDiffTime) {
                Constants.diffTime = diff;
                // 将差值存储文件，解决跨进程问题
                CommonUtils.setIdFile(mContext,
                        Constants.SP_DIFF_TIME, Long.toString(diff));
                Constants.isCalibration = true;
                LogPrompt.showCheckTimeLog(serverTime, currentTime, absDiff);
            }
        }
    }

    /**
     * 数据加密
     */
    private void encryptDataAndRequest(String url, String value) throws IOException {
        Map<String, String> headInfo = getHeadInfo(value);
        String encryptData = "data=" + URLEncoder.encode(encryptData(value), "UTF-8");
        sendRequest(url, encryptData, headInfo);
    }

    private String encryptData(String value) {
        String encryptData = WebSocketAESUtil.encryptAES(value);
        ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH_COUNT + encryptData.length());
        byteBuffer.putInt(encryptData.length());
        byteBuffer.put(encryptData.getBytes());
        return new String(byteBuffer.array());
    }



    /**
     * 发送数据
     */
    public void directSendRequest(final String url, final String value, final HttpCallback callback) throws IOException {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                LogUtil.d(value);
                Map<String, String> headInfo = getHeadInfo(value);
                try {
                    String encryptData = "data=" + URLEncoder.encode(encryptData(value), "UTF-8");
                    String returnInfo;
                    if (url.startsWith(Constants.HTTP)) {
                        returnInfo = RequestUtils.postRequest(url, encryptData, headInfo);
                    } else {
                        returnInfo = RequestUtils.postRequestHttps(mContext, url, encryptData, headInfo);
                    }
                    LogUtil.d(returnInfo);
                    JSONObject json = analysisStrategy(returnInfo);
                    if(callback == null) return;
                    if(json == null) {
                        callback.failed(-1, "收到的响应数据解析异常");
                        return;
                    }
                    if(json.optInt(Constants.SERVICE_CODE) == 10000) {
                        callback.success(json.optString(Constants.SERVICE_DATA));
                    } else {
                        callback.failed(json.optInt(Constants.SERVICE_CODE), json.optString(Constants.SERVICE_MSG));
                    }
                } catch (Throwable e) { }
            }
        });

    }

    /**
     * 发送数据
     */
    private void sendRequest(String url, String dataInfo, Map<String, String> headInfo) {
        LogUtil.d(dataInfo);
        try {
            String returnInfo;
            if (url.startsWith(Constants.HTTP)) {
                returnInfo = RequestUtils.postRequest(url, dataInfo, headInfo);
            } else {
                returnInfo = RequestUtils.postRequestHttps(mContext, url, dataInfo, headInfo);
            }
            policyAnalysis(analysisStrategy(returnInfo));
        } catch (Throwable e) { }
    }

    /**
     * 数据加密压缩编码或只压缩编码
     */
    private String encrypt(String data, int type) {
        return EncryptAgent.dataEncrypt(CommonUtils.getAppKey(mContext), Constants.DEV_SDK_VERSION, data, type);
    }

    /**
     * 获取数据加密后上传头信息
     */
    private Map<String, String> getHeadInfo(String dataInfo) {
        Map<String, String> headInfo = new HashMap<>();
        headInfo.put(KeyConst.HEAD_DATA_EN_WAY, TypeConst.ENCRYPT_NONE);
        headInfo.put(KeyConst.HEAD_DATA_LENGTH, String.valueOf(dataInfo.length()));
        headInfo.put(KeyConst.HEAD_DATA_MD5, MD5Util.md5ToStr(dataInfo));
        return headInfo;
    }

    /**
     * 返回值解密转json
     */
    private JSONObject analysisStrategy(String policy) {
        try {
            if (CommonUtils.isEmpty(policy)) {
                return null;
            }
//            String unzip = CommonUtils.messageUnzip(policy);
            byte[] bytes = policy.getBytes();
            String data = WebSocketAESUtil.decryptAES(new String(Arrays.copyOfRange(bytes, LENGTH_COUNT, bytes.length)));
            LogPrompt.showReturnCode(data);
            return new JSONObject(data);
        } catch (Throwable e) {
            try {
                return new JSONObject(policy);
            } catch (Throwable e1) {
                return null;
            }
        }
    }

    /**
     * 解析返回策略
     */
    private void policyAnalysis(JSONObject json) {
        try {
            if (!CommonUtils.isEmpty(json)) {
                int code = json.optInt(Constants.SERVICE_CODE, -1);
                if (code == 10000) {
                    resetReUploadParams();
                    TableAllInfo.getInstance(mContext).deleteData();
                    SharedUtil.setLong(mContext, Constants.SP_SEND_TIME,
                            System.currentTimeMillis());
                    LogPrompt.showSendResults(true);
                }
            } else {
                reUpload();
            }
        } catch (Throwable throwable) {
            try {
                reUpload();
            } catch (Throwable t) {
            }
        }
    }

    /**
     * 重传逻辑
     * 发送失败的次数是否大于设置的发送失败次数
     * 发送失败时间大于0 且 当前时间减上次上传失败的时间大于时间间隔立即发送,
     * 否则delay发送,delay时间范围内随机
     * 发送失败次数大于默认次数,清空重传次数,set失败时间点,delay发送时间为设置重传间隔时间
     */
    private synchronized void reUpload() {
        int failureCount = SharedUtil.getInt(mContext, Constants.SP_FAILURE_COUNT, 0);
        long intervalTime = getReUploadIntervalTime();
        if (failureCount < getReUploadCount()) {
            SharedUtil.setInt(mContext, Constants.SP_FAILURE_COUNT, failureCount + 1);
            long failureTime = SharedUtil.getLong(mContext, Constants.SP_FAILURE_TIME, -1L);
            // 获取上传失败时间,如果上传失败时间为0，发送delay任务
            if (failureTime == 0) {
                sendUploadDelayedMessage(intervalTime + getRandomNumb());
            } else {
                // 如果当前时间 减 失败时间 大于 间隔时间 则立即上传
                long difference = Math.abs(System.currentTimeMillis() - failureTime);
                if (difference > intervalTime) {
                    sendUploadMessage();
                } else {
                    // 如果当前时间减失败时间小于间隔时间，则间隔时间减当前时间与失败时间的差值加随机数delay
                    sendUploadDelayedMessage(intervalTime - difference + getRandomNumb());
                }
            }
        } else {
            SharedUtil.remove(mContext, Constants.SP_FAILURE_COUNT);
            SharedUtil.setLong(mContext, Constants.SP_FAILURE_TIME, System.currentTimeMillis());
        }
        SharedUtil.setLong(mContext, Constants.SP_FAILURE_TIME, System.currentTimeMillis());
    }

    /**
     * 重置重传数据
     */
    private void resetReUploadParams() {
        SharedUtil.remove(mContext, Constants.SP_FAILURE_COUNT);
        SharedUtil.remove(mContext, Constants.SP_FAILURE_TIME);
    }

    /**
     * 获取随机数
     */
    private int getRandomNumb() {
        Random random = new Random();
        return random.nextInt(30 * 1000 - 10 * 1000) + 10 * 1000;
    }

    /**
     * 重传间隔时间
     */
    private long getReUploadIntervalTime() {
        return SharedUtil.getLong(mContext,
                Constants.SP_FAIL_TRY_DELAY, Constants.FAILURE_INTERVAL_TIME);
    }

    /**
     * 失败次数
     */
    private long getReUploadCount() {
        return SharedUtil.getLong(mContext,
                Constants.SP_FAIL_COUNT, Constants.FAILURE_COUNT);
    }

    private static class Holder {
        public static final UploadManager INSTANCE = new UploadManager();
    }

    /**
     * 获取字符数
     */

    public static int getByteLength(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
        }
        return count;
    }

}
