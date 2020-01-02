package com.dalongtech.analysis.process;

import android.content.Context;
import android.text.TextUtils;

import com.dalongtech.analysis.constants.Constants;
import com.dalongtech.analysis.constants.ExtraConst;
import com.dalongtech.analysis.utils.CheckUtils;
import com.dalongtech.analysis.utils.CommonUtils;
import com.dalongtech.analysis.utils.LogPrompt;
import com.dalongtech.analysis.utils.ParameterAddUtil;
import com.dalongtech.analysis.utils.SharedUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Author:xianglei
 * Date: 2019-12-12 09:33
 * Description:
 */
public class DataAssemble {

    private final String OUTER = "outer";
    private final String VALUE = "value";
    private final String VALUE_TYPE = "valueType";
    Context mContext;

    public static DataAssemble getInstance(Context context) {
        if (Holder.INSTANCE.mContext == null && context != null) {
            Holder.INSTANCE.mContext = context;
        }
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static DataAssemble INSTANCE = new DataAssemble();
    }

    /**
     * 获取完整上传Json数据
     * 参数1.API名称
     * 参数2.eventName
     * 参数3.用户传参
     * 参数4.默认采集
     * 参数5.eventName track事件使用
     */
    public JSONObject getEventData(Object... values) throws JSONException {
        if (values != null && mContext != null) {
            String apiName = String.valueOf(values[0]);
            String eventName = String.valueOf(values[1]);
            Map<String, Object> data = toMap(values[2]);
            if (Constants.TRACK.equals(eventName)) {
                String eventInfo = String.valueOf(values[4]);
                if (!CheckUtils.checkTrackEventName(eventName, eventInfo)) {
                    LogPrompt.showLog(apiName, LogBean.getLog());
                    return null;
                }
                eventName = eventInfo;
            }
            // 校验用户传参
            CheckUtils.checkParameter(apiName, data);
            if (data == null) {
                data = new HashMap<String, Object>();
            }
            mergeParameter(data, values[3]);
            mergeSuperProperty(eventName, data);
            return fillData(eventName, String.valueOf(values[1]), data);
        }
        return null;
    }

    /**
     * 转 map
     */
    private Map<String, Object> toMap(Object data) {
        if (data != null) {
            return (Map<String, Object>) data;
        }
        return null;
    }

    private void mergeParameter(Map<String, Object> userParameter, Object autoParameter) {
        Map<String, Object> autoMap = toMap(autoParameter);
        CommonUtils.clearEmptyValue(autoMap);
        if (autoMap != null) {
            userParameter.putAll(autoMap);
            autoMap.clear();
        }
    }

    /**
     * 添加通用属性
     */
    private void mergeSuperProperty(String eventName, Map<String, Object> data) throws JSONException {
        if (!Constants.LOGIN.equals(eventName) && !eventName.startsWith(Constants.PROFILE)) {
            String property = SharedUtil.getString(
                    mContext, Constants.SP_SUPER_PROPERTY, null);
            if (!TextUtils.isEmpty(property)) {
                JSONObject superProperty = null;
                superProperty = new JSONObject(property);
                Iterator<String> keys = superProperty.keys();
                String key = null;
                while (keys.hasNext()) {
                    key = keys.next();
                    if (key != null) {
                        data.put(key, superProperty.opt(key));
                    }
                }
            }
        }
    }

    /**
     * 通过遍历字段模板填充数据
     */
    private JSONObject fillData(String eventName, String optionName, Map<String, Object> xContextMap) throws JSONException {
        JSONObject allJob = new JSONObject();
        putBaseInfo(allJob, eventName);
        ParameterAddUtil.putXContextInfo(xContextMap, optionName);
        CommonUtils.clearEmptyValue(xContextMap);
        allJob.put(ExtraConst.X_CONTEXT, new JSONObject(xContextMap));
        return allJob;
    }

    private void putBaseInfo(JSONObject allJob, String eventName) {
        CommonUtils.pushToJSON(allJob, Constants.EVENT, eventName);
        CommonUtils.pushToJSON(allJob, ExtraConst.PLATFORM, ExtraConst.C_V_PLATFORM);
        CommonUtils.pushToJSON(allJob, ExtraConst.C_CID, CommonUtils.getCId(mContext));
        CommonUtils.pushToJSON(allJob, ExtraConst.C_AGENT, CommonUtils.getCAgent(mContext));
        CommonUtils.pushToJSON(allJob, ExtraConst.C_APP_VERSION, CommonUtils.getVersionName(mContext));
        CommonUtils.pushToJSON(allJob, ExtraConst.C_PARTNER_CODE, CommonUtils.getPartnerCode(mContext));
        CommonUtils.pushToJSON(allJob, Constants.APP_KEY, CommonUtils.getAppKey(mContext));
        CommonUtils.pushToJSON(allJob, ExtraConst.C_CHANNEL, CommonUtils.getChannel(mContext));
        CommonUtils.pushToJSON(allJob, ExtraConst.C_SDK_VERSION, Constants.DEV_SDK_VERSION);
        CommonUtils.pushToJSON(allJob, Constants.TIME_STAMP, System.currentTimeMillis() / 1000);
    }
}
