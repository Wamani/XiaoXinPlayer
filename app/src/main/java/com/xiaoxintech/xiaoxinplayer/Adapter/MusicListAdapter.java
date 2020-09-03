package com.xiaoxintech.xiaoxinplayer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.xiaoxintech.xiaoxinplayer.Data.Music;
import com.xiaoxintech.xiaoxinplayer.R;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {
    private ArrayList<Music> mMusicList;
    private OnItemClickListener onItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView musicName;
        TextView musicAuthor;

        public ViewHolder (View view)
        {
            super(view);
//            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            musicName = (TextView) view.findViewById(R.id.musicName);
            musicAuthor = (TextView) view.findViewById(R.id.musicAuthor);
        }
    }

    public MusicListAdapter(ArrayList <Music> musicList){
        mMusicList = musicList;
    }
    // ① 定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position, ArrayList<Music> musicList);
        void onItemLongClick(View view, int position, ArrayList<Music> musicList);
    }

    // ② 定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.musicName.setSelected(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos,mMusicList);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        Music music = mMusicList.get(position);
//        holder.fruitImage.setImageResource(fruit.getImageId());

        String[] musicName = music.getName().split("/");
        String[] names = musicName[musicName.length - 1].split("\\.");
        holder.musicName.setText(names[names.length - 2].replace(" ", ""));
        holder.musicAuthor.setText(musicName[musicName.length - 2].replace(" ", ""));
    }

    @Override
    public int getItemCount(){
        return mMusicList.size();
    }
}