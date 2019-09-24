import random
import time
from threading import Timer

from appium import webdriver
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from appium.webdriver.common.touch_action import TouchAction

device_type = 9  # 1:f23 2:pad
isJm = False
phone_type = False

devices_info = []

def setup():
    desired_caps = {}
    desired_caps['platformName'] = 'Android'
    if device_type == 1:
        desired_caps['platformVersion'] = '7.1.2'
        desired_caps['deviceName'] = 'cf15de37'
    elif device_type == 2:
        desired_caps['platformVersion'] = '7.1.2'
        desired_caps['deviceName'] = 'cf15de37'
    elif device_type == 3:
        desired_caps['platformVersion'] = '6.0.1'
        desired_caps['deviceName'] = '11c99d4f7d03'
        desired_caps['noReset'] = True
    elif device_type == 4:
        desired_caps['platformVersion'] = '6.0.1'
        desired_caps['deviceName'] = '5b346332'
    elif device_type == 5:#mi4
        desired_caps['platformVersion'] = '6.0.1'
        desired_caps['deviceName'] = '192.168.1.110:5556'
        desired_caps['noReset'] = True
    elif device_type == 9:
        desired_caps['platformVersion'] = '5.0.2'
        desired_caps['deviceName'] = '1cea9a5'
        desired_caps['noReset'] = True
    if isJm:
        desired_caps['appPackage'] = 'com.jm.android.jumei'
        desired_caps['appActivity'] = '.home.activity.StartActivity'
    else:
        desired_caps['appPackage'] = 'com.jm.video'
        desired_caps['appActivity'] = '.ui.main.MainActivity'
    desired_caps['automationName'] = 'uiautomator2'
    return desired_caps


# 获得机器屏幕大小x,y
def getSize():
    x = dr.get_window_size()['width']
    y = dr.get_window_size()['height']
    return (x, y)


# 屏幕向上滑动
def swipeUp(t):
    try:
        l = getSize()
        random_x = random.randint(1, 9)
        random_x2 = random.randint(1, 9)
        random_y1 = random.randint(7, 9)
        random_y2 = random.randint(1, 2)
        x1 = int(l[0] * random_x * 0.1)  # x坐标
        y1 = int(l[1] * random_y1 * 0.1)  # 起始y坐标
        y2 = int(l[1] * random_y2 * 0.1)  # 终点y坐标
        x2 = int(l[0] * random_x2 * 0.1)
        # print('random_x : ' + str(random_x) + ' and random_y1 == ' + str(random_y1) + ' and random_y2 == ' + str(random_y2))
        # print('x1 : ' + str(x1) + ' and y1 == ' + str(y1) + ' and y2 == ' + str(y2)+" and swipe_time == "+str(t))
        dr.swipe(x1, y1, x2, y2, t)
    except (Exception) as e:
        print(e)


# 屏幕向下滑动
def swipeDown(t):
    l = getSize()
    x1 = int(l[0] * 0.5)  # x坐标
    y1 = int(l[1] * 0.25)  # 起始y坐标
    y2 = int(l[1] * 0.75)  # 终点y坐标
    dr.swipe(x1, y1, x1, y2, t)


# 屏幕向左滑动
def swipLeft(t):
    l = getSize()
    x1 = int(l[0] * 0.75)
    y1 = int(l[1] * 0.5)
    x2 = int(l[0] * 0.05)
    dr.swipe(x1, y1, x2, y1, t)


# 屏幕向右滑动
def swipRight(t):
    l = getSize()
    x1 = int(l[0] * 0.05)
    y1 = int(l[1] * 0.5)
    x2 = int(l[0] * 0.75)
    dr.swipe(x1, y1, x2, y1, t)


# 每隔两秒执行一次任务
def praise():
    try:
        l = getSize()
        random_x = random.randint(2, 7)
        random_y1 = random.randint(2, 7)
        x1 = int(l[0] * random_x * 0.1)  # x坐标
        y1 = int(l[1] * random_y1 * 0.1)  #
        random_x2 = random.randint(1, 20)
        random_y2 = random.randint(1, 20)
        x2 = x1+random_x2
        y2 = y1+random_y2
        print('praise --- x1 : ' + str(x1) + ' and y1 == ' + str(y1)+' x2 : ' + str(x2) + ' and y2 == ' + str(y2))
        TouchAction(dr).press(None, x1, y1).release().perform().wait(88).press(None, x2, y2).release().perform()
    except (Exception) as e:
        print(e)


