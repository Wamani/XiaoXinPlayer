package com.xiaoxintech.xiaoxinplayer.MusicService;

import android.os.Looper;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxintech.xiaoxinplayer.Activity.MainActivity;
import com.xiaoxintech.xiaoxinplayer.Utils.HttpUtils;
import com.xiaoxintech.xiaoxinplayer.Data.Music;
import com.xiaoxintech.xiaoxinplayer.Data.ResResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlayEvent {
    public static List<Music> FileInfo = new ArrayList<>();
    private static List<Song> queue = new ArrayList<>();

    public static PlayEvent playEvent;
    public static PlayEvent getDefault() {
        if (playEvent == null) {
            synchronized (PlayEvent.class) {
                if (playEvent == null) {
                    playEvent =  new PlayEvent();
                }
                new Thread(runnable).start();
            }
        }
        return playEvent;
    }
    public enum Action {
        PLAY, STOP, RESUME, NEXT, PREVIOUS, SEEK, PlayTarget, INIT, MODE
    }

    private Action mAction;
    private Song mSong;
    private List<Song> mQueue;
    private int seekTo;
    private int queueIndex;
    private MusicPlayer.PlayMode mode = MusicPlayer.PlayMode.LOOP;
    public Song getSong() {
        return mSong;
    }

    public void setMode(MusicPlayer.PlayMode playMode) {
        mode = playMode;
    }
    public MusicPlayer.PlayMode getMode() {
        return mode;
    }
    public void setIndex(int index){
        queueIndex = index;
    }
    public int getIndex(){
        return queueIndex;
    }
    public void setSong(Song song) {
        mSong = song;
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public List<Song> getQueue() {
        return mQueue;
    }

    public void setQueue(List<Song> queue) {
        mQueue = queue;
    }

    public int getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(int seekTo) {
        this.seekTo = seekTo;
    }

    static Runnable runnable = new Runnable(){
        @Override
        public void run() {
            String res = HttpUtils.getInstance().GetFileList("list");
            if(res.contains("failed")){
                Looper.prepare();
                Toast.makeText(MainActivity.context, res, Toast.LENGTH_LONG).show();
                Looper.loop();// 进入loop中的循环，查看消息队列
                return;
            }
            // decode response
            Gson gson = new Gson();
            Type type = new TypeToken<ResResult<Music>>(){}.getType();
            ResResult<Music> response = gson.fromJson(res, type);
            FileInfo = response.file_infos;
            // set music list to music service

            for (Music music : FileInfo) {
                String musicUrl = getMusicUrl(music.getName());
                queue.add(getSong(musicUrl));
            }
            MusicPlayer.getDefault().setQueue(queue, 0);
        }
    };
    private static String getMusicUrl(String name) {
        return HttpUtils.getInstance().GetRootURL() + "file?path=" + name;
    }

    private static Song getSong(String url) {
        Song song = new Song();
        song.setPath(url);
        return song;
    }

}