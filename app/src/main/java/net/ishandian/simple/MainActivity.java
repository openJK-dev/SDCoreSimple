package net.ishandian.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.ishandian.sdcore.SDCore;
import net.ishandian.sdcore.http.FileCallBack;
import net.ishandian.sdcore.http.JsonCallBack;
import net.ishandian.sdcore.http.RetrofitService;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button post, download, get, upload;
    private TextView tv;
    //private String downloadUrl = "http://www.jcodecraeer.com/plus/download.php?open=3&id=7875&uhash=ac2ce34b736742fb9fb737e2";
    private String downloadUrl = "http://img.ishandian.com.cn/upload/packages/net.ishandian.app.shophd.shandian.283.26058.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        post = (Button) findViewById(R.id.post);
        download = (Button) findViewById(R.id.download);
        get = (Button) findViewById(R.id.get);
        tv = (TextView) findViewById(R.id.tv);
        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(this);
        post.setOnClickListener(this);
        download.setOnClickListener(this);
        get.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post:
                postRequest();
                break;
            case R.id.download:
                downloadRequest();
                break;
            case R.id.get:
                getRequest();
                break;
            case R.id.upload:
                uploadRequest();
                break;
        }
    }

    /**
     * post请求
     */
    private void postRequest() {
        final HashMap<String, String> prams = new HashMap<>();
        prams.put("mobile", "15751164916");
        prams.put("password", "1234567");
        prams.put("isBoss", "0");
        HashMap<String, String> defaultPrams = new HashMap<String, String>();
        defaultPrams.put("format", "json");
        RetrofitService.newBuilder(MainActivity.this)
                .showLoading(true)//是否显示加载框（默认显示）
                .loadType(RetrofitService.LOADTYPE.CIRCLE)//加载框类型（圆形和水平进度条）
                .baseUrl(SDCore.getInstance().getBaseUrl())//设置baseUrl（默认为SDCore中设置的url）
                .connectTimeout(10) //
                .readTimeout(10)//
                .writeTimeout(10)//
                .showToast(true)//是否显示错误提示（默认显示）
                .build()
                .postRequest(new JsonCallBack() {
                    @Override
                    public void onSuccess(JSONObject dataInfo) {
                        tv.setText(dataInfo.toString());
                    }

                    @Override
                    public void onError(String message) {

                    }
                }, "login/login", defaultPrams, prams);
    }

    /**
     * GET请求
     */
    private void getRequest() {
        HashMap<String, String> map = new HashMap<>();
        RetrofitService.newBuilder(MainActivity.this)
                .baseUrl("https://api.douban.com/v2/book/")
                .build()
                .getRequest(new JsonCallBack() {
                    @Override
                    public void onSuccess(JSONObject dataInfo) {
                        if (dataInfo != null) {
                            tv.setText(dataInfo.toString());
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                }, "6548683", map);
    }

    /**
     * 文件下载
     */
    private void downloadRequest() {
        RetrofitService.newBuilder(MainActivity.this)
                .showLoading(true)
                .loadType(RetrofitService.LOADTYPE.HORIZONTAL)
                .build()
                .downloadFile(new FileCallBack() {
                    @Override
                    public void onProcessUpdate(int value) {
                        Log.d("1--", value + "");
                    }

                    @Override
                    public void onSuccess() {
                        Log.d("1--", "success");
                    }

                    @Override
                    public void onError() {
                        Log.d("1--", "error");
                    }
                }, downloadUrl, "/sdcard/sdcore/", "abc.apk");
    }

    /**
     * 上传文件
     */
    private void uploadRequest() {
        ArrayList<File> files = new ArrayList<File>();
        files.add(new File("/sdcard/123.png"));
        RetrofitService.newBuilder(MainActivity.this)
                .build()
                .uploadFiles(new JsonCallBack() {
                    @Override
                    public void onSuccess(JSONObject dataInfo) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                }, "upload",files);
    }
}
