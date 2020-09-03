package com.xiaoxintech.xiaoxinplayer.Fragments;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.xiaoxintech.xiaoxinplayer.Activity.MainActivity;
import com.xiaoxintech.xiaoxinplayer.Activity.MusicActivity;
import com.xiaoxintech.xiaoxinplayer.MusicService.MusicPlayer;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayEvent;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayerService;
import com.xiaoxintech.xiaoxinplayer.R;

import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentPlaying extends Fragment {
    private Toast toast;

    private ImageView play_mode;
    private ImageView play_status;
    private ImageView play_next;
    private ImageView play_pre;
    private ImageView music_list;

    private static TextView music_duration ;
    private static SeekBar playerSeekBar;
    private static TextView music_current_pos;
    private static Toolbar toolbar;
    private int play_index = 0;
    PlayerService.MyBinder mMyBinder;
    Intent playerServiceIntent;

    private ImageView cd_view;
    ObjectAnimator animation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playerServiceIntent = new Intent(getActivity(), PlayerService.class);
        play_index = MusicPlayer.PlayMode.valueOf(PlayEvent.getDefault().getMode().toString()).ordinal();
        view.getContext().bindService(playerServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        // bind button
        music_duration = view.findViewById(R.id.music_duration);
        playerSeekBar = view.findViewById(R.id.play_seek);
        music_current_pos = view.findViewById(R.id.music_duration_played);
        toolbar = view.findViewById(R.id.toolbar);

        play_status = view.findViewById(R.id.playing_play);
        play_mode = view.findViewById(R.id.playing_mode);
        play_next = view.findViewById(R.id.playing_next);
        play_pre = view.findViewById(R.id.playing_pre);
        music_list = view.findViewById(R.id.playing_playlist);
        cd_view = view.findViewById(R.id.cd_view);
        animation = ObjectAnimator.ofFloat(cd_view, "rotation", 0, 360);
        animation.setDuration(10000);
        animation.setRepeatCount(-1);
        LinearInterpolator linear = new LinearInterpolator();
        animation.setInterpolator(linear);
        animation.start();
        buttonEvent(view);
    }
    public void buttonEvent(View view){
        play_status.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if (PlayEvent.getDefault().getAction() == PlayEvent.Action.STOP){
                    PlayEvent.getDefault().setAction(PlayEvent.Action.RESUME);
                    play_status.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_pause));
                    animation.resume();
                } else {
                    PlayEvent.getDefault().setAction(PlayEvent.Action.STOP);
                    play_status.setImageDrawable(getResources().getDrawable(R.drawable.play_btn_play));
                    animation.pause();
                }
                mMyBinder.callMusicService(PlayEvent.getDefault());
            }
        });

        play_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayEvent.getDefault().setAction(PlayEvent.Action.PREVIOUS);
                mMyBinder.callMusicService(PlayEvent.getDefault());
            }
        });

        play_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayEvent.getDefault().setAction(PlayEvent.Action.NEXT);
                mMyBinder.callMusicService(PlayEvent.getDefault());
            }
        });

        switch (play_index){
            case 0:
                play_mode.setImageDrawable(getResources().getDrawable(R.drawable.play_icn_loop));
                ShowToast("顺序播放");
                break;
            case 1:
                play_mode.setImageDrawable(getResources().getDrawable(R.drawable.play_icn_shuffle));
                ShowToast("随机播放");
                break;
            case 2:
                play_mode.setImageDrawable(getResources().getDrawable(R.drawable.play_icn_one));
                ShowToast("单曲循环");
                break;
            default:
                break;
        }

        setPlayModeIcon(play_index);
        play_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_index = (play_index + 1) % 3;
                PlayEvent.getDefault().setMode(MusicPlayer.PlayMode.values()[play_index]);
                setPlayModeIcon(play_index);
                PlayEvent.getDefault().setAction(PlayEvent.Action.MODE);
                mMyBinder.callMusicService(PlayEvent.getDefault());

            }
        });
        music_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MusicActivity musicActivity = (MusicActivity) getActivity();
                assert musicActivity != null;
                musicActivity.setTabSelect(0);
            }
        });

        // seek bar 
        SeekBar playerSeekBar = view.findViewById(R.id.play_seek);
        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @ Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PlayEvent.getDefault().setSeekTo(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                PlayEvent.getDefault().setAction(PlayEvent.Action.SEEK);
                mMyBinder.callMusicService(PlayEvent.getDefault());
            }
        });

    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (PlayerService.MyBinder) service;
            mMyBinder.callMusicService(PlayEvent.getDefault());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null){
            getActivity().unbindService(mServiceConnection);
        }
    }
    /**
     * 将toast封装起来，连续点击时可以覆盖上一个
     */
    public void ShowToast(String text){
        if(toast == null) {
            toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }
        toast.show();
    }

    public static Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //处理消息
            Bundle bundle=msg.getData();
            //获取歌曲长度和当前播放位置，并设置到进度条上
            int currentPosition = bundle.getInt("current_position");
            String currentPositionStr = bundle.getString("current_position_str");
            playerSeekBar.setProgress(currentPosition);
            music_current_pos.setText(currentPositionStr);
            String durationString = bundle.getString("duration_string");
            int durationInt = bundle.getInt("duration_int");
            String title = bundle.getString("name");
            playerSeekBar.setMax(durationInt);
            playerSeekBar.setProgress(0);
            music_duration.setText(durationString);
            toolbar.setTitle(title);
        }
    };

    private void setPlayModeIcon(int play_index){
        switch (play_index){
            case 0:
                play_mode.setImageDrawable(getResources().getDrawable(R.drawable.play_icn_loop));
                ShowToast("顺序播放");
                break;
            case 1:
                play_mode.setImageDrawable(getResources().getDrawable(R.drawable.play_icn_shuffle));
                ShowToast("随机播放");
                break;
            case 2:
                play_mode.setImageDrawable(getResources().getDrawable(R.drawable.play_icn_one));
                ShowToast("单曲循环");
                break;
            default:
                break;
        }
    }
}