def delayPraise(second):
    time.sleep(second)
    praise()


def always_allow(driver, number=3):
    '''
    args:1.传driver
    2.number，判断弹窗次数，默认给5次
    其它：
    WebDriverWait里面0.5s判断一次是否有弹窗，1s超时
    '''
    for i in range(number):
        loc = ("xpath", "//*[@text='始终允许']")
        try:
            e = WebDriverWait(driver, 1, 0.5).until(EC.presence_of_element_located(loc))
            e.click()
        except:
            pass


# 验证使用press_Keycode方法来输入银行卡号
def press_Keycode(stringInput):
    # 将银行卡号字符串转化成字符数组
    input_str = (','.join(stringInput)).split(',')
    print('input_str方法press_Keycode:', input_str)
    # 通过模拟物理按键用for循环每次输入一个字符输入手机号
    for i in range(len(input_str)):
        # 用press_keycode方法模拟键盘逐个字符输入
        # print('int(input_str[' + str(i) + ']:', int(input_str[i]))
        dr.press_keycode(int(input_str[i]) + 7)
        # # 通过当前输入框内内容的长度来判断前端加空格截断后是否有多输入，有则删除多输入的
        # if len((element.text).replace(" ", "")) > i + 1:
        #     self.driver.press_keycode(67)
        #     # 保持焦点在输入框内且每次输入单个字符后，将光标置到最后
        #     if i == len(input_str) / 2:
        #         # print('len(input_str)/2:', len(input_str)/2)
        #         element.click()
        #         self.driver.press_keycode(123)


def autoLoginForJm():
    dr.find_element_by_android_uiautomator('text("去允许")').click()
    always_allow(dr)
    time.sleep(5)
    dr.find_element_by_id("com.jm.android.jumei:id/ftb_mine").click()
    time.sleep(1.5)
    dr.find_element_by_android_uiautomator('text("注册/登录")').click()
    time.sleep(1.5)
    press_Keycode("18208182052")
    time.sleep(1)
    dr.find_element_by_android_uiautomator('text("验证")').click()
    time.sleep(10)
    dr.find_element_by_android_uiautomator('text("注册/登录")').click()
    time.sleep(2)
    dr.find_element_by_id("com.jm.android.jumei:id/v_back").click()
    time.sleep(2)
    swipLeft(600)
    pass


# always_allow(dr)

def autologinForSB(is_phone_num):
    time.sleep(15)
    dr.find_element_by_id("com.jm.video:id/circleBar").click()
    time.sleep(1.5)
    if is_phone_num:
        press_Keycode("18208182052")
        time.sleep(1)
        dr.find_element_by_android_uiautomator('text("手机登录(无需注册)")').click()
        time.sleep(19)
    else:
        dr.find_element_by_android_uiautomator('text("微信账号登陆")').click()
        time.sleep(10)
    dr.find_element_by_id("com.jm.video:id/btn_back").click()
    time.sleep(1.5)
    dr.find_element_by_id("com.jm.video:id/imgClose").click()
    pass


if __name__ == '__main__':
    desired_caps = setup()
    dr = webdriver.Remote('http://localhost:4723/wd/hub', desired_caps)
    time.sleep(2)
    if isJm:
        autoLoginForJm()
        minSec = 13
        maxSec = 15
    else:
        # autologinForSB(phone_type)
        minSec = 40
        maxSec = 50
    minSecOffset = minSec - 10  # 5 #3
    maxSecOffset = maxSec - 10  # 18 #5
    for i in range(0, 666):
        random_time = random.randint(minSec, maxSec)

        praise_delaytime = random.randint(1, 10)
        print('index: ' + str(i) + '; time_delay: ' + str(random_time) + ' ; nowTime(' + time.strftime(
            "%Y-%m-%d %H:%M:%S", time.localtime()) + '); praise_time:' + str(praise_delaytime)+" ;device_type == "+str(device_type))
        if praise_delaytime > 7:
            # delayPraise(praise_delaytime)
            # random_time = random_time-praise_delaytime
            # print('index : ' + str(i) + ' and time_delay == ' + str(random_time) + ' and praise_delaytime == ' + str(praise_delaytime))
            time.sleep(random_time)
            # dr.find_element_by_id("com.jm.video:id/praise").click()
            # time.sleep(praise_delaytime)
        else:
            time.sleep(random_time)
        random_swipe_time = random.randint(600, 700)
        swipeUp(random_swipe_time)
    dr.quit()
    pass
# com.jm.video:id/attention
