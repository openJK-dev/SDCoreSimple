package net.ishandian.simple;

import android.app.Application;

import net.ishandian.sdcore.SDCore;

/**
 * 类名：
 *
 * @Author: huangdianhua on 2017/5/18 10:07.
 * E-mail: hdh.mr.666@gmail.com
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDCore.getInstance()
                .setBaseUrl("http://v5.ishandian.com.cn/shop/")
                .setDebug(true)
                .setContext(getApplicationContext());

    }
}
