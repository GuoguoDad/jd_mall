package com.aries.rn;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.CatalystInstance;
import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.ReactContext;
import com.aries.rn.utils.MaskLog;

import java.lang.reflect.Field;
import java.util.List;

import static com.aries.rn.Constants.ASSETS_BUNDLE_PREFIX;

public class RootViewReload {


    private final Activity mActivity;
    private final ReactInstanceManager mReactInstanceManager;

    public RootViewReload(Activity activity, ReactInstanceManager reactInstanceManager) {
        this.mActivity = activity;
        this.mReactInstanceManager = reactInstanceManager;
    }


    // Use reflection to find and set the appropriate fields on ReactInstanceManager. See #556 for a proposal for a less brittle way
    // to approach this.
    // #2) Update the locally stored JS bundle file path
    public void setJSBundle(String latestJSBundleFile) throws IllegalAccessException {
//        ReactInstanceManager reactInstanceManager = getReactInstanceManager();
        try {
            JSBundleLoader latestJSBundleLoader;
            if (latestJSBundleFile.toLowerCase().startsWith(ASSETS_BUNDLE_PREFIX)) {
                latestJSBundleLoader = JSBundleLoader.createAssetLoader(mActivity, latestJSBundleFile, false);
            } else {
                latestJSBundleLoader = JSBundleLoader.createFileLoader(latestJSBundleFile);
            }
            Field bundleLoaderField = mReactInstanceManager.getClass().getDeclaredField("mBundleLoader");
            bundleLoaderField.setAccessible(true);
            bundleLoaderField.set(mReactInstanceManager, latestJSBundleLoader);
        } catch (Exception e) {
            MaskLog.e("Unable to set JSBundle -  may not support this version of React Native");
            throw new IllegalAccessException("Could not setJSBundle");
        }
    }


    // #3) Get the context creation method and fire it on the UI thread (which RN enforces)
    public void reload() {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                // We don't need to resetReactRootViews anymore
                // due the issue https://github.com/facebook/react-native/issues/14533
                // has been fixed in RN 0.46.0
//                    ReactInstanceManager instanceManager = getReactInstanceManager();
                resetReactRootViews(mReactInstanceManager);
                mReactInstanceManager.recreateReactContextInBackground();
            } catch (Exception e) {
                activityReload(mActivity);
                // The recreation method threw an unknown exception
                // so just simply fallback to restarting the Activity (if it exists)
            }
        });
    }


    private void activityReload(Activity activity) {
        activity.runOnUiThread(activity::recreate);
    }


    // This workaround has been implemented in order to fix https://github.com/facebook/react-native/issues/14533
    // resetReactRootViews allows to call recreateReactContextInBackground without any exceptions
    // This fix also relates to https://github.com/microsoft/react-native-code-push/issues/878
    private void resetReactRootViews(ReactInstanceManager reactInstanceManager) throws NoSuchFieldException, IllegalAccessException {
        Field mAttachedRootViewsField = reactInstanceManager.getClass().getDeclaredField("mAttachedRootViews");
        mAttachedRootViewsField.setAccessible(true);
        List<ReactRootView> mAttachedRootViews = (List<ReactRootView>) mAttachedRootViewsField.get(reactInstanceManager);
        for (ReactRootView reactRootView : mAttachedRootViews) {
            reactRootView.removeAllViews();
            reactRootView.setId(View.NO_ID);
        }
        mAttachedRootViewsField.set(reactInstanceManager, mAttachedRootViews);
    }

    public void loadScriptFromAsset(String assetName, boolean loadSynchronously) {
        CatalystInstance instance = mReactInstanceManager.getCurrentReactContext().getCatalystInstance();
        String source = assetName.startsWith(ASSETS_BUNDLE_PREFIX) ? assetName : ASSETS_BUNDLE_PREFIX + assetName;
        instance.loadScriptFromAssets(mActivity.getAssets(), source, loadSynchronously);
    }

    public void loadScriptFromFile(String fileName, String sourceUrl, boolean loadSynchronously) {
        CatalystInstance instance = mReactInstanceManager.getCurrentReactContext().getCatalystInstance();
        instance.loadScriptFromFile(fileName, sourceUrl, loadSynchronously);
//        try {
//            Method method = CatalystInstanceImpl.class.getDeclaredMethod("loadScriptFromFile", String.class, String.class, boolean.class);
//            method.setAccessible(true);
//            method.invoke(instance, fileName, sourceUrl, loadSynchronously);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Nullable
    private CatalystInstance catalystInstance(ReactInstanceManager manager) {
        if (null == manager) return null;
        ReactContext context = manager.getCurrentReactContext();
        if (null == context) return null;
        return context.getCatalystInstance();
    }

}
