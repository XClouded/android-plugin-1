package com.zjmdp.pluginsdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import dalvik.system.DexClassLoader;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jamie on 14-6-4.
 */
public class PluginUtils {

    private static final String PLUGIN_PATH = "plugins";

    private static final HashMap<String, DexClassLoader> sClassLoaderCache = new HashMap<String, DexClassLoader>();
    private static final ConcurrentHashMap<String, PackageInfo> sPackageInfoCache = new ConcurrentHashMap<String, PackageInfo>();

    static synchronized ClassLoader getClassLoader(Context c, String pluginName, String apkFilePath) throws Exception {

        DexClassLoader dexClassLoader = PluginUtils.sClassLoaderCache.get(pluginName);
        if (dexClassLoader == null) {
            String optimizedDexOutputPath = c.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath();
            dexClassLoader = new DexClassLoader(apkFilePath, optimizedDexOutputPath, null, c.getClassLoader());
            PluginUtils.sClassLoaderCache.put(pluginName, dexClassLoader);
        }
        return dexClassLoader;
    }

    static PackageInfo getPackgeInfo(Context context, String pluginFilePath){
        PackageInfo packageInfo =  sPackageInfoCache.get(pluginFilePath);
        if(packageInfo == null){
            try {
                PackageManager pm = context.getPackageManager();
                packageInfo = pm.getPackageArchiveInfo(pluginFilePath, PackageManager.GET_ACTIVITIES);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if(packageInfo != null){
            sPackageInfoCache.put(pluginFilePath, packageInfo);
        }
        return packageInfo;
    }

    static synchronized ClassLoader getClassLoader(String pluginID) {
        return  PluginUtils.sClassLoaderCache.get(pluginID);
    }
    public static File getInstallPath(Context context, String pluginID) {
        File pluginDir = getPluginPath(context);
        if (pluginDir == null) {
            return null;
        }
        int suffixBegin = pluginID.lastIndexOf('.');
        if (suffixBegin != -1 && !pluginID.substring(suffixBegin).equalsIgnoreCase(".apk")) {
            pluginID = pluginID.substring(0, suffixBegin) + ".apk";
        } else if (suffixBegin == -1) {
            pluginID = pluginID + ".apk";
        }
        return new File(pluginDir, pluginID);
    }

    public static File getPluginPath(Context context) {
        return context.getDir(PLUGIN_PATH, Context.MODE_PRIVATE);
    }

    public static void installPlugin(Context context, String pluginName){
        File pluginFile = new File(context.getDir(PLUGIN_PATH, Context.MODE_PRIVATE), pluginName);
        if(pluginFile.exists()){
            return;
        }

        BufferedInputStream bis;
        OutputStream dexWriter;

        final int BUF_SIZE = 8 * 1024;
        try {
            bis = new BufferedInputStream(context.getAssets().open(pluginFile.getName()));
            dexWriter = new BufferedOutputStream(
                    new FileOutputStream(pluginFile));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

