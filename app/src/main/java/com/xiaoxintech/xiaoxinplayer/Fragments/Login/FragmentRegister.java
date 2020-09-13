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
import com.xiaoxintech.xiaoxinplayer.Utils.Common;
import com.xiaoxintech.xiaoxinplayer.Utils.HttpUtils;
import com.xiaoxintech.xiaoxinplayer.Data.UserInfo;
import com.xiaoxintech.xiaoxinplayer.R;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentRegister extends Fragment {
    private EditText register_email, register_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        register_email = view.findViewById(R.id.register_username);
        register_password = view.findViewById(R.id.register_password);
        Button register_register = view.findViewById(R.id.register_register);
        Button register_login = view.findViewById(R.id.register_login);
        register_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                assert loginActivity != null;
                loginActivity.setTabSelect(0);
            }
        });
        register_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = register_email.getText().toString();
                String password = register_password.getText().toString();
                UserInfo userInfo = new UserInfo(email, password);
                String res = HttpUtils.getInstance().Register(userInfo);
                try {
                    JSONObject jObject  = new JSONObject(res);
                    int errorCode = (int) jObject.get("error_code");
                    if (errorCode == 0) {
                        Common.getInstance().ShowToast("注册成功", getActivity());
                    }
                    if (errorCode == 1002) {
                        Common.getInstance().ShowToast("账号已存在，请直接登录", getActivity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}