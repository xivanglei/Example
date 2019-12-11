package com.xianglei.analysis;

import android.content.Context;

import com.xianglei.analysis.process.AgentProcess;

/**
 * Author:xianglei
 * Date: 2019-12-11 16:20
 * Description:
 */
public class AnalysisAgent {

    /**
     * 初始化接口
     *
     * @param config 初始化配置信息
     */
    public static void init(Context context, AnalysysConfig config) {
        AgentProcess.getInstance(context).init(config);
    }



}
