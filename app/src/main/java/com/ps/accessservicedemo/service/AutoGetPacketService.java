package com.ps.accessservicedemo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.ps.accessservicedemo.io.Consts;
import com.ps.accessservicedemo.other.MeetAndroidApplication;
import com.ps.accessservicedemo.tools.ForegroundAppUtil;
import com.white.easysp.EasySP;

import java.util.List;

import static com.ps.accessservicedemo.io.Consts.*;


/**
 * @author donghailong
 */
public class AutoGetPacketService extends BaseAccessibilityService {
    private static final String TAG = AutoGetPacketService.class.getSimpleName();

    public static int defaultTime = 0;
    /**
     * text
     */
    public static final String WX_RED_PACKAGE_TEXT = "微信红包";
    /**
     * 开 对应的viewId（随着微信的更新，可能会变）
     */
    public static final String WX_OPEN_RED_PACKAGE_VIEW_ID = "com.tencent.mm:id/c31";
    private long lastResumeTime;
    private boolean isSwiped = true;
    boolean startVideo = false;
    boolean startRead = false;
    private boolean isRefreshed = true;
    private Runnable autoSwipeRunable = () -> swipeDelay(AUTO_RANDOM_PLAY);
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            handler.removeCallbacksAndMessages(null);
        }
    };
    private IntentFilter intentFilter = new IntentFilter(Consts.RESET_TIME_ACTION);
    private LocalBroadcastManager localBroadcastManager;

    public class ClickRunable implements Runnable {
        public String id;

        @Override
        public void run() {
            AccessibilityNodeInfo node = findViewByViewId(id);
            performViewClick(node);
        }
    }

    private ClickRunable followBtRunable = new ClickRunable();
    private ClickRunable praiseBtRunable = new ClickRunable();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Message tempMsg = Message.obtain();
            switch (msg.what) {
                case QK_PACKAGE_NAME_VALUE:
                    if (isButtonCheckedByText("小视频")) {
                        dispatchGesture(true,"小视频");
                        tempMsg.what = SWIPE_VALUE;
                        tempMsg.arg1 = QK_PACKAGE_NAME_VALUE;
                        tempMsg.obj = false;
                        sendMessageDelayed(tempMsg,2000);
                    } else {
                        isSwiped = true;
                        removeCallbacksAndMessages(null);
                        startVideo = false;
                    }
                    break;
                case XYZQ_PACKAGE_NAME_VALUE:
                    if (isButtonCheckedByText("短视频")) {
                        dispatchGesture(true,"短视频");
                        tempMsg.what = SWIPE_VALUE;
                        tempMsg.arg1 = XYZQ_PACKAGE_NAME_VALUE;
                        tempMsg.obj = true;
                        sendMessageDelayed(tempMsg,2000);
                    } else {
                        isSwiped = true;
                        removeCallbacksAndMessages(null);
                        startVideo = false;
                    }
                    break;
                case SWIPE_VALUE:
                    isSwiped = true;
                    swipeDelay(msg.arg1, (Boolean) msg.obj);
                    break;
                case AUTO_RANDOM_PLAY:
                    dispatchGesture(true,"小视频");
                    isSwiped = true;
                    if (EasySP.init(MeetAndroidApplication.getInstance()).getBoolean(Consts.AUTO_PLAY,false)) {
                        postDelayed(autoSwipeRunable,4000);
                    } else {
                        startVideo = false;
                    }
                    break;
                case AUTO_READ_NEWS_SDHZ:
                    AccessibilityNodeInfo backBT = findViewByViewIdNoClick("c.l.a:id/back");
                    if (backBT != null) {
                        performViewClick(backBT);
                    }
                    newsIndex++;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (isButtonCheckedByText("首页")) {
                                startReadNews();
                            } else {
                                startVideo = false;
                            }
                        }
                    },2500);
                    break;
            }
            System.gc();
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        CharSequence pkg = event.getPackageName();
        Log.i(TAG, "event pkg:" + pkg+" event type:" + event.getEventType());
        if (TextUtils.isEmpty(pkg) || startVideo) return;
        if (pkg.equals("com.ps.accessservicedemo")) return;
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
//                autoForXiaozhuo();
                break;
            case DDQW:
//                autoForDDQW();
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
                        followAndPraise(Consts.sb_follow,Consts.sb_praise);
                    }
                }
                break;
            case QK_PACKAGE_NAME:
                Log.e(TAG, "QK_PACKAGE_NAME isSwiped -- "+isSwiped+" hasMessages -- "+handler.hasMessages(QK_PACKAGE_NAME_VALUE));
                if (!startVideo && isButtonCheckedByText("小视频")) {
                    startVideo = true;
                    swipeDelay(QK_PACKAGE_NAME_VALUE);
                }
                break;
            case HSJS:
