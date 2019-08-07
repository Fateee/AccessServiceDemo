package com.ps.accessservicedemo.service;

import android.content.IntentFilter;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;


/**
 * @author donghailong
 */
public class AutoOpenRedPacketService extends BaseAccessibilityService {
    private static final String TAG = AutoOpenRedPacketService.class.getSimpleName();
    /**
     * wx包名
     */
    public static final String WX_PACKAGE_NAME = "com.tencent.mm";
    public static final String SB_PACKAGE_NAME = "com.jm.video";

    /**
     * text
     */
    public static final String WX_RED_PACKAGE_TEXT = "微信红包";
    /**
     * 开 对应的viewId（随着微信的更新，可能会变）
     */
    public static final String WX_OPEN_RED_PACKAGE_VIEW_ID = "com.tencent.mm:id/c31";
    private long lastResumeTime;


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "event type:" + event.getEventType());
        AccessibilityNodeInfo seekbar = findViewByViewIdNoClick("com.jm.video:id/seek_bar");
        if (seekbar != null) {
//            Log.i(TAG, "seekbar :" + seekbar.toString());
        }
        String packageName = event.getPackageName().toString();
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SELECTED && packageName.equals(SB_PACKAGE_NAME)) {
            if (lastResumeTime == 0L) {
                lastResumeTime = System.currentTimeMillis();
                return;
            }
            long now = System.currentTimeMillis();
            Log.d(TAG, "resume now = "+now+" lastResumeTime = "+lastResumeTime+" offset "+(now-lastResumeTime));
            if (now-lastResumeTime >10*1000) {
                Log.d(TAG, "dispatchGesture");
                lastResumeTime = System.currentTimeMillis();
                dispatchGesture();
            }
        } else {
            while (true) {
                try {
                    int randomTime = getRandomNum(13, 30);
                    Thread.sleep(randomTime * 1000);
                    dispatchGesture();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        while (true) {
//            try {
//                Thread.sleep(10*1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            dispatchGesture();
//        }
//        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
//                && event.getPackageName().equals(WX_PACKAGE_NAME)) {
//            AccessibilityNodeInfo viewByText = findViewByText(WX_RED_PACKAGE_TEXT);
//            if (viewByText != null) {
//                performViewClick(viewByText);
//                clickTextViewByViewId(WX_OPEN_RED_PACKAGE_VIEW_ID);
//            } else {
//                AccessibilityNodeInfo viewByViewId = findViewByViewId("com.tencent.mm:id/apx");
//                if (null != viewByViewId
//                        && viewByViewId.getText().toString().contains(WX_RED_PACKAGE_TEXT)) {
//                    performViewClick(viewByViewId);
//                    clickTextViewByViewId(WX_OPEN_RED_PACKAGE_VIEW_ID);
//                }
//            }
//        } else if (event.getEventType() == 4f
//                && event.getPackageName().equals(SB_PACKAGE_NAME)) {
//            Log.e(TAG, "event type:" + SB_PACKAGE_NAME);
//            dispatchGesture();
////            performScrollUp();
//        }
    }

    @Override
    public void onInterrupt() {

    }
}
