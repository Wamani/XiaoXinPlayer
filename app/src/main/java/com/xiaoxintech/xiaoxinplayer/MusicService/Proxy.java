package com.xiaoxintech.xiaoxinplayer.MusicService;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.xiaoxintech.xiaoxinplayer.Activity.MainActivity;

import java.io.File;

public class Proxy extends Application {

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        Proxy myApplication = (Proxy) context.getApplicationContext();
        return myApplication.proxy == null ? (myApplication.proxy = myApplication.newProxy()) : myApplication.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        File file = new File(MainActivity.context.getExternalFilesDir("music"), "audio-cache");
        return new HttpProxyCacheServer.Builder(this)
                .cacheDirectory(file).maxCacheFilesCount(100).maxCacheSize(1024*1024*1024)
                .fileNameGenerator(new MyFileNameGenerator()).build();
    }

    public class MyFileNameGenerator implements FileNameGenerator {
        // Urls contain mutable parts (parameter 'sessionToken') and stable video's id (parameter 'videoId').
        // e. g. http://example.com?guid=abcqaz&sessionToken=xyz987
        public String generate(String url) {
            Uri uri = Uri.parse(url);
            String musicName = uri.getQueryParameter("path");
            String[] values = musicName.split("/");
            return values[values.length - 1];
        }
    }
}
