package com.zjmdp.pluginsdk;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by jamie on 14-6-3.
 */
public interface IActivity {
    public void IOnCreate(Bundle savedInstanceState);

    public void IOnResume();

    public void IOnStart();

    public void IOnPause();

    public void IOnStop();

    public void IOnDestroy();

    public void IOnRestart();

    public void IInit(String path, Activity context, ClassLoader classLoader);
}
