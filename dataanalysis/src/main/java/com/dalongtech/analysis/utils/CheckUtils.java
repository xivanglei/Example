package com.dalongtech.analysis.utils;

import android.text.TextUtils;
import com.dalongtech.analysis.constants.Constants;
import com.dalongtech.analysis.constants.ExtraConst;
import com.dalongtech.analysis.process.LogBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Copyright © 2019 EGuan Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2019/2/22 16:43
 * @Author: Wang-X-C
 */
public class CheckUtils {

    /**
     * 校验数据是否符合上传格式
     */
    public static JSONObject checkField(JSONObject eventInfo) {
        if (eventInfo != null) {
            String appkey = eventInfo.optString(Constants.APP_KEY);
            long timeStamp = eventInfo.optLong(Constants.TIME_STAMP);
            String user = eventInfo.optString(Constants.USER);
            String event = eventInfo.optString(Constants.EVENT);
            JSONObject xContext = eventInfo.optJSONObject(Constants.X_CONTEXT);
            if (TextUtils.isEmpty(appkey) || TextUtils.isEmpty(appkey.trim())
                    || timeStamp == 0
                    || TextUtils.isEmpty(user) || TextUtils.isEmpty(user.trim())
                    || TextUtils.isEmpty(event) || TextUtils.isEmpty(event.trim())) {
                return null;
            }
        }
        return eventInfo;
    }

    public static boolean checkIdLength(String id) {
        if (CommonUtils.isEmpty(id) || id.length() > 255) {
            LogBean.setDetails(Constants.CODE_FAILED, LogPrompt.ID_LENGTH_ERROR);
            return false;
        }
        return true;
    }

    public static boolean checkOriginalIdLength(String id) {
        if (id.length() > 255) {
            LogBean.setDetails(Constants.CODE_FAILED, LogPrompt.ID_LENGTH_ERROR);
            return false;
        }
        return true;
    }

    /**
     * 过滤掉value为空的数据
     */
    public static void checkToMap(Map<String, Object> map, String key, String value) {
        try {
            if (!CommonUtils.isEmpty(key) && !CommonUtils.isEmpty(value)) {
                map.put(key, value);
            }
        } catch (Throwable e) {
            //ANSLog.e(e);
        }
    }

    /**
     * 用户接口传参校验
     */
    public static boolean checkParameter(String apiName, Map<String, Object> parameters) {
        if (parameters != null) {
            LogBean.resetLogBean();
            if (ExtraConst.USER_KEYS_LIMIT > 0 && ExtraConst.USER_KEYS_LIMIT < parameters.size()) {
                LogBean.setDetails(Constants.CODE_FAILED, LogPrompt.MAP_SIZE_ERROR);
            }
            Set<String> keys = parameters.keySet();
            String key;
            Object value;
            for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
                key = iterator.next();
                value = parameters.get(key);
                if (key == null) {
                    iterator.remove();
                    LogBean.setDetails(Constants.CODE_FAILED, LogPrompt.KEY_EMPTY);
                    continue;
                }
                if (Constants.CODE_FAILED == ParameterCheck.checkKey(key)) {
                    continue;
                }
                if (Constants.CODE_FAILED == ParameterCheck.checkValue(value)) {
                    continue;
                }
                if (LogBean.getCode() == Constants.CODE_CUT_OFF
                        && !TextUtils.isEmpty(LogBean.getValue())) {
                    parameters.put(key, LogBean.getValue());
                    LogBean.setValue(null);
                }
            }
            if (Constants.API_PROFILE_UNSET.equals(apiName)) {
                LogBean.setDetails(Constants.CODE_SUCCESS, null);
            }
            if (LogBean.getCode() != Constants.CODE_SUCCESS) {
                LogPrompt.showLog(apiName, LogBean.getLog());
            }
        }
        return true;
    }

    /**
     * 校验track事件eventName
     */
    public static boolean checkTrackEventName(String eventName, String eventInfo) {
        return ParameterCheck.checkEventName(eventInfo) == Constants.CODE_SUCCESS;
    }

    /**
     * 通过反射funcList内方法校验key
     */
    private static int reflexCheckParameter(String apiName, Object data, JSONArray methodArray) {
        if (methodArray == null) {
            return Constants.CODE_CUT_OFF;
        }
        String path;
        for (int i = 0; i < methodArray.length(); i++) {
            path = methodArray.optString(i);
            CommonUtils.reflexUtils(
                    CommonUtils.getClassPath(path),
                    CommonUtils.getMethod(path),
                    new Class[]{Object.class}, data);
        }
        return LogBean.getCode();
    }
}
