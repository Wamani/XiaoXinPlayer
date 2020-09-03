package com.xiaoxintech.xiaoxinplayer.Fragments;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.xiaoxintech.xiaoxinplayer.Activity.MusicActivity;
import com.xiaoxintech.xiaoxinplayer.Adapter.MusicListAdapter;
import com.xiaoxintech.xiaoxinplayer.Adapter.PlayListAdapter;
import com.xiaoxintech.xiaoxinplayer.Data.Music;
import com.xiaoxintech.xiaoxinplayer.Data.PlayList;
import com.xiaoxintech.xiaoxinplayer.R;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentPlayList extends Fragment {
    private RecyclerView playList;
    public ArrayList<PlayList> playListName = new ArrayList<>();
    PlayListAdapter adapter = new PlayListAdapter(playListName);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playList = view.findViewById(R.id.playList);
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        playList.setLayoutManager(recyclerViewLayoutManager);
        playList.setAdapter(adapter);
        PlayList playList = new PlayList("全部歌曲", 0);
        playListName.add(playList);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new PlayListAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position, ArrayList<PlayList> playLists) {
                //切换到Playing Activity
                Intent intent=new Intent(getActivity(), MusicActivity.class);
                intent.putExtra("playListName", playListName.get(position).getName());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position, ArrayList<PlayList> playLists) {

            }
        });

    }

}