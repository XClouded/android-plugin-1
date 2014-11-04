package com.zjmdp.Plugin1;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import com.zjmdp.pluginsdk.PluginBaseService;

/**
 * Created by jamie on 14-6-10.
 */
public class DemoService extends PluginBaseService{
    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onBind Service", Toast.LENGTH_LONG).show();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(getApplicationContext(), "onUnBind Service", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

    class LocalBinder extends Binder{
        public void showToast(){
            Toast.makeText(getApplicationContext(), "Show toast by service", Toast.LENGTH_LONG).show();
        }
    }

}
