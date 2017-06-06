package net.ishandian.sdcore.http;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import net.ishandian.sdcore.R;
import net.ishandian.sdcore.SDCore;
import net.ishandian.sdcore.utils.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求返回Json数据回调
 *
 * @Author: huangdianhua on 2017/4/27 18:39.
 * E-mail: hdh.mr.666@gmail.com
 */

public abstract class JsonCallBack implements Callback<ResponseBody> {
    private String message;
    private Context mContext = SDCore.getInstance().getContext();
    private boolean isShowLoad;
    private ProgressDialog progressDialog;
    private boolean showToast;

    public void setProgressDialog(ProgressDialog progressDialog, boolean isShowLoad, boolean showToast) {
        this.progressDialog = progressDialog;
        this.isShowLoad = isShowLoad;
        this.showToast = showToast;
        message = mContext.getString(R.string.err_back_data);
    }

    /**
     * 消除弹框
     */
    private void setDimissDialog() {
        if (isShowLoad && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            if (response.isSuccessful()) {//请求成功，返回json数据
                String data = response.body().string();
                JSONObject baseData = new JSONObject(data);
                if (baseData.has("error")) {
                    JSONObject errObj = baseData.getJSONObject("error");
                    if (errObj.has("message")) {
                        message = errObj.getString("message");
                    }
                    onError(message);
                    if (mContext != null && showToast) {
                        CommonUtil.showToast(message + "");
                    }
                } else {
                    onSuccess(baseData);
                }

            } else {//请求失败，返回获取的错误码，和信息
                String ms = response.message();
                onError(ms);
                if (mContext != null && showToast) {
                    CommonUtil.showToast(ms + "");
                }
            }
        } catch (JSONException e) {//json异常
            onError(mContext.getString(R.string.tip_json_err));
            if (mContext != null && showToast) {
                CommonUtil.showToast(mContext.getString(R.string.tip_json_err));
            }
        } catch (IOException e) {//IO异常
            onError(mContext.getString(R.string.tip_io_err));
            if (mContext != null && showToast) {
                CommonUtil.showToast(mContext.getString(R.string.tip_io_err));
            }
        } catch (NullPointerException e) {
            onError(mContext.getString(R.string.tip_return_data_null));
            if (mContext != null && showToast) {
                CommonUtil.showToast(mContext.getString(R.string.tip_return_data_null));
            }
        }
        setDimissDialog();
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        onError(t.getMessage().toString());
        if (t != null && t.getMessage() != null && mContext != null && showToast) {
            CommonUtil.showToast(t.getMessage().toString() + "");
        }
        setDimissDialog();
    }

    /**
     * 成功回调
     *
     * @param dataInfo
     */
    public abstract void onSuccess(JSONObject dataInfo);

    /**
     * 错误回调
     *
     * @param message
     */
    public abstract void onError(String message);
}
