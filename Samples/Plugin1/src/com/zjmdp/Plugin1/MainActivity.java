package com.zjmdp.Plugin1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private boolean mIsBound;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.zjmdp.Plugin1.R.layout.main);
        initView();
    }

    private DemoService.LocalBinder mBinder;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBinder = (DemoService.LocalBinder)service;
        }

        public void onServiceDisconnected(ComponentName className) {
            mBinder = null;
        }
    };

    void doBindService() {
        Intent intent = new Intent(MainActivity.this, DemoService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mBinder = null;
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private void initView() {
        findViewById(com.zjmdp.Plugin1.R.id.button1).setOnClickListener(mOnClickListener);
        findViewById(com.zjmdp.Plugin1.R.id.button2).setOnClickListener(mOnClickListener);
        findViewById(com.zjmdp.Plugin1.R.id.button3).setOnClickListener(mOnClickListener);
        findViewById(com.zjmdp.Plugin1.R.id.button4).setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case com.zjmdp.Plugin1.R.id.button1:
                {
                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
                    startActivity(intent);
                }
                break;
                case com.zjmdp.Plugin1.R.id.button2:
                    doBindService();
                    break;
                case com.zjmdp.Plugin1.R.id.button3:
                {
                    if(mBinder != null){
                        mBinder.showToast();
                    }else{
                        Toast.makeText(MainActivity.this, "Bind service first!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
                case com.zjmdp.Plugin1.R.id.button4:
                    doUnbindService();
                    break;
            }
        }
    };
}
