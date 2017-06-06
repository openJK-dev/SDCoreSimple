package net.ishandian.sdcore.http;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * okhttp请求日志打印
 *
 * @Author: huangdianhua on 2017/4/27 16:53.
 * E-mail: hdh.mr.666@gmail.com
 */

public class HttpLogInterceptor implements Interceptor {
    public static final String TAG = "Http_Log";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //the request url
        String url = request.url().toString();
        //the request method
        String method = request.method();
        long t1 = System.nanoTime();
        StringBuilder sb = new StringBuilder("发送 请求 [url =");
        //CommonUtil.debug(TAG,String.format(Locale.getDefault(),"Sending %s request [url = %s]",method,url));
        //the request body
        RequestBody requestBody = request.body();
        sb.append(url + "&");
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            if (isPlaintext(buffer)) {
                sb.append(buffer.readString(charset));
                sb.append(" (Content-Type = ").append(contentType.toString()).append(",")
                        .append(requestBody.contentLength()).append("-byte body)");
            } else {
                sb.append(" (Content-Type = ").append(contentType.toString())
                        .append(",binary ").append(requestBody.contentLength()).append("-byte body omitted)");
            }
            sb.append("]");
            Log.d(TAG, String.format(Locale.getDefault(), "%s %s", method, sb.toString()));
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        //the response time
        Log.d(TAG, String.format(Locale.getDefault(), "收到 回复 for [url = %s] in %.1fms", url, (t2 - t1) / 1e6d));
        //the response state
        Log.d(TAG, String.format(Locale.CHINA, "收到 回复 is %s ,message[%s],code[%d]", response.isSuccessful() ? "success" : "fail", response.message(), response.code()));

        /*//the response data
        ResponseBody body = response.body();

        BufferedSource source = body.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = body.contentType();
        if (contentType != null) {
            charset = contentType.charset(charset);
        }
        String bodyString = buffer.clone().readString(charset);
        CommonUtil.debug(TAG,String.format("Received response json string [%s]", URLEncoder.encode(bodyString,"unicode")));*/
        return response;
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}
