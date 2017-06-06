package net.ishandian.sdcore.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.Toast;

import net.ishandian.sdcore.SDCore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {
    private static Toast toast;
    private static long serverTime = 0;
    private static long systime = 0;

    /**
     * 获得设备的品牌
     *
     * @return
     */
    public static String getPhoneBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 设置服务器时间
     */
    public static void setServerTime(final long time) {
        systime = System.currentTimeMillis();
        serverTime = time;
    }

    /**
     * 获取服务器时间
     *
     * @return 秒数
     */
    public static long getServerTime() {
        long second = (System.currentTimeMillis() - systime);
        if (serverTime == 0) {
            return System.currentTimeMillis() / 1000;
        }
        return serverTime + (second / 1000);
    }

    /**
     * 显示提示框
     *
     * @param string
     */
    public static void showToast(String string) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(SDCore.getInstance().getContext(), string, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 将秒转换成规定的日期（"yyyy-MM-dd HH:mm:ss"）
     *
     * @param second
     * @return
     */
    public static String secondToDateString(long second, String format) {
        try {
            if ("".equals(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(new Date(second * 1000L));
        } catch (Exception e) {
            return "1970.1.1";
        }
    }

    /**
     * 将毫秒转换成规定的日期（"yyyy-MM-dd HH:mm:ss"）
     *
     * @param second
     * @return
     */
    public static String mSecondToDateString(long second, String format) {
        try {
            if ("".equals(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(new Date(second));
        } catch (Exception e) {
            return "1970.1.1";
        }
    }

    /**
     * 验证生日格式
     */
    public static boolean isBirthdayNo(String birthday) {
        String data = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])" +
                "(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))|((" +
                "([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";
        if (TextUtils.isEmpty(birthday))
            return false;
        else
            return birthday.matches(data);

    }

    /**
     * 判断字符串是否包含特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isContains(String str) {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static boolean getIsDebug() {
        return SDCore.getInstance().isDebug();
    }

    /**
     * 将Unicode码转换成汉字
     *
     * @param str
     * @return
     */
    public static String UnicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");

        }
        return str;
    }

    /**
     * 调试信息打印
     *
     * @param tag
     * @param msg
     */
    public static void debug(String tag, String msg) {
        if (getIsDebug()) {
            Log.d(tag, msg + " ");
        }
    }

    /**
     * 判断字符串是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }

    /**
     * 保存本地数据信息
     *
     * @param key
     * @param context
     */
    public static void saveSharedPrefrences(String name, String key, String context) {
        SharedPreferences sp = SDCore.getInstance().getContext().getSharedPreferences(name, Context
                .MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, context);
        editor.commit();
    }

    /**
     * 获取本地保存的数据
     *
     * @param key
     * @return
     */
    public static String getSharedPrefrences(String name, String key) {
        SharedPreferences sp = SDCore.getInstance().getContext().getSharedPreferences(name, Context
                .MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        if (TextUtils.isEmpty(key)) {
            return "";
        } else {
            return sp.getString(key, "");
        }
    }

    /**
     * 清理本地保存数据
     *
     * @param key
     */
    public static void removeSharedPrefrences(String name, String key) {
        SharedPreferences sp = SDCore.getInstance().getContext().getSharedPreferences(name, Context
                .MODE_PRIVATE);
        // 存入数据
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 将MAC地址转变为字符串
     */
    public static String transMac(String address) {
        if (TextUtils.isEmpty(address)) {
            return null;
        } else if (address.contains(":")) {
            address = address.replace(":", "");
        } else {
            String[] macs = new String[6];
            for (int i = 0; i <= 5; i++) {
                macs[i] = address.substring(i * 2, i * 2 + 2);
            }
            address = macs[0];
            for (int i = 1; i < macs.length; i++) {
                address += ":" + macs[i];
            }
        }
        return address;
    }

    /**
     * utf8 字符转换
     *
     * @param str
     * @return
     */
    public static String utf8Encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) SDCore.getInstance().getContext().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            // 只判断是否链接
            // cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 获取凌晨零点时间戳
     *
     * @param time
     * @return
     */
    public static long getAm0Bytime(final long time) {
        long sec = time % 86400;
        return time - sec - 28800;
    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getSysHourMin() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][43587]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 获取千分位记说
     *
     * @return
     */
    public static String geThousands(final String number) {
        double doule = Double.valueOf(number);
        DecimalFormat format = new DecimalFormat("###,##0.00");
        return format.format(doule);
    }

    /**
     * 像素转换
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, int px) {
        context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 保留2位小数
     *
     * @param num
     * @return
     */
    public static String DecimalFormat(double num) {
        String newnum;
        DecimalFormat df = new DecimalFormat("######0.00");
        newnum = df.format(num);
        return newnum;
    }

    /**
     * 保留1位小数
     *
     * @param num
     * @return
     */
    public static String Decimal2Format(double num) {
        String newnum;
        DecimalFormat df = new DecimalFormat("######0.0");
        newnum = df.format(num);
        return newnum;
    }

    /**
     * 金额输入框中的内容限制（最大：小数点前五位，小数点后2位）
     *
     * @param edt
     */
    public static void judgeNumber(EditText edt) {

        String text = edt.getText().toString();
        if (text.contains(".")) {
            int index = text.indexOf(".");
            if (index + 3 < text.length()) {
                text = text.substring(0, index + 3);
                edt.setText(text);
                edt.setSelection(text.length());
            }
        }
    }

    /**
     * 数出Oid
     *
     * @param oid
     * @return
     */
    public static String outPutOid(String oid) {
        String regex = "(.{4})";
        String input = oid.replaceAll(regex, "$1 ");
        return input;
    }

}
