package com.xiaoxintech.xiaoxinplayer;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.xiaoxintech.xiaoxinplayer.MusicService.MusicPlayer;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayEvent;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayerService;

public class PlayingActivity extends FragmentActivity {
    private Toast toast;

    private ImageView play_mode;
    private ImageView play_status;
    private ImageView play_next;
    private ImageView play_pre;
    private ImageView music_list;
    private static TextView music_duration ;
    private static SeekBar playerSeekBar;
    private static TextView music_current_pos;
    private static android.widget.Toolbar toolbar;
    private int play_index = 0;
    PlayerService.MyBinder mMyBinder;
    Intent playerServiceIntent;

    private ImageView cd_view;
    ObjectAnimator animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_play);
        playerServiceIntent = new Intent(this, PlayerService.class);
        bindService(playerServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        // bind button
        music_duration = findViewById(R.id.music_duration);
        playerSeekBar = findViewById(R.id.play_seek);
        music_current_pos = findViewById(R.id.music_duration_played);
        toolbar = findViewById(R.id.toolbar);

        play_status = findViewById(R.id.playing_play);
        play_mode = findViewById(R.id.playing_mode);
        play_next = findViewById(R.id.playing_next);
        play_pre = findViewById(R.id.playing_pre);
        music_list = findViewById(R.id.playing_playlist);
        cd_view = findViewById(R.id.cd_view);
        animation = ObjectAnimator.ofFloat(cd_view, "rotation", 0, 360);
        animation.setDuration(10000);
        animation.setRepeatCount(-1);
        LinearInterpolator linear = new LinearInterpolator();
        animation.setInterpolator(linear);
        animation.start();
        buttonEvent();
    }
    public void buttonEvent(){
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
        play_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_index = (play_index + 1) % 3;
                PlayEvent.getDefault().setMode(MusicPlayer.PlayMode.values()[play_index]);
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
                PlayEvent.getDefault().setAction(PlayEvent.Action.MODE);
                mMyBinder.callMusicService(PlayEvent.getDefault());

            }
        });
        music_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayingActivity.this, MainActivity.class);
                intent.putExtra("name",0);
                startActivity(intent);
            }
        });
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (PlayerService.MyBinder) service;
            int song_index = getIntent().getIntExtra("position", 10);
            PlayEvent.getDefault().setAction(PlayEvent.Action.PlayTarget);
            PlayEvent.getDefault().setIndex(song_index);
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
    /**
     * 将toast封装起来，连续点击时可以覆盖上一个
     */
    public void ShowToast(String text){
        if(toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
        }
        toast.show();
    }
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //处理消息
            Bundle bundle=msg.getData();
            String durationString = bundle.getString("duration_string");
            int durationInt = bundle.getInt("duration_int");
            String title = bundle.getString("name");
            playerSeekBar.setMax(durationInt);
            playerSeekBar.setProgress(0);
            music_duration.setText(durationString);
            toolbar.setTitle(title);
        }
    };
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
        }
    };
}
