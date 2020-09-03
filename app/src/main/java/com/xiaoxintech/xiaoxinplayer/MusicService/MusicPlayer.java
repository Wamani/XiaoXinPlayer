package com.xiaoxintech.xiaoxinplayer.MusicService;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import com.danikula.videocache.HttpProxyCacheServer;
import com.xiaoxintech.xiaoxinplayer.Activity.MainActivity;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentPlaying;
import wseemann.media.FFmpegMediaMetadataRetriever;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private List<Song> mQueue;
    private static int mQueueIndex;
    private PlayMode mPlayMode;
    private FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
    public enum PlayMode {
        LOOP, RANDOM, REPEAT
    }
    public static MusicPlayer musicPlayer;
    public static MusicPlayer getDefault() {
        if (musicPlayer == null) {
            synchronized (MusicPlayer.class) {
                if (musicPlayer == null) {
                    musicPlayer =  new MusicPlayer();
                }
            }
        }
        return musicPlayer;
    }
    private HttpProxyCacheServer proxy;

    public MusicPlayer() {
        mMediaPlayer = new ManagedMediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
        mQueue = new ArrayList<>();
        mQueueIndex = 0;
        mPlayMode = PlayMode.LOOP;
        proxy = Proxy.getProxy(MainActivity.context);
    }

    public void setQueue(List<Song> queue, int index) {
        mQueue = queue;
        mQueueIndex = index;
//        play(getNowPlaying());
    }
    public void playNow(){
        play(getNowPlaying());
    }
    public void play(Song song) {
        try {
            mMediaPlayer.reset();
            if(song.getPlayPath().equals("")){
                song.setPlayPath(proxy.getProxyUrl(song.getPath(), true));
            }
            mMediaPlayer.setDataSource(song.getPlayPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekPlayProgress();
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    System.out.println("error"+ what);
                    return false;
                }
            });
//          new Thread(runnable).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void resume() {
        mMediaPlayer.start();
    }

    public void next() {
        play(getNextSong());
    }

    public void previous() {
        play(getPreviousSong());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public Song getNowPlaying() {
        if (mQueue.isEmpty()) {
            return null;
        }
        return mQueue.get(mQueueIndex);
    }
    private Song getNextSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getNextIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }

    private Song getPreviousSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getPreviousIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }


    public int getCurrentPosition() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public int getOnlineDuration(){
        if (getNowPlaying() != null) {
            String path = getNowPlaying().getPath();
            int duration_tmp = 0;
            if(proxy.isCached(path)){
                duration_tmp = getDuration();
            } else {
                mmr.setDataSource(getNowPlaying().getPath());
                duration_tmp = Integer.parseInt(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION));
            }
            return duration_tmp;
        }
        return 0;
    }

    public String getStringDuration(int duration) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(duration);
    }
    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    private int getNextIndex() {
        mQueueIndex = (mQueueIndex + 1) % mQueue.size();
        return mQueueIndex;
    }

    private int getPreviousIndex() {
        if (mQueueIndex == 0) {
            mQueueIndex = mQueue.size();
        }
        mQueueIndex = (mQueueIndex - 1) % mQueue.size();
        return mQueueIndex;
    }

    private int getRandomIndex() {
        mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
        return mQueueIndex;
    }

    public int getCurrentSongIndex() {
        if(getNowPlaying() == null) {
            return 0;
        }
        return mQueueIndex;
    }

    private void release() {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void playTargetSong(int index) {
        mQueueIndex = index;
        play(mQueue.get(index));
    }

    public void seekTo(int positon) {
        mMediaPlayer.seekTo(positon);
    }

    public void seekPlayProgress(){
        //计时器对象
        final Timer timer=new Timer();
        final TimerTask task=new TimerTask() {
            @Override
            public void run() {
                //开启线程定时获取当前播放进度
                int currentPosition = getCurrentPosition();
                String[] values = getNowPlaying().getPath().split("/");

                //利用message给主线程发消息更新seekbar进度
                Message ms=Message.obtain();
                Bundle bundle=new Bundle();
                bundle.putInt("current_position",currentPosition);
                String current_duration_str = getStringDuration(currentPosition);
                bundle.putString("current_position_str",current_duration_str);
                bundle.putString("duration_string",getStringDuration(getOnlineDuration()));
                bundle.putInt("duration_int", getOnlineDuration());
                bundle.putString("name",values[values.length - 1]);
                //设置发送的消息内容
                ms.setData(bundle);
                //发送消息
                FragmentPlaying.timerHandler.sendMessage(ms);
            }
        };
        timer.schedule(task,300,500);
        //当播放结束时停止播放
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                timer.cancel();
                task.cancel();
                next();
            }
        });

    }
}