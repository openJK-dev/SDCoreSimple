package net.ishandian.sdcore;

import android.content.Context;

import java.util.Map;

/**
 * 类名：SDCore
 * 使用：在使用的项目中的Applicition中初始化
 *
 * @Author: huangdianhua on 2017/5/17 11:30.
 * E-mail: hdh.mr.666@gmail.com
 */

public class SDCore {
    private String mBaseUrl;//服务器地址
    private boolean isDebug;
    private Context mContext;
    private static SDCore sdCore;

    private SDCore() {

    }

    public static SDCore getInstance() {
        if (sdCore == null) {
            sdCore = new SDCore();
        }
        return sdCore;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public SDCore setDebug(boolean debug) {
        isDebug = debug;
        return this;
    }

    public Context getContext() {
        return mContext;
    }

    public SDCore setContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    /**
     * 设置服务器地址
     *
     * @param baseUrl
     * @return
     */
    public SDCore setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;

    }

    /**
     * 返回服务器地址
     *
     * @return
     */
    public String getBaseUrl() {
        return mBaseUrl;
    }

}
