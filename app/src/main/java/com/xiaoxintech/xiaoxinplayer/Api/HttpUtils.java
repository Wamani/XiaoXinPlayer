package com.xiaoxintech.xiaoxinplayer.Api;

import okhttp3.*;

import java.io.IOException;

public class HttpUtils {
    private static final OkHttpClient okHttpClient = new OkHttpClient();
//    private static String rootURL = "http://10.86.92.81:8080/";
//
    private static String rootURL = "http://amani.cf:1024/";
    public static String GetFileList(String mUrl) {
        mUrl = rootURL + mUrl;
        Request request = new Request.Builder()
                .url(mUrl)
                .get()          // 默认 GET 请求，可以不添加
                .build();

        // 同步 execute
        final Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            ResponseBody body = response.body();
            String res = "";
            if (body != null) {
                // 处理从服务器获取到的信息
                res += body.string();
            }
            response.close();
            return res;
        } catch (IOException e) {
            // 处理错误信息
            return e.toString();
        }
    }

    public static String GetRootURL(){
        return rootURL;
    }
}
