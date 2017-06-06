package net.ishandian.sdcore.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import net.ishandian.sdcore.SDCore;
import net.ishandian.sdcore.utils.CommonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 类名：网络请求下载数据回调
 *
 * @Author: huangdianhua on 2017/5/25 13:35.
 * E-mail: hdh.mr.666@gmail.com
 */

public abstract class FileCallBack implements Callback<ResponseBody> {
    private String message;
    private Context mContext = SDCore.getInstance().getContext();
    private boolean isShowLoad;
    private ProgressDialog progressDialog;
    private boolean showToast;
    private String filePath;//文件路径
    private String fileName;//文件名称

    public void setProgressDialog(ProgressDialog progressDialog, boolean isShowLoad, boolean showToast, String filePath, String fileName) {
        this.progressDialog = progressDialog;
        this.isShowLoad = isShowLoad;
        this.showToast = showToast;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    /**
     * 消除弹框
     */
    private void setDimissDialog() {
        if (isShowLoad && progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 更新对话框进度条
     */
    private void updataDialog(int value) {
        progressDialog.setProgress(value);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {//请求成功
            new DownloadTask(response.body()).execute();
        } else {//请求失败，返回获取的错误码，和信息
            setDimissDialog();
            String ms = response.message();
            onError();
            if (mContext != null && showToast) {
                CommonUtil.showToast(ms + "");
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        onError();
    }

    /**
     * 下载进度
     *
     * @param value
     */
    public abstract void onProcessUpdate(int value);

    public abstract void onSuccess();

    public abstract void onError();

    /**
     * 异步读取文件
     */
    class DownloadTask extends AsyncTask<Void, Integer, Integer> {
        private ResponseBody body;

        protected DownloadTask(ResponseBody body) {
            this.body = body;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            boolean result = writeResponseBodyToFile(body);
            return result ? 1 : 0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            updataDialog(values[0]);
            onProcessUpdate(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                onSuccess();
            } else {
                onError();
            }
            setDimissDialog();
        }

        /**
         * 将下载文件写入到本地
         *
         * @param body
         */
        private boolean writeResponseBodyToFile(ResponseBody body) {
            boolean result;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File writeFile = new File(filePath + fileName);
                byte[] fileReader = new byte[128];
                double fileSize = body.contentLength();
                double fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(writeFile);
                int compareValue = 0;
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    int value = (int) (fileSizeDownloaded / fileSize * 100);
                    if (value != compareValue) {
                        compareValue = value;
                        publishProgress(value);
                    }
                }
                result = true;
                outputStream.flush();
            } catch (IOException e) {
                result = false;
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}
