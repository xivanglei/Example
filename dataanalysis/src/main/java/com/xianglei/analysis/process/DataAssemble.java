package com.xianglei.analysis.process;

import android.content.Context;
import android.text.TextUtils;

import com.xianglei.analysis.constants.Constants;
import com.xianglei.analysis.utils.CheckUtils;
import com.xianglei.analysis.utils.CommonUtils;
import com.xianglei.analysis.utils.LogPrompt;
import com.xianglei.analysis.utils.SharedUtil;

import org.json.JSONArray;
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
            JSONObject eventMould = getEventMould(eventName);
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
            return fillData(eventName, eventMould, data);
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
        if (!Constants.ALIAS.equals(eventName) && !eventName.startsWith(Constants.PROFILE)) {
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
    private JSONObject fillData(String eventName, JSONObject eventMould, Map<String, Object> xContextMap) throws JSONException {
        JSONObject allJob = null;
        putBaseInfo(allJob, eventName);
        JSONArray outerKeysArray = eventMould.optJSONArray(OUTER);
        String outFields = null;
        JSONObject fieldsRuleMould = null;
        JSONArray xContextFieldsArray = null;
        Map<String, Object> map = new HashMap<String, Object>();
        if (outerKeysArray != null) {
            allJob = new JSONObject();
            for (int i = 0; i < outerKeysArray.length(); i++) {
                outFields = outerKeysArray.optString(i);
                fieldsRuleMould = TemplateManage.ruleMould.optJSONObject(outFields);
                xContextFieldsArray = eventMould.optJSONArray(outFields);
                if (xContextFieldsArray != null) {
                    int length = xContextFieldsArray.length();
                    String xContextFields = null;
                    for (int j = 0; j < length; j++) {
                        xContextFields = xContextFieldsArray.optString(j);
                        map.put(xContextFields,
                                getValue(fieldsRuleMould.optJSONObject(xContextFields), null));
                    }
                    if (!CommonUtils.isEmpty(map)) {
                        CommonUtils.clearEmptyValue(map);
                        xContextMap.putAll(map);
                        map.clear();
                    }
                    allJob.put(outFields, new JSONObject(xContextMap));
                } else {
                    //  3.获取value并校验
                    Object outerValue = getValue(fieldsRuleMould, eventName);
                    CommonUtils.pushToJSON(allJob, outFields, outerValue);
                }
            }
        }
        return allJob;
    }

    private void putBaseInfo(JSONObject allJob, String eventName) {
        CommonUtils.pushToJSON(allJob, Constants.APP_ID, CommonUtils.getAppKey(mContext));
        CommonUtils.pushToJSON(allJob, Constants.X_WHAT, eventName);
        CommonUtils.pushToJSON(allJob, Constants.X_WHEN, System.currentTimeMillis());
    }

    private void putXContextData() {

    }

    /**
     * 获取事件字段模板
     */
    private JSONObject getEventMould(String eventName) {
        if (eventName.startsWith(Constants.PROFILE)) {
            return TemplateManage.fieldsMould.optJSONObject(Constants.PROFILE);
        }
        return TemplateManage.fieldsMould.optJSONObject(eventName);
    }
}
