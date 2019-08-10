package com.ps.accessservicedemo.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.ps.accessservicedemo.MainActivity;
import com.ps.accessservicedemo.other.MeetAndroidApplication;
import com.ps.accessservicedemo.tools.PacketUtil;



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
    public static final String QK_PACKAGE_NAME = "com.jifen.qukan";
    private static final int QK_PACKAGE_NAME_VALUE = 520;
    public static final String XYZQ = "com.xiaoyuzhuanqian";
    public static final String ZHUANKE = "cn.zhuanke.zhuankeAPP";
    public static final String MUI_INSTALLER = "com.miui.packageinstaller";
    public static final String MUI_securitycenter = "om.miui.securitycenter";
    public static final String MUI_security_MUI = "com.lbe.security.miui";
    public static final String LYQ = "cn.lingyongqian.stark";
    public static final String XIAOZHUO = "com.xzzq.xiaozhuo";
    public static final String DDQW = "com.ddfun";

    public static final String DINGDING = "com.alibaba.android.rimet";

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
    private boolean isSwiped = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case QK_PACKAGE_NAME_VALUE:
                    dispatchGesture(true,"小视频");
                    isSwiped = true;
                    handler.removeMessages(QK_PACKAGE_NAME_VALUE);
                    break;
            }
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        CharSequence pkg = event.getPackageName();
        Log.i(TAG, "event pkg:" + pkg+" event type:" + event.getEventType());
        if (TextUtils.isEmpty(pkg)) return;
