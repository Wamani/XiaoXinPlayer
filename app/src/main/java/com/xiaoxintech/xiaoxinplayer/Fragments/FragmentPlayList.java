package com.xiaoxintech.xiaoxinplayer.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.xiaoxintech.xiaoxinplayer.Activity.MusicActivity;
import com.xiaoxintech.xiaoxinplayer.Adapter.PlayListAdapter;
import com.xiaoxintech.xiaoxinplayer.Data.PlayList;
import com.xiaoxintech.xiaoxinplayer.R;

import java.util.ArrayList;

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
        PlayList playList_add = new PlayList("新增", 1);
        playListName.add(playList);
        playListName.add(playList_add);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new PlayListAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position, ArrayList<PlayList> playLists) {
                if (position == playListName.size() - 1) {
                    final EditText inputServer = new EditText(getActivity());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("输入歌单名称").setView(inputServer)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String text = inputServer.getText().toString();
                            PlayList playListTmp = new PlayList(text, 0);
                            playListName.add(playListName.size()-1, playListTmp);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();
                }else {
                    //切换到Playing Activity
                    Intent intent=new Intent(getActivity(), MusicActivity.class);
                    intent.putExtra("playListName", playListName.get(position).getName());
                    startActivity(intent);
                }

            }
        });
        adapter.setOnItemLongClickListener(new PlayListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position, ArrayList<PlayList> playLists) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final String[] items = { "删除","编辑","取消"};
                final int playListIndex = position;
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        if (playListIndex == 0 || playListIndex == playListName.size() - 1){
                            Toast.makeText(getActivity(),
                                    "不允许编辑/删除歌单：" + playListName.get(playListIndex).getName(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (index == 0) {
                            Toast.makeText(getActivity(),
                                    "你删除了歌单：" + playListName.get(playListIndex).getName(), Toast.LENGTH_SHORT).show();
                            playListName.remove(playListIndex);
                            adapter.notifyDataSetChanged();
                        }else if (index == 2){
                            dialog.dismiss();
                        }else {
                            final EditText inputServer = new EditText(getActivity());
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle(playListName.get(playListIndex).getName()+" 改为").setView(inputServer)
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String text = inputServer.getText().toString();
                                    playListName.get(playListIndex).setName(text);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            builder.show();
                        }
                    }
                });
                builder.show();
            }
        });

    }

}