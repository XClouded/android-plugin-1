package com.zjmdp.pluginsdk;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.File;
import java.io.IOException;

public class ProxyService extends Service {
	
	private IService mPluginService;
	private String mPluginName;
	private String mPluginFilePath;
	private String mLaunchService;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		boolean ret = startPluginIfNeccessary(intent);
		if(ret && null != mPluginService) {
			mPluginService.IOnStart(intent, startId);
		}
	}

	@SuppressLint("NewApi")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		boolean ret = startPluginIfNeccessary(intent);
		if(ret && null != mPluginService) {
			mPluginService.IOnStartCommand(intent, flags, startId);
		}
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		boolean res = super.onUnbind(intent);
		if(null != mPluginService) {
			res = mPluginService.IOnUnbind(intent);
		}
		return res;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(null != mPluginService) {
			mPluginService.IOnDestroy();
			mPluginService = null;
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		IBinder res = null;
		if(null == mPluginService) {
			startPluginIfNeccessary(intent);
		}
		if(null != mPluginService) {
			res = mPluginService.IOnBind(intent);
		}
		return res;
	}

	protected boolean startPluginIfNeccessary(Intent intent) {
		if(null == intent) {
			return false;
		}
		
		String pluginName = intent.getStringExtra(PluginConstants.PLUGIN_NAME);
		String launchService = intent.getStringExtra(PluginConstants.LAUNCH_SERVICE);
        File file = PluginUtils.getInstallPath(this, pluginName);
        try {
            mPluginFilePath = file.getCanonicalPath();
        } catch (IOException e) {
            e = null;
        }

        if(null != mPluginService) {
			if(mPluginName.equals(pluginName) && mLaunchService.equals(launchService)) {
				//already init the same service
				return true;
			} else {
				//error arguments
				return false;
			}
		}
		
		mPluginName = pluginName;
		mLaunchService = launchService;

		ClassLoader classLoader = PluginUtils.getClassLoader(mPluginName);
		if(null != classLoader) {
			intent.setExtrasClassLoader(classLoader);
		}
		
		String errInfo = null;
		if (mPluginName == null || mPluginName.length() == 0) {
			errInfo = "Plugin name is wrong";
		} else {
			File f = new File(mPluginFilePath);
			if (!f.exists() && !f.isFile()) {
				errInfo = "Plugin File Not Found!";
			} else {
				try {
					initPlugin();
					if(null == errInfo) {
						mPluginService.IOnCreate();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	
	private void initPlugin() throws Exception {
		ClassLoader classLoader = PluginUtils.getClassLoader(this, mPluginName, mPluginFilePath);
		Class<?> pluginServiceClass = classLoader.loadClass(mLaunchService);
		mPluginService = (IService) pluginServiceClass.newInstance();
		mPluginService.IInit(mPluginFilePath, this, classLoader);
	}
}
