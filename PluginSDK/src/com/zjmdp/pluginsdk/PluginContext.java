package com.zjmdp.pluginsdk;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;

import java.lang.reflect.Method;

class PluginContext extends ContextThemeWrapper {

	private AssetManager mAsset;
	private Resources mResources;
	private Theme mTheme;
	private ClassLoader mClassLoader;

	private AssetManager getPluginAssets(String apkPath) {
		AssetManager instance = null;
		try {
			instance = AssetManager.class.newInstance();
			Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
			addAssetPathMethod.invoke(instance, apkPath);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return instance;
	}

	private Resources getPluginResources(Context ctx, AssetManager selfAsset) {
		DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
		Configuration configuration = ctx.getResources().getConfiguration();
		return new Resources(selfAsset, metrics, configuration);
	}

	private Theme getPluginTheme(Resources selfResources) {
		Theme theme = selfResources.newTheme();
		int themeResId = getInnerRIdValue("com.android.internal.R.style.Theme");
		theme.applyStyle(themeResId, true);
		return theme;
	}
	

	private int getInnerRIdValue(String rIdStrnig) {
		int value = -1;
		try {
			int rindex = rIdStrnig.indexOf(".R.");
			String Rpath = rIdStrnig.substring(0, rindex + 2);
			int fieldIndex = rIdStrnig.lastIndexOf(".");
			String fieldName = rIdStrnig.substring(fieldIndex + 1, rIdStrnig.length());
			rIdStrnig = rIdStrnig.substring(0, fieldIndex);
			String type = rIdStrnig.substring(rIdStrnig.lastIndexOf(".") + 1, rIdStrnig.length());
			String className = Rpath + "$" + type;

			Class<?> cls = Class.forName(className);
			value = cls.getDeclaredField(fieldName).getInt(null);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return value;
	}


	public PluginContext(Context base, int themeres, String pluginFilePath, ClassLoader classLoader) {
		super(base, themeres);
		mClassLoader = classLoader;
        mAsset = getPluginAssets(pluginFilePath);
        mResources = getPluginResources(base, mAsset);
		mTheme = getPluginTheme(mResources);
	}

	@Override
	public Resources getResources() {
		return mResources;
	}

	@Override
	public AssetManager getAssets() {
		return mAsset;
	}

	@Override
	public Theme getTheme() {
		return mTheme;
	}

	@Override
	public ClassLoader getClassLoader() {
		if (mClassLoader != null) {
			return mClassLoader;
		}
		return super.getClassLoader();
	}
}
