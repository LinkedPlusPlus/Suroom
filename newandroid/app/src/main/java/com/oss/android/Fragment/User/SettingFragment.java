package com.oss.android.Fragment.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oss.android.LoginActivity;
import com.oss.android.MainActivity;
import com.oss.android.Model.Setting;
import com.oss.android.R;


public class SettingFragment extends Fragment {

    Button btn_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);


        btn_logout = (Button) view.findViewById(R.id.user_setting_btn_logout);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext().getApplicationContext(), MainActivity.class);
                Setting.Init();
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
