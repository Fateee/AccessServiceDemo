package com.ps.accessservicedemo.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.GestureDescription;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;


import com.ps.accessservicedemo.io.IAccessbilityAction;
import com.ps.accessservicedemo.other.MeetAndroidApplication;
import com.ps.accessservicedemo.tools.CameraUtils;

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
    private GestureResultCallback gestureCallback;

    public BaseAccessibilityService() {
//        mManager = (AccessibilityManager) getApplicationContext()
//                .getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"onStartCommand=");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG,"onStart=");
        super.onStart(intent, startId);
    }

    @Override
    protected void onServiceConnected() {
        Log.e(TAG,"onServiceConnected=");
        super.onServiceConnected();
    }

    @Override
    public void onInterrupt() {
        Log.e(TAG,"onInterrupt=");
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
//    random_x = random.randint(1, 9)
//    random_y1 = random.randint(7, 8)
//    random_y2 = random.randint(1, 2)
//    random_x2 = random.randint(1, 10)
//    x1 = int(l[0] * random_x * 0.1)  # x坐标
//    y1 = int(l[1] * random_y1 * 0.1)  # 起始y坐标
//    y2 = int(l[1] * random_y2 * 0.1)  # 终点y坐标
//            x2 = x1+random_x2
    public void dispatchGesture() {
        int random_x1 = getRandomNum(1,9);
        int random_y1 = getRandomNum(7, 9);
        int random_x2 = getRandomNum(1, 20);
        int random_y2 = getRandomNum(1, 3);
        if (CameraUtils.realWidth == 0 || CameraUtils.realHeight == 0) {
            CameraUtils.getPingMuSize(MeetAndroidApplication.getInstance());
        }
        double x1 = CameraUtils.realWidth * random_x1 * 0.1;
        double y1 = CameraUtils.realHeight * random_y1 * 0.1;
        double x2 = x1+random_x2;
        double y2 = CameraUtils.realHeight * random_y2 * 0.1;

        path.moveTo((float) x1, (float) y1);
        path.lineTo((float) x2, (float) y2);
        Log.e(TAG,"dispatchGesture x1 = "+x1+" y1 = "+y1+" x2 = "+x2+" y2 = "+y2);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (sd == null) {
                sd = new GestureDescription.StrokeDescription(path, 0, 700);
            }
            if (gesture == null) {
                gesture = new GestureDescription.Builder().addStroke(sd).build();
            }
            //先横滑
            dispatchGesture(gesture, gestureCallback, null);
        } else {
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
    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     * @param startNum
     * @param endNum
     * @return
     */
    public int getRandomNum(int startNum,int endNum){
        if(endNum > startNum){
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }
}
