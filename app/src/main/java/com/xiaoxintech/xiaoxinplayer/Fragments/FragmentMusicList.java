package com.xiaoxintech.xiaoxinplayer.Fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.xiaoxintech.xiaoxinplayer.Activity.MusicActivity;
import com.xiaoxintech.xiaoxinplayer.Data.Music;
import com.xiaoxintech.xiaoxinplayer.Adapter.MusicListAdapter;
import com.xiaoxintech.xiaoxinplayer.MusicService.MusicPlayer;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayEvent;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayerService;
import com.xiaoxintech.xiaoxinplayer.R;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentMusicList extends Fragment {
    public RecyclerView recyclerView;
    private TextView currentSong, currentAuthor;
    public ArrayList<Music> musicList = new ArrayList<>();
    MusicListAdapter adapter = new MusicListAdapter(musicList);
    public static FragmentMusicList newInstance() {
        return new FragmentMusicList();
    }

    private PlayerService.MyBinder mMyBinder;
    Intent playerServiceIntent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 最下方
        currentSong = view.findViewById(R.id.musicNameCurrent);
        currentAuthor = view.findViewById(R.id.musicAuthorCurrent);
        currentSong.setSelected(true);
        LinearLayout linearLayout = view.findViewById(R.id.currentPlayingBar);
        linearLayout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                MusicActivity musicActivity = (MusicActivity) getActivity();
                assert musicActivity != null;
                if (MusicPlayer.getDefault().getIsInit()){
                    PlayEvent.getDefault().setAction(PlayEvent.Action.PlayTarget);
                    while (mMyBinder == null){}
                    mMyBinder.callMusicService(PlayEvent.getDefault());
                }
                musicActivity.setTabSelect(1);
            }
        });

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.playListReturnBar);
        toolbar.setTitle("");
        getActivity().setActionBar(toolbar);//设置toolbar
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true); // 给左上角图标的左边加上一个返回的图
        TextView textView = view.findViewById(R.id.toolbarTitle);
        textView.setText(((MusicActivity) Objects.requireNonNull(getActivity())).playListName);

        playerServiceIntent = new Intent(getActivity(), PlayerService.class);
        getActivity().bindService(playerServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        recyclerView = view.findViewById(R.id.music_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<Music> musicList) {

                MusicActivity musicActivity = (MusicActivity) getActivity();
                assert musicActivity != null;
                PlayEvent.getDefault().setIndex(position);
                PlayEvent.getDefault().setAction(PlayEvent.Action.PlayTarget);
                mMyBinder.callMusicService(PlayEvent.getDefault());
                updateCurrentBarInfo();
                musicActivity.setTabSelect(1);

                String musicName = musicList.get(position).getName();
                String[] values = musicName.split("/");
                Toast.makeText(view.getContext(),"播放 " + values[values.length - 1], Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onItemLongClick(View view, int position, ArrayList<Music> musicList) {
                Toast.makeText(view.getContext(),"long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });

        musicList.clear();

        musicList.addAll(PlayEvent.FileInfo);
        // update view
        adapter.notifyDataSetChanged();
        updateCurrentBarInfo();
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (PlayerService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null){
            getActivity().unbindService(mServiceConnection);
        }
    }

    private void updateCurrentBarInfo(){
        if (MusicPlayer.getDefault().getNowPlaying() != null) {
            String[] musicName = MusicPlayer.getDefault().getNowPlaying().getPath().split("/");
            String[] names = musicName[musicName.length - 1].split("\\.");
            currentSong.setText(names[names.length - 2].replace(" ", ""));
            currentAuthor.setText(musicName[musicName.length - 2].replace(" ", ""));
        }
    }

}
