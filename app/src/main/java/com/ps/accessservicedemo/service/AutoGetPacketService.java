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
    public static final String XYZQ = "com.xiaoyuzhuanqian";
    public static final String ZHUANKE = "cn.zhuanke.zhuankeAPP";
    public static final String MUI_INSTALLER = "com.miui.packageinstaller";
    public static final String MUI_securitycenter = "om.miui.securitycenter";
    public static final String MUI_security_MUI = "com.lbe.security.miui";
    public static final String LYQ = "cn.lingyongqian.stark";
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
        CharSequence pkg = event.getPackageName();
        Log.i(TAG, "event pkg:" + pkg+" event type:" + event.getEventType());
        if (TextUtils.isEmpty(pkg)) return;
        if (event.getPackageName().equals(XYZQ)) {
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
            } else {
                if (lastResumeTime == 0L) {
                    lastResumeTime = System.currentTimeMillis();
                    return;
                }
                long now = System.currentTimeMillis();
                Log.d(TAG, "resume now = "+now+" lastResumeTime = "+lastResumeTime+" offset "+(now-lastResumeTime));
                if (now-lastResumeTime >10*1000) {
                    Log.d(TAG, "dispatchGesture");
                    lastResumeTime = System.currentTimeMillis();
                    dispatchGesture(false);
                }
            }
        } else if (event.getPackageName().equals(ZHUANKE)) {
//            AccessibilityNodeInfo title = findViewByText("试玩任务详情");
//            if (title != null) {
//
//            }
            AccessibilityNodeInfo downProgress = findViewByViewIdNoClick("cn.zhuanke.zhuankeAPP:id/downProgress");
            if (downProgress != null) return;
            AccessibilityNodeInfo clickIKNOW = findViewByViewIdNoClick("cn.zhuanke.zhuankeAPP:id/dialog_btn");
            if (clickIKNOW != null) {
                boolean success = clickIKNOW.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i(TAG, "ZHUANKE success :" + success);
            }
        }  else if (event.getPackageName().equals(MUI_securitycenter) || event.getPackageName().equals(MUI_security_MUI)) {
            AccessibilityNodeInfo yesBT = findViewByText("允许",true);
            if (yesBT != null) {
                boolean success = yesBT.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i(TAG, "MUI_securitycenter success :" + success);
            }
        } else if (event.getPackageName().equals(MUI_INSTALLER)) {
            AccessibilityNodeInfo clickIKNOW = findViewByViewIdNoClick("com.miui.packageinstaller:id/ok_button");//安装
            if (clickIKNOW != null) {
                boolean success = clickIKNOW.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i(TAG, "MUI_INSTALLER success :" + success);
            }
//            com.miui.packageinstaller:id/done_button
//            com.miui.packageinstaller:id/launch_button
            AccessibilityNodeInfo launchBT = findViewByViewIdNoClick("com.miui.packageinstaller:id/launch_button");//打开
            if (launchBT != null) {
                boolean success = launchBT.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i(TAG, "MUI_INSTALLER launchBT success :" + success);
                try {
                    Thread.sleep(3*60*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                performGlobalAction(GLOBAL_ACTION_BACK);
//                performGlobalAction(GLOBAL_ACTION_BACK);
            }
        } else if (event.getPackageName().equals(LYQ)) {
            AccessibilityNodeInfo recyclerView = findViewByViewIdNoClick("cn.lingyongqian.stark:id/recyclerView");
            if (recyclerView != null) {
                Log.i(TAG, "recyclerView :" + recyclerView.toString());
                Log.i(TAG, "count  :" + recyclerView.getChildCount());
                int count = recyclerView.getChildCount();
                for (int i = 6; i < count; i++) {
                    AccessibilityNodeInfo item = recyclerView.getChild(i);
                    if (item != null) {
                        boolean success = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.i(TAG, "LYQ click item :" + success);
                        if (success) break;
                    }
                }
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            AccessibilityNodeInfo begin_get_money_btn = findViewByViewIdNoClick("cn.lingyongqian.stark:id/do_task");
            if (begin_get_money_btn != null) {
                boolean success = begin_get_money_btn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            AccessibilityNodeInfo downProgress = findViewByViewIdNoClick("cn.lingyongqian.stark:id/progress");
            if (downProgress != null) return;
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
