package net.ishandian.sdcore.http;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import net.ishandian.sdcore.R;
import net.ishandian.sdcore.SDCore;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * 封装retrofit网络请求
 *
 * @Author: huangdianhua on 2017/4/26 16:41.
 * E-mail: hdh.mr.666@gmail.com
 */

public class RetrofitService {
    private IServiceApi mIServiceApi;
    private OkHttpClient.Builder okBuilder;
    private OkHttpClient client;
    private Retrofit.Builder reBuilder;
    private Retrofit retrofit;
    private Context mContext;
    private Activity mActivity;
    private boolean isShowLoad;//是否显示对话框
    private LOADTYPE loadtype;//对话框类型
    private boolean showToast;
    private static ProgressDialog progressDialog;

    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多
    // 线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还
    // 没赋值完时，被另外线程调用)
    private static volatile RetrofitService mRetrofitService;

    private RetrofitService() {
        okBuilder = new OkHttpClient().newBuilder();
        if (SDCore.getInstance().isDebug())
            okBuilder.addNetworkInterceptor(new HttpLogInterceptor());
        reBuilder = new Retrofit.Builder();
    }

    private static RetrofitService getInstance(Builder builder) {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (mRetrofitService == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (RetrofitService.class) {
                if (mRetrofitService == null) {
                    mRetrofitService = new RetrofitService();
                }
            }
        }
        mRetrofitService.initRetrofit(builder);
        return mRetrofitService;
    }

