package com.xiaoxintech.xiaoxinplayer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.xiaoxintech.xiaoxinplayer.Data.PlayList;
import com.xiaoxintech.xiaoxinplayer.R;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private ArrayList<PlayList> mPlayNameList;
    private OnItemClickListener onItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView playListName;

        public ViewHolder (View view)
        {
            super(view);
//            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            playListName = (TextView) view.findViewById(R.id.playListName);
        }
    }

    public PlayListAdapter(ArrayList <PlayList> playList){
        mPlayNameList = playList;
    }
    // ① 定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position, ArrayList<PlayList> musicList);
        void onItemLongClick(View view, int position, ArrayList<PlayList> musicList);
    }

    // ② 定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_playlist,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.playListName.setSelected(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos, mPlayNameList);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

        PlayList playList = mPlayNameList.get(position);
        holder.playListName.setText(playList.getName());
    }

    @Override
    public int getItemCount(){
        return mPlayNameList.size();
    }
}