//                AccessibilityNodeInfo bt = findViewByRealTextInList("领100金币");
//                if (bt != null) {
//                    bt.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                }
                break;
            case SDHZ:
                if (!startVideo && isButtonCheckedByText("首页")) {
                    startVideo = true;
                    startReadNews();
                }
                break;
                default:
                    autoSwipeSmallVideo();
                    break;
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(broadcastReceiver , intentFilter);
    }

    @Override
    public void onServiceDisconnected() {
        Log.e(TAG, "onServiceDisconnected");
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }

    int newsIndex = 0;
    int newsCount = 0;
    AccessibilityNodeInfo recyclerView = null;
    private void startReadNews() {
        List<AccessibilityNodeInfo> recyclerViewList = findViewListByViewId("c.l.a:id/recyvlerview");
        if (recyclerViewList != null && recyclerViewList.size() > 1) {
            recyclerView = recyclerViewList.get(0);
        }
        if (recyclerView != null) {
            newsCount = recyclerView.getChildCount();
            Log.e(TAG, "newsIndex : "+newsIndex+" newsCount : "+newsCount);
            if (newsCount > 0 && newsIndex < newsCount) {
                AccessibilityNodeInfo itemView = recyclerView.getChild(newsIndex);
                Log.e(TAG, "itemView : "+itemView);
                if (itemView != null) {
                    AccessibilityNodeInfo adItem = findViewByViewId(itemView, "c.l.a:id/action");
                    if (adItem == null) {
                        adItem = findViewByViewId(itemView, "c.l.a:id/native_ad_container");
                    }
                    if (adItem == null) {
                        adItem = findViewByViewId(itemView, "c.l.a:id/video");
                    }
                    if (adItem == null) {
                        boolean success = itemView.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.e(TAG, "click success : "+success);
                        if (success) {
                            if (defaultTime == 0) {
                                defaultTime = EasySP.init(MeetAndroidApplication.getInstance()).getInt(Consts.TIME_SET,18);
                            }
                            int randomTime = getRandomNum(defaultTime, defaultTime+10);
                            handler.sendEmptyMessageDelayed(AUTO_READ_NEWS_SDHZ,randomTime*1000);
                        } else {
                            handler.sendEmptyMessageDelayed(AUTO_READ_NEWS_SDHZ,1*1000);
                        }
                    } else {
                        handler.sendEmptyMessageDelayed(AUTO_READ_NEWS_SDHZ,1*1000);
                    }
                }
            } else {
                dispatchGesture(true,"资讯");
                newsIndex = -1;
                newsCount = 0;
                handler.sendEmptyMessageDelayed(AUTO_READ_NEWS_SDHZ,2500);
            }
        }
    }

    private void autoSwipeSmallVideo() {
        if (!startVideo && EasySP.init(MeetAndroidApplication.getInstance()).getBoolean(Consts.AUTO_PLAY,false)) {
            startVideo = true;
            swipeDelay(AUTO_RANDOM_PLAY);
        }
    }

    private void followAndPraise(String followId,String praiseId) {
        handler.removeCallbacks(followBtRunable);
        handler.removeCallbacks(praiseBtRunable);
        int randomFollow = getRandomNum(0,10);
        if (randomFollow > 6) {
            followBtRunable.id = followId;
            int actionDelay = getRandomNum(2, 9);
            Log.e(TAG,"randomFollow == "+randomFollow+" actionDelay == "+actionDelay);
            handler.postDelayed(followBtRunable,actionDelay*1000);
        }
        int randomPraise = getRandomNum(0,10);
        if (randomPraise > 4) {
            praiseBtRunable.id = praiseId;
            int actionDelay = getRandomNum(2, 9);
            Log.e(TAG,"randomPraise == "+randomPraise+" actionDelay == "+actionDelay);
            handler.postDelayed(praiseBtRunable,actionDelay*1000);
        }
    }

    private void swipeDelay(int what,boolean exactly) {
        if (isSwiped) {
            isSwiped = false;
            handler.removeCallbacksAndMessages(null);
            if (defaultTime == 0) {
                defaultTime = EasySP.init(MeetAndroidApplication.getInstance()).getInt(Consts.TIME_SET,18);
            }
            int randomTime = defaultTime;
            if (!exactly){
                randomTime = getRandomNum(defaultTime, defaultTime+5);
            }
            Log.e(TAG, "what: "+what+" randomTime: "+ randomTime);
            if (hasAd(what)) {
                handler.sendEmptyMessage(what);
            } else {
                handler.sendEmptyMessageDelayed(what,randomTime*1000);
            }
        }
    }

    private void swipeDelay(int what) {
        swipeDelay(what,false);
    }

    public boolean hasAd(int what) {
        AccessibilityNodeInfo ad = null;
//        String nowPkg = ForegroundAppUtil.getForegroundActivityName(MeetAndroidApplication.getInstance());
//        switch (nowPkg) {
//            case QK_PACKAGE_NAME:
//                ad = findViewByViewIdNoClick("com.jifen.qukan:id/af8");
//                if (ad != null) {
//                    return true;
//                }
//                break;
//            case XYZQ:
//                ad = findViewByViewIdNoClick("com.xiaoyuzhuanqian:id/title");
//                if (ad != null) {
//                    CharSequence title = ad.getText();
//                    if (TextUtils.isEmpty(title)) return false;
//                    if (title.toString().contains("广告")) {
//                        return true;
//                    }
//                }
//                break;
//            case HBSP:
//                ad = findViewByViewIdNoClick("com.sanmiao.sound:id/tt_video_ad_close_layout");
//                if (ad != null && ad.isClickable()) {
//                    ad.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    return false;
//                }
//                ad = findViewByViewIdNoClick("com.sanmiao.sound:id/ad_container");
//                if (ad != null) {
//                    return true;
//                }
//                break;
//        }
        ad = findViewByRealText("广告");
        if (ad != null) return true;
        ad = findViewByViewIdNoClick("com.coohua.xinwenzhuan:id/mini_video_popup");
        if (ad != null) return true;
        return false;
    }

    private void autoForXyzq() {
        if (!startVideo && isButtonCheckedByText("短视频")) {
            startVideo = true;
            swipeDelay(XYZQ_PACKAGE_NAME_VALUE,true);
        }
//        else if (isRefreshed && isButtonCheckedByText("任务赚")) {
//            handler.removeCallbacksAndMessages(null);
//            isRefreshed = false;
//            startVideo = false;
//            AccessibilityNodeInfo task_list_rl = findViewByViewIdNoClick("com.xiaoyuzhuanqian:id/task_list_rl");
//            if (task_list_rl != null && task_list_rl.getChildCount() > 0) {
//                Log.i(TAG, "task_list_rl :" + task_list_rl.toString());
//                Log.i(TAG, "count  :" + task_list_rl.getChildCount());
//                int count = task_list_rl.getChildCount();
//                for (int i = 0; i < count; i++) {
//                    AccessibilityNodeInfo item = task_list_rl.getChild(i);
//                    if (item != null) {
//                        Log.i(TAG, "item :" + item.toString() + " item count  :" + item.getChildCount());
//                        AccessibilityNodeInfo appStoreNameNode = findViewByViewId(item, "com.xiaoyuzhuanqian:id/appstore_name");
//                        if (appStoreNameNode != null) {
//                            CharSequence appStoreName = appStoreNameNode.getText();
//                            if (TextUtils.isEmpty(appStoreName)) return;
//                            Log.i(TAG, "list appStoreName :" + appStoreName);
////                            if (appStoreName.toString().contains("华为")) continue;
//                            boolean success = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            if (success) break;
//                        }
//                    }
//                }
//                handler.postDelayed(() -> {
//                    AccessibilityNodeInfo begin_get_money_btn = findViewByViewIdNoClick("com.xiaoyuzhuanqian:id/begin_get_money_btn");
//                    if (begin_get_money_btn != null) {
//                        boolean success = begin_get_money_btn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    }
//                }, 2 * 1000);
//            } else {
//                handler.postDelayed(() -> {
//                    dispatchGesture(false,"任务赚");
//                    isRefreshed = true;
//                }, 28 * 1000);
//            }
//        }
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
//                    delaySecond(16);
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
//                    try {
//                        //Thread.sleep(1*1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    AccessibilityNodeInfo okIKnow = findViewByViewIdNoClick("com.xzzq.xiaozhuo:id/task_guide_confirm_btn");
                    if (okIKnow != null) {
//                        try {
//                            //Thread.sleep(4*1000);
//                            boolean okIKnowOk = okIKnow.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
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
//                delaySecond(31);
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
//        try {
//            //Thread.sleep(count*1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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
//            try {
//                //Thread.sleep(1*1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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
//            try {
//                //Thread.sleep(3*60*1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            handler.postDelayed(() -> {
                performGlobalAction(GLOBAL_ACTION_BACK);
                performGlobalAction(GLOBAL_ACTION_BACK);
            },3*60*1000);

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

    @Override
    public void onInterrupt() {

    }
}
