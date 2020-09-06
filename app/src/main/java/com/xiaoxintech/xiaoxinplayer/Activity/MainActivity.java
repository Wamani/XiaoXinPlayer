package com.xiaoxintech.xiaoxinplayer.Activity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.xiaoxintech.xiaoxinplayer.Fragments.Music.FragmentMusic;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentVideo;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentMine;
import com.xiaoxintech.xiaoxinplayer.Fragments.FragmentSettings;
import com.xiaoxintech.xiaoxinplayer.MusicService.PlayEvent;
import com.xiaoxintech.xiaoxinplayer.R;


public class MainActivity extends AppCompatActivity {
    public static Context context;
    private static final String TAG = "MainActivity";
    private LinearLayout homeLin,messageLin,mineLin,settingLin;
    private TextView textView1,textView2,textView3,textView4;
    private Fragment homeFragment,messageFragment,mineFragment,settingFragment;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //控件等的初始化
        homeLin=findViewById(R.id.lin1);
        messageLin=findViewById(R.id.lin2);
        mineLin=findViewById(R.id.lin3);
        settingLin=findViewById(R.id.lin4);
        textView1=findViewById(R.id.t1);
        textView2=findViewById(R.id.t2);
        textView3=findViewById(R.id.t3);
        textView4=findViewById(R.id.t4);

        homeLin.setOnClickListener(listener);
        messageLin.setOnClickListener(listener);
        mineLin.setOnClickListener(listener);
        settingLin.setOnClickListener(listener);

        //默认选中第一个Fragment，也就是主页的Fragment
        setTabSelect(0);
        context = this;

        if (PlayEvent.FileInfo.size() == 0){
            PlayEvent.getDefault();
        }
    }

    View.OnClickListener listener=new View.OnClickListener() {
        //点击切换页面
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.lin1:
                    setTabSelect(0);
                    break;
                case R.id.lin2:
                    setTabSelect(1);
                    break;
                case R.id.lin3:
                    setTabSelect(2);
                    break;
                case R.id.lin4:
                    setTabSelect(3);
                    break;
            }
        }
    };

    //实现页面切换的核心代码，主要过程是动态加载Fragment
    private void setTabSelect(int index){
        initTextColor();
        if (fragmentManager==null){
            fragmentManager=getSupportFragmentManager();
        }
        FragmentTransaction ft=fragmentManager.beginTransaction();
        hideAllFragment(ft);
        switch (index){
            case 0:
                if (homeFragment==null){
                    homeFragment=new FragmentMusic();
                    ft.add(R.id.home_content,homeFragment);
                }else {
                    ft.show(homeFragment);
                }
                textView1.setTextColor(Color.parseColor("#03DAC5"));
                break;
            case 1:
                if (messageFragment==null){
                    messageFragment=new FragmentVideo();
                    ft.add(R.id.home_content,messageFragment);
                }
                ft.show(messageFragment);
                textView2.setTextColor(Color.parseColor("#03DAC5"));
                break;
            case 2:
                if (mineFragment==null){
                    mineFragment=new FragmentMine();
                    ft.add(R.id.home_content,mineFragment);

                }
                ft.show(mineFragment);
                textView3.setTextColor(Color.parseColor("#03DAC5"));
                break;
            case 3:
                if (settingFragment==null){
                    settingFragment=new FragmentSettings();
                    ft.add(R.id.home_content,settingFragment);
                }
                ft.show(settingFragment);
                textView4.setTextColor(Color.parseColor("#03DAC5"));
                break;
        }
        ft.commit();
    }

    private void initTextColor() {
        //一般情况下点击之后图标和文字都会改变颜色，我这里只做了文字颜色的改变，图标同理
        textView1.setTextColor(Color.parseColor("#1C86EE"));
        textView2.setTextColor(Color.parseColor("#1C86EE"));
        textView3.setTextColor(Color.parseColor("#1C86EE"));
        textView4.setTextColor(Color.parseColor("#1C86EE"));
    }

    //避免页面覆盖
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(homeFragment!=null){
            fragmentTransaction.hide(homeFragment);
        }
        if(messageFragment!=null){
            fragmentTransaction.hide(messageFragment);
        }if(mineFragment!=null){
            fragmentTransaction.hide(mineFragment);
        }if(settingFragment!=null){
            fragmentTransaction.hide(settingFragment);
        }
    }

}
