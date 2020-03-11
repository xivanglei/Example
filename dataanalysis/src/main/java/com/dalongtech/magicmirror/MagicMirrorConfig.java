package com.dalongtech.magicmirror;

import com.dalongtech.magicmirror.constants.Constants;

/**
 * @Copyright © 2018 EGuan Inc. All rights reserved.
 * @Description: TODO
 * @Version: 1.0
 * @Create: 2018/9/12 18:05
 * @Author: Wang-X-C
 */
public class MagicMirrorConfig {
    private String channel;
    private String baseUrl;
    private boolean autoProfile = true;
    private EncryptEnum encryptType = EncryptEnum.EMPTY;
    private String appKey;
    private String cAgent;
    private String partnerCode;
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

//
//    /**
//     * 设置渠道归因是否开启
//     * 默认 false 关闭
//     */
//    public void setAutoInstallation(boolean autoInstallation) {
//        this.autoInstallation = autoInstallation;
//    }

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

    public String getcAgent() {
        return cAgent;
    }

    public void setcAgent(String cAgent) {
        this.cAgent = cAgent;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }
}

