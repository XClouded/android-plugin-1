package com.zjmdp.pluginsdk;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;

@SuppressLint("NewApi")
public abstract class PluginBaseService extends Service implements IService {
	
	protected boolean mProxy;

	protected Service mProxyService;
	protected String mPluginFilePath;

	@Override
	public void IOnCreate() { 
		onCreate();
	}

	@Override
	public void IOnStart(Intent intent, int startId) {
		onStart(intent, startId);
	}

	@Override
	public int IOnStartCommand(Intent intent, int flags, int startId) {
		return onStartCommand(intent, flags, startId);
	}

	public IBinder IOnBind(Intent intent) {
		return onBind(intent);
	}
	
	@Override
	public boolean IOnUnbind(Intent intent) {
		return onUnbind(intent);
	}

	@Override
	public void IOnDestroy() {
		onDestroy();
	}
	
	@Override
	public Object getSystemService(String name) {
        if (mProxy) {
            return mProxyService.getSystemService(name);
        } else {
            return super.getSystemService(name);
        }
	}
	
	@Override
	public String getPackageName() {
		if (mProxy) {
            return mProxyService.getPackageName();
		} else {
			return super.getPackageName();
		}
	}
	
	@Override
	public ApplicationInfo getApplicationInfo() {
		if (mProxy) {
            return mProxyService.getApplicationInfo();
		} else {
			return super.getApplicationInfo();
		}
	}
	
	@Override
	public void IInit(String apkPath, Service context, ClassLoader classLoader) {
		mProxy = true;
		mPluginFilePath = apkPath;
		mProxyService = context;

        PluginContext pluginContext = new PluginContext(context, 0, mPluginFilePath, classLoader);
		attachBaseContext(pluginContext);
	}

}
