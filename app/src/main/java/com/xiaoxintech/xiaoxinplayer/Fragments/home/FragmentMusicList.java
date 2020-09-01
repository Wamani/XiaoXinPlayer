package com.xiaoxintech.xiaoxinplayer.Fragments.home;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoxintech.xiaoxinplayer.Api.HttpUtils;
import com.xiaoxintech.xiaoxinplayer.Data.Music;
import com.xiaoxintech.xiaoxinplayer.Data.ResResult;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentPlay;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayEvent;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayerService;
import com.xiaoxintech.xiaoxinplayer.MusicService.Song;
import com.xiaoxintech.xiaoxinplayer.PlayingActivity;
import com.xiaoxintech.xiaoxinplayer.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static android.content.Context.BIND_AUTO_CREATE;

public class FragmentMusicList extends Fragment {
    public RecyclerView recyclerView;
    public ArrayList<Music> musicList = new ArrayList<>();
    MusicListAdapter adapter = new MusicListAdapter(musicList);
    List<Song> queue = new ArrayList<>();
    public static FragmentMusicList newInstance() {
        return new FragmentMusicList();
    }

    private PlayerService.MyBinder mMyBinder;
    Intent playerServiceIntent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Thread(runnable).start();
        playerServiceIntent = new Intent(getActivity(), PlayerService.class);
        getActivity().bindService(playerServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        recyclerView = view.findViewById(R.id.music_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<Music> musicList) {
                String musicName = musicList.get(position).getName();

                //切换到Playing Activity
                Intent intent=new Intent(getActivity(), PlayingActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

                String[] values = musicName.split("/");
                Toast.makeText(view.getContext(),"播放 " + values[values.length - 1], Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onItemLongClick(View view, int position, ArrayList<Music> musicList) {
                Toast.makeText(view.getContext(),"long click " + position + " item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            String res = HttpUtils.GetFileList("list");
            if(res.contains("failed")){
                Looper.prepare();
                Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
                Looper.loop();// 进入loop中的循环，查看消息队列
                return;
            }
            data.putString("value", res);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            // decode response
            String val = data.getString("value");
            Gson gson = new Gson();
            Type type = new TypeToken<ResResult<Music>>(){}.getType();
            ResResult<Music> response = gson.fromJson(val, type);
            List<Music> file_infos = response.file_infos;
            musicList.clear();
            musicList.addAll(file_infos);
            // update view
            adapter.notifyDataSetChanged();
            // set music list to music service
            for (Music music : musicList) {
                String musicUrl = getMusicUrl(music.getName());
                queue.add(getSong(musicUrl));
            }
            PlayEvent.getDefault().setQueue(queue);
        }
    };


    private Song getSong(String url) {
        Song song = new Song();
        song.setPath(url);
        return song;
    }

    private String getMusicUrl(String name) {
        return HttpUtils.GetRootURL()+"file?path=" + name;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (PlayerService.MyBinder) service;
            mMyBinder.init(PlayEvent.getDefault());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
