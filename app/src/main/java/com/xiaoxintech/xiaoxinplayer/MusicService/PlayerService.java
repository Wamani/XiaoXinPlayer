package com.xiaoxintech.xiaoxinplayer.MusicService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class PlayerService extends Service {

    private final MyBinder myBinder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyBinder extends Binder {
        public void callMusicService(PlayEvent playEvent){
            switch (playEvent.getAction()) {
                case INIT:
                    MusicPlayer.getDefault().setQueue(playEvent.getQueue(), MusicPlayer.getDefault().getCurrentSongIndex());
                    break;
                case PlayTarget:
                    MusicPlayer.getDefault().playTargetSong(playEvent.getIndex());
                case PLAY:
                    MusicPlayer.getDefault().playNow();
                    break;
                case NEXT:
                    MusicPlayer.getDefault().next();
                    break;
                case STOP:
                    MusicPlayer.getDefault().pause();
                    break;
                case RESUME:
                    MusicPlayer.getDefault().resume();
                    break;
                case PREVIOUS:
                    MusicPlayer.getDefault().previous();
                    break;
                case SEEK:
                    MusicPlayer.getDefault().seekTo(playEvent.getSeekTo());
                    break;
                case MODE:
                    MusicPlayer.getDefault().setPlayMode(playEvent.getMode());
                    break;
            }
        }
    }
}