package com.ps.accessservicedemo.io;

import android.view.accessibility.AccessibilityNodeInfo;

public interface IAccessbilityAction {
    /**
     * 模拟后退事件
     */
    void performBack();

    /**
     * 模拟向上滚动
     */
    void performScrollUp();

    /**
     * 模拟向下滚动
     */
    void performScrollDown();

    /**
     * 模拟view点击
     *
     * @param nodeInfo
     */
    void performViewClick(AccessibilityNodeInfo nodeInfo);

    /**
     * 根据文本获取view控件
     *
     * @param text
     * @return
     */
    AccessibilityNodeInfo findViewByText(String text);

    /**
     * 根据文本获取view控件
     *
     * @param text
     * @param clickable
     * @return
     */
    AccessibilityNodeInfo findViewByText(String text, boolean clickable);
    /**
     * 根据文本获取view控件
     *
     * @param viewId
     * @return
     */
    AccessibilityNodeInfo findViewByViewId(String viewId);

    /**
     * 根据文本获取view控件
     *
     * @param viewId
     * @param clickable
     * @return
     */
    AccessibilityNodeInfo findViewByViewId(String viewId, boolean clickable);

    /**
     * 点击指定text的view
     *
     * @param text
     */
    void clickTextViewByText(String text);

    /**
     * 点击指定viewId的view
     *
     * @param viewId
     */
    void clickTextViewByViewId(String viewId);

    /**
     * 模拟文本框输入
     *
     * @param info
     * @param text
     */
    void performInputText(AccessibilityNodeInfo info, String text);

    /**
     *
     * @param serviceName
     * @return
     */
    boolean checkAccessbilityEnabled(String serviceName);
}