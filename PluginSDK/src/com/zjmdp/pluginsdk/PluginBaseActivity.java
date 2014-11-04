package com.zjmdp.pluginsdk;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.*;

/**
 * Created by jamie on 14-6-3.
 */
public class PluginBaseActivity extends Activity implements IActivity {

    private boolean mProxy;
    private Activity mProxyActivity;
    private PluginContext mPluginContext;
    private View mContentView;
    private Activity mRealActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mProxy) {
            mRealActivity = mProxyActivity;
        } else {
            super.onCreate(savedInstanceState);
            mRealActivity = this;
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mProxy) {
            mContentView = LayoutInflater.from(mPluginContext).inflate(layoutResID, null);
            mRealActivity.setContentView(mContentView);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (mProxy) {
            mContentView = view;
            mRealActivity.setContentView(mContentView);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public View findViewById(int id) {
        if (mProxy && mContentView != null && mContentView.findViewById(id) != null) {
            return mContentView.findViewById(id);
        } else {
            return super.findViewById(id);
        }
    }

    @Override
    public void IOnCreate(Bundle savedInstanceState) {
        onCreate(savedInstanceState);
    }

    @Override
    public void IOnResume() {
        onResume();
    }

    @Override
    public void IOnStart() {
        onStart();
    }

    @Override
    public void IOnPause() {
        onPause();
    }

    @Override
    public void IOnStop() {
        onStop();
    }

    @Override
    public void IOnDestroy() {
        onDestroy();
    }

    @Override
    public void IOnRestart() {
        onRestart();
    }

    @Override
    public void IInit(String path, Activity context, ClassLoader classLoader) {
        mProxy = true;
        mProxyActivity = context;

        mPluginContext = new PluginContext(context, 0, path, classLoader);
        attachBaseContext(mPluginContext);
    }

    @Override
    protected void onResume() {
        if (!mProxy) {
            super.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (!mProxy) {
            super.onPause();
        }

    }

    @Override
    protected void onStart() {
        if (!mProxy) {
            super.onStart();
        }
    }

    @Override
    protected void onRestart() {
        if (!mProxy) {
            super.onRestart();
        }
    }

    @Override
    protected void onStop() {
        if (!mProxy) {
            super.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        if (!mProxy) {
            super.onDestroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return !mProxy && super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return !mProxy && onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return !mProxy && onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!mProxy) {
            super.onWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return !mProxy && super.onTouchEvent(e);
    }

    @Override
    public Context getApplicationContext() {
        if (mProxy) {
            return mProxyActivity.getApplicationContext();
        } else {
            return super.getApplicationContext();
        }
    }

    @Override
    public void finish() {
        if (mProxy) {
            mProxyActivity.finish();
        } else {
            super.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mProxy) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if(mProxy){
            return mRealActivity.getLayoutInflater();
        } else {
            return LayoutInflater.from(mRealActivity);
        }
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        if (mProxy) {
            mRealActivity.overridePendingTransition(enterAnim, exitAnim);
        } else {
            super.overridePendingTransition(enterAnim, exitAnim);
        }
    }

    @Override
    public Object getSystemService(String name) {
        if (mProxy) {
            return mProxyActivity.getSystemService(name);
        } else {
            return super.getSystemService(name);
        }
    }


    @Override
    public WindowManager getWindowManager() {
        if (mProxy) {
            return mProxyActivity.getWindowManager();
        } else {
            return super.getWindowManager();
        }
    }

    @Override
    public int getChangingConfigurations() {
        if (mProxy) {
            return mProxyActivity.getChangingConfigurations();
        } else {
            return super.getChangingConfigurations();
        }
    }

    @Override
    public Window getWindow() {
        if (mProxy) {
            return mProxyActivity.getWindow();
        } else {
            return super.getWindow();
        }
    }

    @Override
    public void setTheme(int resid) {
        if (mProxy) {
            mProxyActivity.setTheme(resid);
        } else {
            super.setTheme(resid);
        }
    }

    @Override
    public String getPackageName() {
        if (mProxy) {
            return mProxyActivity.getPackageName();
        } else {
            return super.getPackageName();
        }
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (mProxy) {
            return mProxyActivity.getApplicationInfo();
        } else {
            return super.getApplicationInfo();
        }
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (mProxy) {
            intent.putExtra(PluginConstants.IS_IN_PLUGIN, true);
            mRealActivity.startActivityForResult(intent, requestCode);
        } else {
            super.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if(mProxy){
            service.putExtra(PluginConstants.IS_IN_PLUGIN, true);
            return mRealActivity.startService(service);
        }else{
            return super.startService(service);
        }
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        if(mProxy){
            service.putExtra(PluginConstants.IS_IN_PLUGIN, true);
            return mRealActivity.bindService(service, conn, flags);
        }else{
            return super.bindService(service, conn, flags);
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        if(mProxy){
            mRealActivity.unbindService(conn);
        }else{
            super.unbindService(conn);
        }
    }
}
