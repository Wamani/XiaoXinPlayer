package com.xiaoxintech.xiaoxinplayer.Activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentMusicList;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentPlaying;
import com.xiaoxintech.xiaoxinplayer.R;

public class MusicActivity extends FragmentActivity {
    private Fragment listFragment, playingFragment;
    private FragmentManager fragmentManager;
    public String playListName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        playListName = getIntent().getStringExtra("playListName");
        setTabSelect(0);
    }

    public void setTabSelect(int index){
        if (fragmentManager==null){
            fragmentManager=getSupportFragmentManager();
        }
        FragmentTransaction ft=fragmentManager.beginTransaction();
        hideAllFragment(ft);
        switch (index){
            case 0:
                if (listFragment==null){
                    listFragment=new FragmentMusicList();
                    ft.add(R.id.musicActivity,listFragment);
                }
                ft.show(listFragment);
                break;
            case 1:
                if (playingFragment==null){
                    playingFragment=new FragmentPlaying();
                    ft.add(R.id.musicActivity,playingFragment);
                }
                ft.show(playingFragment);
                break;
        }
        ft.commit();
    }
    //避免页面覆盖
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(listFragment!=null){
            fragmentTransaction.hide(listFragment);
        }
        if(playingFragment!=null){
            fragmentTransaction.hide(playingFragment);
        }
    }

}