package com.dalongtech.analysis;

import com.dalongtech.analysis.constants.Constants;

/**
 * @Copyright © 2018 EGuan Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2018/9/12 18:05
 * @Author: Wang-X-C
 */
public class AnalysysConfig {
    private String channel;
    private String baseUrl;
    private boolean autoProfile = true;
    private EncryptEnum encryptType = EncryptEnum.EMPTY;
    private String appKey;
    private boolean autoInstallation = false;
    private boolean calibration = Constants.isTimeCheck;
    private long diffTime = Constants.ignoreDiffTime;

    /**
     * 获取 App key
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * 设置 App key
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * 获取 APP 渠道
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置 APP 渠道
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 获取追踪新用户的首次属性
     * 默认为 true 追踪
     */
    public boolean isAutoProfile() {
        return autoProfile;
    }

    /**
     * 设置追踪新用户的首次属性
     * 默认为 true 追踪
     */
    public void setAutoProfile(boolean autoProfile) {
        this.autoProfile = autoProfile;
    }

    /**
     * 获取加密方式
     */
    public EncryptEnum getEncryptType() {
        return encryptType;
    }

    /**
     * 设置加密方式
     */
    public void setEncryptType(EncryptEnum encryptType) {
        this.encryptType = encryptType;
    }

    /**
     * 获取渠道归因设置
     */
    public boolean isAutoInstallation() {
        return autoInstallation;
    }

    /**
     * 设置渠道归因是否开启
     * 默认 false 关闭
     */
    public void setAutoInstallation(boolean autoInstallation) {
        this.autoInstallation = autoInstallation;
    }

    /**
     * 设置是否进行时间校准
     */
    public void setAllowTimeCheck(boolean calibration) {
        this.calibration = calibration;
    }

    public boolean isTimeCheck() {
        return calibration;
    }

    /**
     * 设置进行时间校准的误差值
     */
    public void setMaxDiffTimeInterval(long value) {
        this.diffTime = value * 1000;
    }

    public long getMaxDiffTimeInterval() {
        return diffTime;
    }
}

