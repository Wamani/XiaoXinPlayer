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
                    MusicPlayer.getPlayer().setQueue(playEvent.getQueue(), MusicPlayer.getPlayer().getCurrentSongIndex());
                    break;
                case PlayTarget:
                    MusicPlayer.getPlayer().playTargetSong(playEvent.getIndex());
                case PLAY:
                    MusicPlayer.getPlayer().playNow();
                    break;
                case NEXT:
                    MusicPlayer.getPlayer().next();
                    break;
                case STOP:
                    MusicPlayer.getPlayer().pause();
                    break;
                case RESUME:
                    MusicPlayer.getPlayer().resume();
                    break;
                case PREVIOUS:
                    MusicPlayer.getPlayer().previous();
                    break;
                case SEEK:
                    MusicPlayer.getPlayer().seekTo(playEvent.getSeekTo());
                    break;
                case MODE:
                    MusicPlayer.getPlayer().setPlayMode(playEvent.getMode());
                    break;
            }
        }
    }
}