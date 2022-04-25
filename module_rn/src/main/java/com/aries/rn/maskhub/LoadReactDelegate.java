package com.aries.rn.maskhub;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.Callback;
import com.facebook.react.devsupport.DoubleTapReloadRecognizer;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.PermissionListener;

public class LoadReactDelegate {

    private final Activity mActivity;

    private ReactRootView mReactRootView;

    @Nullable
    private final DoubleTapReloadRecognizer mDoubleTapReloadRecognizer;

    private final ReactNativeHost mReactNativeHost;

    @Nullable
    private PermissionListener mPermissionListener;

    @Nullable
    private Callback mPermissionsCallback;

    public LoadReactDelegate(Activity activity, ReactNativeHost reactNativeHost) {
        mActivity = activity;
        mDoubleTapReloadRecognizer = new DoubleTapReloadRecognizer();
        mReactNativeHost = reactNativeHost;
    }

//    public LoadReactDelegate(Activity activity, ReactNativeHost reactNativeHost, @Nullable String appKey, @Nullable Bundle launchOptions) {
//        mActivity = activity;
////        mMainComponentName = appKey;
////        mLaunchOptions = launchOptions;
//        mDoubleTapReloadRecognizer = new DoubleTapReloadRecognizer();
//        mReactNativeHost = reactNativeHost;
//    }

    public void onHostResume() {
        if (getReactNativeHost().hasInstance()) {
            if (mActivity instanceof DefaultHardwareBackBtnHandler) {
                getReactNativeHost()
                        .getReactInstanceManager()
                        .onHostResume(mActivity, (DefaultHardwareBackBtnHandler) mActivity);
            } else {
                throw new ClassCastException("Host Activity does not implement DefaultHardwareBackBtnHandler");
            }

            if (null != mPermissionsCallback) {
                mPermissionsCallback.invoke();
                mPermissionsCallback = null;
            }
        }
    }


    public void onHostPause() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostPause(mActivity);
        }
    }

    public void onHostDestroy() {
        if (null != mReactRootView) {
            mReactRootView.unmountReactApplication();
            mReactRootView = null;
        }
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostDestroy(mActivity);
        }
    }

    public boolean onBackPressed() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onBackPressed();
            return true;
        }
        return false;
    }


    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance()
                && getReactNativeHost().getUseDeveloperSupport()
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
            return true;
        }
        return false;
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onWindowFocusChange(hasFocus);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, boolean shouldForwardToReactInstance) {
        if (getReactNativeHost().hasInstance() && shouldForwardToReactInstance) {
            getReactNativeHost().getReactInstanceManager().onActivityResult(mActivity, requestCode, resultCode, data);
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.onActivityResult(requestCode, resultCode, data, true);
    }


//    public void loadApp() {
//        loadApp(mMainComponentName);
//    }

    public void loadApp(String appKey, @Nullable Bundle launchOptions) {
        if (null != mReactRootView)
            throw new IllegalStateException("Cannot loadApp while app is already running.");

        // react native 的偶现BUG
        // https://github.com/facebook/react-native/issues/28461
        if (null != launchOptions) launchOptions.remove("profile");
        mReactRootView = createRootView();
        if (null != mActivity) mActivity.setContentView(mReactRootView);
        mReactRootView.startReactApplication(getReactNativeHost().getReactInstanceManager(), appKey, launchOptions);
    }



    /**
     * 创建全新的React Native View
     *
     * @param appKey
     * @param launchOptions
     * @return
     */
    public ReactRootView createApp(String appKey, @Nullable Bundle launchOptions) {
        ReactRootView reactRootView = createRootView();
        reactRootView.startReactApplication(getReactNativeHost().getReactInstanceManager(), appKey, launchOptions);
        return reactRootView;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance()
                && getReactNativeHost().getUseDeveloperSupport()
                && keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
            event.startTracking();
            return true;
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return shouldShowDevMenuOrReload(keyCode, event);
    }

    /**
     * Handles delegating the {@link Activity#onKeyUp(int, KeyEvent)} method to determine whether the
     * application should show the developer menu or should reload the React Application.
     *
     * @return true if we consume the event and either shoed the develop menu or reloaded the
     * application.
     */
    public boolean shouldShowDevMenuOrReload(int keyCode, KeyEvent event) {
        if (getReactNativeHost().hasInstance() && getReactNativeHost().getUseDeveloperSupport()) {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
                return true;
            }
            boolean didDoubleTapR = Assertions.assertNotNull(mDoubleTapReloadRecognizer)
                    .didDoubleTapR(keyCode, mActivity.getCurrentFocus());
            if (didDoubleTapR) {
                getReactNativeHost().getReactInstanceManager().getDevSupportManager().handleReloadJS();
                return true;
            }
        }
        return false;
    }

    public boolean onNewIntent(Intent intent) {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onNewIntent(intent);
            return true;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mPermissionListener = listener;
        mActivity.requestPermissions(permissions, requestCode);
    }

    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        mPermissionsCallback = args -> {
            if (null != mPermissionListener && mPermissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                mPermissionListener = null;
            }
        };
    }


    public ReactRootView getReactRootView() {
        return mReactRootView;
    }

    protected ReactRootView createRootView() {
        return new ReactRootView(mActivity);
    }


    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    public ReactInstanceManager getReactInstanceManager() {
        return getReactNativeHost().getReactInstanceManager();
    }

}