    /**
     * 初始化Retrofit
     *
     * @param builder
     */
    private void initRetrofit(Builder builder) {
        isShowLoad = builder.isShowLoad;
        loadtype = builder.loadType;
        showToast = builder.showToast;
        client = okBuilder.connectTimeout(builder.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(builder.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(builder.writeTimeout, TimeUnit.SECONDS).build();
        retrofit = reBuilder.baseUrl(builder.baseUrl)
                .client(client)
                .build();
        mIServiceApi = retrofit.create(builder.serviceApi);
        try {
            mContext = builder.mContext;
            mActivity = (Activity) mContext;
        } catch (Exception e) {
            mContext = builder.mContext;
        }
        initDialog();
    }

    /**
     * 初始化弹框
     */
    private void initDialog() {
        if (mActivity != null) {
            progressDialog = new ProgressDialog(mActivity);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMax(100);
            if (loadtype == LOADTYPE.CIRCLE) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage(mContext.getString(R.string.loading_data));
            } else {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage(mContext.getString(R.string.download_data));
            }
        }
    }

    /**
     * 显示弹框
     */
    private void showDialog() {
        if (isShowLoad && progressDialog != null && mActivity != null && !mActivity.isDestroyed()) {
            progressDialog.show();
        }
    }

    /**
     * POST 请求
     *
     * @param callBack      回调
     * @param urlMethod     接口名
     * @param getParameter  get参数
     * @param postParameter post参数
     */
    public void postRequest(JsonCallBack callBack, String urlMethod, Map<String, String> getParameter, Map<String, String> postParameter) {
        if (getParameter == null) {
            throw new NullPointerException(mContext.getString(R.string.get_param_no));

        }
        if (postParameter == null) {
            throw new NullPointerException(mContext.getString(R.string.post_param_no));
        }
        if (urlMethod == null) {
            throw new NullPointerException(mContext.getString(R.string.urlmethod_no));
        }
        if (mIServiceApi != null) {
            showDialog();
            callBack.setProgressDialog(progressDialog, isShowLoad, showToast);
            Call<ResponseBody> call = mIServiceApi.postRetrofit(urlMethod, getParameter, postParameter);
            call.enqueue(callBack);
        }
    }

    /**
     * GET 请求
     *
     * @param callBack  结果回调
     * @param urlMethod 接口名
     * @param parameter get参数
     */
    public void getRequest(JsonCallBack callBack, String urlMethod, Map<String, String> parameter) {
        if (parameter == null) {
            throw new NullPointerException(mContext.getString(R.string.get_param_no));
        }
        if (urlMethod == null) {
            throw new NullPointerException(mContext.getString(R.string.urlmethod_no));
        }
        if (mIServiceApi != null) {
            showDialog();
            callBack.setProgressDialog(progressDialog, isShowLoad, showToast);
            Call<ResponseBody> call = mIServiceApi.getRetrofit(urlMethod, parameter);
            call.enqueue(callBack);
        }
    }

    /**
     * 下载文件
     *
     * @param callBack 结果回调
     * @param url      请求地址
     */
    public void downloadFile(FileCallBack callBack, String url, String filePath, String fileName) {
        if (mIServiceApi != null) {
            showDialog();
            callBack.setProgressDialog(progressDialog, isShowLoad, showToast, filePath, fileName);
            Call<ResponseBody> call = mIServiceApi.downloadFile(url);
            call.enqueue(callBack);
        }
    }

    /**
     * 上传文件
     *
     * @param callBack  结果回调
     * @param urlMethod 接口名
     * @param files     上传的文件
     */
    public void uploadFiles(JsonCallBack callBack, String urlMethod, List<File> files) {
        if (mIServiceApi != null) {
            showDialog();
            callBack.setProgressDialog(progressDialog, isShowLoad, showToast);
            Call<ResponseBody> call = mIServiceApi.uploadFiles(urlMethod, filesToMultiRequestBodys(files));
            call.enqueue(callBack);
        }
    }

    /**
     * 将file转化成RequestBody
     *
     * @param files
     * @return
     */
    private Map<String, RequestBody> filesToMultiRequestBodys(List<File> files) {
        Map<String, RequestBody> requestBodys = new HashMap<>();
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            requestBodys.put("file" + i + "\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        return requestBodys;
    }

    /**
     * 创建一个配置对象
     *
     * @param context
     * @return
     */
    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    /**
     * Retrofit请求配置类
     */
    public static final class Builder {
        int connectTimeout;
        int readTimeout;
        int writeTimeout;
        boolean isShowLoad;
        boolean showToast;
        String baseUrl;
        LOADTYPE loadType;
        Class<IServiceApi> serviceApi;
        Context mContext;

        public Builder(Context context) {
            connectTimeout = 30;
            readTimeout = 60;
            writeTimeout = 60;
            isShowLoad = true;
            baseUrl = SDCore.getInstance().getBaseUrl();//服务器地址
            loadType = LOADTYPE.CIRCLE;
            serviceApi = IServiceApi.class;
            mContext = context;
            showToast = true;
        }

        /**
         * 连接超时时间
         *
         * @param timeout
         * @return
         */
        public Builder connectTimeout(long timeout) {
            connectTimeout = checkDuration("timeout", timeout);
            return this;
        }

        /**
         * 读取超时时间
         *
         * @param timeout
         * @return
         */
        public Builder readTimeout(long timeout) {
            readTimeout = checkDuration("timeout", timeout);
            return this;
        }

        /**
         * 写入超时时间
         *
         * @param timeout
         * @return
         */
        public Builder writeTimeout(long timeout) {
            writeTimeout = checkDuration("timeout", timeout);
            return this;
        }

        /**
         * 是否显示进度框
         *
         * @param showLoad
         * @return
         */
        public Builder showLoading(boolean showLoad) {
            isShowLoad = showLoad;
            return this;
        }

        /**
         * 进度框类型
         *
         * @param loadType
         * @return
         */
        public Builder loadType(LOADTYPE loadType) {
            this.loadType = loadType;
            return this;
        }

        /**
         * 基础URL
         *
         * @param baseUrl
         * @return
         */
        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 请求的接口
         *
         * @param serviceApi
         * @return
         */
        public Builder serviceApi(Class serviceApi) {
            this.serviceApi = serviceApi;
            return this;
        }

        /**
         * 是否显示提示
         *
         * @param showToast
         * @return
         */
        public Builder showToast(boolean showToast) {
            this.showToast = showToast;
            return this;
        }

        /**
         * 判断时间设置是否合法
         *
         * @param name
         * @param duration
         * @return
         */
        private int checkDuration(String name, long duration) {
            if (duration < 0) throw new IllegalArgumentException(name + " < 0");
            if (duration > Integer.MAX_VALUE)
                throw new IllegalArgumentException(name + " too large.");
            if (duration == 0) throw new IllegalArgumentException(name + " too small.");
            return (int) duration;
        }

        public RetrofitService build() {
            return getInstance(this);
        }
    }

    /**
     * 加载框的类型
     */
    public enum LOADTYPE {
        CIRCLE, HORIZONTAL//(圆形，水平进度条)
    }
}
