package com.ps.accessservicedemo.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;


import com.ps.accessservicedemo.io.IAccessbilityAction;

import java.util.List;
import java.util.Random;


/**
 * @author donghailong
 */
public class BaseAccessibilityService extends AccessibilityService
        implements IAccessbilityAction {
    private static final String TAG = BaseAccessibilityService.class.getSimpleName();
    /**
     *
     */
    private AccessibilityManager mManager;
    private GestureResultCallback gestureCallback = new AccessibilityService.GestureResultCallback() {
        @Override
        public void onCompleted(GestureDescription gestureDescription) {
            Log.e(TAG,"onCompleted="+gestureDescription.toString());
            super.onCompleted(gestureDescription);
//                    Path path2 = new Path();
//                    path2.moveTo(600, 600);
//                    path2.lineTo(600, 800);
//                    final GestureDescription.StrokeDescription sd2 = new GestureDescription.StrokeDescription(path2, 1000, 500);
//                    //滑完后再过1秒竖滑
//                    service.dispatchGesture(new GestureDescription.Builder().addStroke(sd2)/*.addStroke(sd2)*/.build(), null, null);
        }

        @Override
        public void onCancelled(GestureDescription gestureDescription) {
            Log.e(TAG,"onCancelled="+gestureDescription.toString());
            super.onCancelled(gestureDescription);
        }
    };

    public BaseAccessibilityService() {
//        mManager = (AccessibilityManager) getApplicationContext()
//                .getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }


    @Override
    public void performBack() {
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    @Override
    public void performScrollUp() {
        performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
    }

    @Override
    public void performScrollDown() {
        performGlobalAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
    }
    Path path = new Path();
    GestureDescription.StrokeDescription sd;
    GestureDescription gesture;
    Random random = new Random();
    public void dispatchGesture() {
        path.moveTo(350, 1000);
        path.lineTo(350, 100);
        Log.e(TAG,"dispatchGesture=");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (sd == null) {
                sd = new GestureDescription.StrokeDescription(path, 0, 700);
            }
            if (gesture == null) {
                gesture = new GestureDescription.Builder().addStroke(sd).build();
            }
            //先横滑
            dispatchGesture(gesture, gestureCallback, null);
        }
    }

    @Override
    public void performViewClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        while (nodeInfo != null) {
            if (nodeInfo.isClickable()) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            nodeInfo = nodeInfo.getParent();
        }
    }

    @Override
    public AccessibilityNodeInfo findViewByText(String text) {
        return findViewByText(text, false);
    }

    @Override
    public AccessibilityNodeInfo findViewByText(String text, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList =
                accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    @Override
    public AccessibilityNodeInfo findViewByViewId(String viewId) {
        return findViewByViewId(viewId, true);
    }

    @Override
    public AccessibilityNodeInfo findViewByViewId(String viewId, boolean clickable) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList =
                accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && (nodeInfo.isClickable() == clickable)) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    public AccessibilityNodeInfo findViewByViewIdNoClick(String viewId) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return null;
        }
        List<AccessibilityNodeInfo> nodeInfoList =
                accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    return nodeInfo;
                }
            }
        }
        return null;
    }

    @Override
    public void clickTextViewByText(String text) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            Log.i(TAG, "accessibilityNodeInfo is null");
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo
                .findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo);
                    break;
                }
            }
        }
    }

    @Override
    public void clickTextViewByViewId(String viewId) {
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            Log.i(TAG, "accessibilityNodeInfo is null");
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo
                .findAccessibilityNodeInfosByViewId(viewId);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performViewClick(nodeInfo);
                    break;
                }
            }
        }
    }

    @Override
    public void performInputText(AccessibilityNodeInfo info, String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            info.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", text);
            clipboard.setPrimaryClip(clip);
            info.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            info.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }
    }

    @Override
    public boolean checkAccessbilityEnabled(String serviceName) {
        List<AccessibilityServiceInfo> accessibilityServices =
                mManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}
