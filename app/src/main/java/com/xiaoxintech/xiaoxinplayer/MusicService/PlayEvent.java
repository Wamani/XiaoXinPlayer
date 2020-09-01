package com.xiaoxintech.xiaoxinplayer.MusicService;

import java.util.List;

public class PlayEvent {

    public static PlayEvent playEvent;
    public static PlayEvent getDefault() {
        if (playEvent == null) {
            synchronized (PlayEvent.class) {
                if (playEvent == null) {
                    playEvent =  new PlayEvent();
                }
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
    private MusicPlayer.PlayMode mode;
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
}