package com.zjmdp.pluginsdk;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public interface IService {

	public void IOnCreate();

	public void IOnStart(Intent intent, int startId);

	public int IOnStartCommand(Intent intent, int flags, int startId);

	public IBinder IOnBind(Intent intent);

	public boolean IOnUnbind(Intent intent);

	public void IOnDestroy();

    public void IInit(String apkPath, Service context, ClassLoader classLoader);
}
