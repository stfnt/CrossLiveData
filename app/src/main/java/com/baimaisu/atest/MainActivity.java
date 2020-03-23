package com.baimaisu.atest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baimaisu.atest.account.AccountApi;
import com.baimaisu.atest.account.AccountInfo;
import com.baimaisu.process.CrossLiveData;
import com.baimaisu.retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityAXX";

    private AccountInfo accountInfo = new AccountInfo("axx","#ff0000");
    private CrossLiveData<AccountInfo> infoCrossLiveData = Retrofit.createApi(AccountApi.class).getAccountInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
    }

    private void bindView(){
        TextView tvProcessName = findViewById(R.id.tvProcessName);
        tvProcessName.setText(AppUtil.getCurrentProcessName());
        final EditText etAccountName = findViewById(R.id.etAccountName);
        final EditText etAccountColor = findViewById(R.id.etAccountColor);

        findViewById(R.id.btnSetAccountName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountInfo.setName(etAccountName.getText().toString());
                infoCrossLiveData.setValue(accountInfo);
            }
        });

        findViewById(R.id.btnSetAccountColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountInfo.setColor(etAccountColor.getText().toString());
                infoCrossLiveData.setValue(accountInfo);
            }
        });

        findViewById(R.id.btnRecycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoCrossLiveData.recycle();
                Toast.makeText(MainActivity.this, "释放所有监听", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnStartSubProcess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SubProcessActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
            }
        });
    }
}
