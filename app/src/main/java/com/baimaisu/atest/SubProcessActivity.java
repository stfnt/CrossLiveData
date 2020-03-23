package com.baimaisu.atest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.baimaisu.atest.account.AccountApi;
import com.baimaisu.atest.account.AccountInfo;
import com.baimaisu.process.CrossLiveData;
import com.baimaisu.retrofit.Retrofit;

public class SubProcessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subprocess);
        bindView();
    }

    private void bindView(){
        TextView tvProcessName = findViewById(R.id.tvProcessName);
        tvProcessName.setText(AppUtil.getCurrentProcessName());

        final TextView tvAccountInfo = findViewById(R.id.tvAccountInfo);
        final CrossLiveData<AccountInfo> infoCrossLiveData = Retrofit.createApi(AccountApi.class).getAccountInfo().toSubProcess();

        infoCrossLiveData.observe(this, new Observer<AccountInfo>() {
            @Override
            public void onChanged(AccountInfo accountInfo) {
                if(tvAccountInfo == null){
                    return;
                }
                tvAccountInfo.setText(accountInfo.getName());
                tvAccountInfo.setTextColor(Color.parseColor(accountInfo.getColor()));
            }
        });

        findViewById(R.id.btnRecycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoCrossLiveData.recycle();
                Toast.makeText(SubProcessActivity.this, "不再接收到监听", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
