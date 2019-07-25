package com.ps.accessservicedemo.service;

import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


/**
 * @author donghailong
 */
public class AutoGetPacketService extends BaseAccessibilityService {
    private static final String TAG = AutoGetPacketService.class.getSimpleName();
    /**
     * wx包名
     */
    public static final String WX_PACKAGE_NAME = "com.tencent.mm";
    public static final String SB_PACKAGE_NAME = "com.jm.video";
    private static final String XYZQ = "com.xiaoyuzhuanqian";
    /**
     * text
     */
    public static final String WX_RED_PACKAGE_TEXT = "微信红包";
    /**
     * 开 对应的viewId（随着微信的更新，可能会变）
     */
    public static final String WX_OPEN_RED_PACKAGE_VIEW_ID = "com.tencent.mm:id/c31";
    private long lastResumeTime;
    boolean click = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "event type:" + event.getEventType());
        CharSequence pkg = event.getPackageName();
        if (TextUtils.isEmpty(pkg)) return;
        if (event.getPackageName().equals(XYZQ)) {
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AccessibilityNodeInfo task_list_rl = findViewByViewIdNoClick("com.xiaoyuzhuanqian:id/task_list_rl");
            if (task_list_rl != null) {
                Log.i(TAG, "task_list_rl :" + task_list_rl.toString());
                Log.i(TAG, "count  :" + task_list_rl.getChildCount());
                int count = task_list_rl.getChildCount();
                for (int i = 0; i < count; i++) {
                    AccessibilityNodeInfo item = task_list_rl.getChild(i);
                    if (item != null) {
                        Log.i(TAG, "item :" + item.toString()+" item count  :" + item.getChildCount());
                        AccessibilityNodeInfo appStoreNameNode = findViewByViewId(item,"com.xiaoyuzhuanqian:id/appstore_name");
                        if (appStoreNameNode != null) {
                            CharSequence appStoreName = appStoreNameNode.getText();
                            if (TextUtils.isEmpty(appStoreName)) return;
                            Log.i(TAG, "list appStoreName :" + appStoreName);
                            if (appStoreName.toString().contains("华为")) continue;
                            boolean success = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (success) break;
                        }
                    }
                }
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AccessibilityNodeInfo begin_get_money_btn = findViewByViewIdNoClick("com.xiaoyuzhuanqian:id/begin_get_money_btn");
                if (begin_get_money_btn != null) {
                    boolean success = begin_get_money_btn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        }

//        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SELECTED) {
//            if (lastResumeTime == 0L) {
//                lastResumeTime = System.currentTimeMillis();
//                return;
//            }
//            long now = System.currentTimeMillis();
//            Log.d(TAG, "resume now = "+now+" lastResumeTime = "+lastResumeTime+" offset "+(now-lastResumeTime));
//            if (now-lastResumeTime >10*1000) {
//                Log.d(TAG, "dispatchGesture");
//                lastResumeTime = System.currentTimeMillis();
////                dispatchGesture();
//            }
//        }
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
