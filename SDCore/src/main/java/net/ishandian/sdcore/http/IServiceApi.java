package net.ishandian.sdcore.http;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 网络请求API
 *
 * @Author: huangdianhua on 2017/4/26 16:09.
 * E-mail: hdh.mr.666@gmail.com
 */

public interface IServiceApi {

    /**
     * get请求
     *
     * @param urlMethod
     * @param getPrams
     * @return
     */
    @GET("{urlMethod}?")
    Call<ResponseBody> getRetrofit(@Path("urlMethod") String urlMethod, @QueryMap Map<String, String> getPrams);

    /**
     * post请求
     *
     * @param urlMethod
     * @param getPrams
     * @param postPrams
     * @return
     */
    @FormUrlEncoded
    @POST("{urlMethod}?")
    Call<ResponseBody> postRetrofit(@Path("urlMethod") String urlMethod, @QueryMap Map<String, String> getPrams, @FieldMap Map<String, String> postPrams);

    /**
     * 文件下载
     *
     * @param url
     * @return
     */
    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    /**
     * 文件上传
     *
     * @param files
     * @return
     */
    @Multipart
    @POST("{urlMethod}?")
    Call<ResponseBody> uploadFiles(@Path("urlMethod") String urlMethod, @PartMap Map<String, RequestBody> files);

}
