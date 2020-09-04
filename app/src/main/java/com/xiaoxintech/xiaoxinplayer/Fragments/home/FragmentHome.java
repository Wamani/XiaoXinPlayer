package com.xiaoxintech.xiaoxinplayer.Fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentPlayList;
import com.xiaoxintech.xiaoxinplayer.R;

import java.util.ArrayList;

public class FragmentHome extends Fragment  {

    private ViewPager viewPager;
    private ArrayList<Fragment> list=new ArrayList<>();
    private ArrayList<String> title=new ArrayList<>();
    private FragmentPlayList fragmentMusicList;
    private FragmentSheet fragmentMusicSheet;
    private TabLayout tableLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_music,container,false);
        //初始化viewpager
        viewPager=view.findViewById(R.id.pager);
        //初始化tablayout
        tableLayout=view.findViewById(R.id.tab);
        title.add("我的歌单");
        title.add("歌手列表");
        //初始化fragment的列表
        setList();
        PagerAdapter adapter=new PagerAdapter(getChildFragmentManager(),list,title);
        //设置viewPager缓存的fragment数量，总共缓存（limit*2+1）个，limit就是里面的参数
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);
        //把tableLayout与viewPager绑定
        tableLayout.setupWithViewPager(viewPager);
        return view;
    }

    private void setList() {
        if (fragmentMusicList ==null){
            fragmentMusicList = new FragmentPlayList();
            list.add(fragmentMusicList);
        }
        if (fragmentMusicSheet ==null){
            fragmentMusicSheet = new FragmentSheet();
            list.add(fragmentMusicSheet);
        }
    }
}

class PagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragmentArrayList;
    private ArrayList<String> title;

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList, ArrayList<String> title) {
        super(fm);
        this.fragmentArrayList=fragmentArrayList;
        this.title=title;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}


