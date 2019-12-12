package com.xianglei.analysis.process;

import android.content.Context;

import com.xianglei.analysis.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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
