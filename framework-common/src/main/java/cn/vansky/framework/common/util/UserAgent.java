/*
 * Copyright (C) 2015 CK, Inc. All Rights Reserved.
 */

package cn.vansky.framework.common.util;

/**
 * 根据 user agent string 来判断出客户端的浏览器以及平台等信息
 * Author: CK
 * Date: 2015/9/22
 */
public class UserAgent {
    /**
     * 浏览器类型
     */
    private String browserType;
    /**
     * 浏览器版本
     */
    private String browserVersion;
    /**
     * 平台类型
     */
    private String platformType;
    /**
     * 平台系列
     */
    private String platformSeries;
    /**
     * 平台版本
     */
    private String platformVersion;

    /**
     * UserAgent
     */
    public UserAgent() {
    }

    /**
     * 构造方法
     *
     * @param browserType
     *            浏览器类型
     * @param browserVersion
     *            浏览器版本
     * @param platformType
     *            平台类型
     * @param platformSeries
     *            平台系列
     * @param platformVersion
     *            平台版本
     */
    public UserAgent(String browserType, String browserVersion, String platformType,
                     String platformSeries, String platformVersion) {
        this.browserType = browserType;
        this.browserVersion = browserVersion;
        this.platformType = platformType;
        this.platformSeries = platformSeries;
        this.platformVersion = platformVersion;
    }

    /**
     * @return the browserType
     */
    public String getBrowserType() {
        return browserType;
    }

    /**
     * @param browserType
     *            the browserType to set
     */
    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    /**
     * @return the browserVersion
     */
    public String getBrowserVersion() {
        return browserVersion;
    }

    /**
     * @param browserVersion
     *            the browserVersion to set
     */
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    /**
     * @return the platformType
     */
    public String getPlatformType() {
        return platformType;
    }

    /**
     * @param platformType
     *            the platformType to set
     */
    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    /**
     * @return the platformSeries
     */
    public String getPlatformSeries() {
        return platformSeries;
    }

    /**
     * @param platformSeries
     *            the platformSeries to set
     */
    public void setPlatformSeries(String platformSeries) {
        this.platformSeries = platformSeries;
    }

    /**
     * @return the platformVersion
     */
    public String getPlatformVersion() {
        return platformVersion;
    }

    /**
     * @param platformVersion
     *            the platformVersion to set
     */
    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }
}
