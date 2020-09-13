package com.xiaoxintech.xiaoxinplayer.Utils;

import com.google.gson.JsonObject;
import com.xiaoxintech.xiaoxinplayer.Data.UserInfo;
import okhttp3.*;

import java.io.IOException;


public class HttpUtils {
    private final OkHttpClient okHttpClient = new OkHttpClient();
//    private final String rootURL = "http://amani.cf:1024/";
    private final String rootURL = "http://192.168.123.238:8080/";
    private static HttpUtils instance;
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    public static HttpUtils getInstance() {
        if (instance == null) {
            instance = new HttpUtils();
        }
        return instance;
    }
    private String MakeRequest(Request request) {
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
    private String Get(String path) {
        Request request = new Request.Builder()
                .url(path)
                .get()          // 默认 GET 请求，可以不添加
                .build();
        return MakeRequest(request);
    }
    private String Post(String path, RequestBody body) {
        Request request = new Request.Builder().url(path).post(body).build();
        return MakeRequest(request);
    }
    public String GetFileList(String mUrl) {
        mUrl = rootURL + mUrl;
        return Get(mUrl);
    }
    public String Register(UserInfo userInfo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", userInfo.getEmail());
        jsonObject.addProperty("password", userInfo.getPassword());
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        String url = rootURL + "v1/users";
        return Post(url, body);
    }
    public String GetRootURL(){
        return rootURL;
    }
}
