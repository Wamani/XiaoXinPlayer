package com.xiaoxintech.xiaoxinplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayEvent;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayerService;

public class PlayingActivity extends FragmentActivity {
    private ImageView play_mode;
    private ImageView play_fav;
    private final int play_index = 0;
    private final boolean fav_status = false;
    private static TextView music_duration ;
    private static SeekBar playerSeekBar;
    private static TextView music_current_pos;
    private static android.widget.Toolbar toolbar;

    Intent playerServiceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_play);
        playerServiceIntent = new Intent(this, PlayerService.class);
        bindService(playerServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        music_duration = findViewById(R.id.music_duration);
        playerSeekBar = findViewById(R.id.play_seek);
        music_current_pos = findViewById(R.id.music_duration_played);
        toolbar = findViewById(R.id.toolbar);

        ImageView play = findViewById(R.id.playing_play);
        ImageView music_list = findViewById(R.id.playing_playlist);
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
            PlayerService.MyBinder mMyBinder = (PlayerService.MyBinder) service;
            int song_index = getIntent().getIntExtra("position", 10);
            mMyBinder.init(PlayEvent.getDefault());
            mMyBinder.playTarget(song_index);
            PlayEvent.getDefault().setAction(PlayEvent.Action.PlayTarget);
            PlayEvent.getDefault().setIndex(song_index);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
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
