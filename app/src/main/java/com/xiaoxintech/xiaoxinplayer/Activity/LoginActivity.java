package com.xiaoxintech.xiaoxinplayer.Activity;

import android.os.Bundle;
import android.os.StrictMode;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.xiaoxintech.xiaoxinplayer.Fragments.Login.FragmentLogin;
import com.xiaoxintech.xiaoxinplayer.Fragments.Login.FragmentRegister;
import com.xiaoxintech.xiaoxinplayer.R;

public class LoginActivity extends FragmentActivity {
    private Fragment loginFragment, registerFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTabSelect(0);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public void setTabSelect(int index){
        if (fragmentManager==null){
            fragmentManager=getSupportFragmentManager();
        }
        FragmentTransaction ft=fragmentManager.beginTransaction();
        hideAllFragment(ft);
        switch (index){
            case 0:
                if (loginFragment==null){
                    loginFragment=new FragmentLogin();
                    ft.add(R.id.loginActivity,loginFragment);
                }
                ft.show(loginFragment);
                break;
            case 1:
                if (registerFragment==null){
                    registerFragment = new FragmentRegister();
                    ft.add(R.id.loginActivity,registerFragment);
                }
                ft.show(registerFragment);
                break;
        }
        ft.commit();
    }
    //避免页面覆盖
    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if(loginFragment!=null){
            fragmentTransaction.hide(loginFragment);
        }
        if(registerFragment!=null){
            fragmentTransaction.hide(registerFragment);
        }
    }
}