//        if (click) return;
//        try {
//            Thread.sleep(10*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        click = true;
////        openApp(DINGDING);
//        PacketUtil.openAPP(DINGDING);
        switch (pkg.toString()) {
            case XYZQ:
                autoForXyzq();
                break;
            case ZHUANKE:
                autoForZk();
                break;
            case LYQ:
                autoForLYQ();
                break;
            case XIAOZHUO:
                autoForXiaozhuo();
                break;
            case DDQW:
                autoForDDQW();
                break;
            case MUI_securitycenter:
            case MUI_security_MUI:
                AccessibilityNodeInfo yesBT = findViewByText("允许",true);
                if (yesBT != null) {
                    boolean success = yesBT.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    Log.i(TAG, "MUI_securitycenter success :" + success);
                }
                break;
            case MUI_INSTALLER:
                autoInstall();
                break;
            case SB_PACKAGE_NAME:
                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SELECTED) {
                    if (lastResumeTime == 0L) {
                        lastResumeTime = System.currentTimeMillis();
                        return;
                    }
                    long now = System.currentTimeMillis();
                    Log.d(TAG, "resume now = "+now+" lastResumeTime = "+lastResumeTime+" offset "+(now-lastResumeTime));
                    if (now-lastResumeTime >10*1000) {
                        Log.d(TAG, "dispatchGesture");
                        lastResumeTime = System.currentTimeMillis();
                        dispatchGesture(true,"首页");
                    }
                }
                break;
            case QK_PACKAGE_NAME:
                Log.e(TAG, "QK_PACKAGE_NAME isSwiped -- "+isSwiped+" hasMessages -- "+handler.hasMessages(QK_PACKAGE_NAME_VALUE));
                swipeDelay(QK_PACKAGE_NAME_VALUE);
                break;
                default:
                    break;
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

    private void swipeDelay(int what,int randomTime) {
        if (isSwiped) {
            isSwiped = false;
            if (randomTime == 0) {
                randomTime = getRandomNum(18, 32);
            }
            Log.e(TAG, "what: "+what+" randomTime: "+ randomTime);
            handler.sendEmptyMessageDelayed(what,randomTime*1000);
        }
    }

    private void swipeDelay(int what) {
        swipeDelay(what,0);
    }

    private void autoForDDQW() {
        AccessibilityNodeInfo leftRB = findViewByViewIdNoClick("com.ddfun:id/tab_activity_radiogbutton1");
        AccessibilityNodeInfo rightRB = findViewByViewIdNoClick("com.ddfun:id/tab_activity_radiogbutton2");
        if (rightRB != null && rightRB.isChecked()) {
            AccessibilityNodeInfo yesBT = findViewByText("开始赚钱",true);
            if (yesBT == null) {
                yesBT = findViewByText("打开",true);
                Log.i(TAG, "打开 ....:");
            }
            if (yesBT != null) {
                boolean success = yesBT.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                Log.i(TAG, "dou dou qu wan click success :" + success);
                if (success) {
                    delaySecond(16);
//                    Toast.makeText(AutoGetPacketService.this,"可以了..........",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void autoForXiaozhuo() {
        AccessibilityNodeInfo dialogTitle = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/task_title");
        if (dialogTitle != null) {
            CharSequence title = dialogTitle.getText();
            if (!TextUtils.isEmpty(title)&& ("每日任务".equals(title))) {
                AccessibilityNodeInfo startEveryDay = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/task_button_right");
                if (startEveryDay != null) {
                    boolean startEveryDayBtOk = startEveryDay.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    try {
                        Thread.sleep(1*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AccessibilityNodeInfo okIKnow = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/task_guide_confirm_btn");
                    if (okIKnow != null) {
                        try {
                            Thread.sleep(4*1000);
                            boolean okIKnowOk = okIKnow.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else {
                AccessibilityNodeInfo changeBt = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/task_button_left");
                if (changeBt == null) return;
                boolean changeBtOk = changeBt.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return;
            }
        }
        //每日任务
        AccessibilityNodeInfo status1 = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/normal_task_status_1");
        performClickXiaoZhuo(status1);
        AccessibilityNodeInfo status2 = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/normal_task_status_2");
        performClickXiaoZhuo(status2);
        AccessibilityNodeInfo status3 = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/normal_task_status_3");
        performClickXiaoZhuo(status3);
        AccessibilityNodeInfo openAd = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/video_task_open_ad");
        performClickXiaoZhuo(openAd);

        AccessibilityNodeInfo confirmBt = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/daily_task_confirm_btn");
        if (confirmBt != null) {
            boolean confirmBtOk = confirmBt.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
        AccessibilityNodeInfo goonBt = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/finish_task_left_btn");
        if (goonBt != null) {
            boolean goonBtOk = goonBt.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private boolean performClickXiaoZhuo(AccessibilityNodeInfo status) {
        if (status != null) {
            CharSequence text = status.getText();
            if (!TextUtils.isEmpty(text)&& ("打开试玩".equals(text) || "去观看".equals(text))) {
                boolean success = status.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                delaySecond(31);
                AccessibilityNodeInfo closeAd = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/tt_video_ad_close");
                if (closeAd != null) {
                    boolean closeAdSuccess = closeAd.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return closeAdSuccess;
                }
                delaySecond(1);
                return success;
            }
        }
        return false;
    }

    private void delaySecond(int count) {
        try {
            Thread.sleep(count*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void autoForLYQ() {
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
        AccessibilityNodeInfo downProgress = findViewByViewIdNoClick("cn.lingyongqian.stark:id/progress");
        if (downProgress != null) return;
        AccessibilityNodeInfo begin_get_money_btn = findViewByViewIdNoClick("cn.lingyongqian.stark:id/do_task");
        if (begin_get_money_btn != null) {
            boolean success = begin_get_money_btn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void autoInstall() {
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
    }

    private void autoForZk() {
        AccessibilityNodeInfo downProgress = findViewByViewIdNoClick("cn.zhuanke.zhuankeAPP:id/downProgress");
        if (downProgress != null) return;
        AccessibilityNodeInfo clickIKNOW = findViewByViewIdNoClick("cn.zhuanke.zhuankeAPP:id/dialog_btn");
        if (clickIKNOW != null) {
            boolean success = clickIKNOW.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.i(TAG, "ZHUANKE success :" + success);
        }
    }

    private void autoForXyzq() {
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
                dispatchGesture(true);
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
