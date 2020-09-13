package com.xiaoxintech.xiaoxinplayer.Fragments.Login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.xiaoxintech.xiaoxinplayer.Activity.LoginActivity;
import com.xiaoxintech.xiaoxinplayer.Data.UserInfo;
import com.xiaoxintech.xiaoxinplayer.R;

public class FragmentLogin extends Fragment {

    private Button registerButton, loginButton;
    private EditText emailText, passwordText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailText = view.findViewById(R.id.login_username);
        passwordText = view.findViewById(R.id.register_password);
        registerButton = view.findViewById(R.id.toRegister);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LoginActivity loginActivity = (LoginActivity) getActivity();
                assert loginActivity != null;
                loginActivity.setTabSelect(1);
            }
        });

        loginButton = view.findViewById(R.id.login_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = emailText.getText().toString();
                UserInfo userInfo = new UserInfo(email, password);
            }
        });
    }
}