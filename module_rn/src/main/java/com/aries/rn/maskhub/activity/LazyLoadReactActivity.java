package com.aries.rn.maskhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.aries.rn.maskhub.LoadReactDelegate;

public abstract class LazyLoadReactActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler, PermissionAwareActivity {

    private LoadReactDelegate mReactDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactDelegate = createReactActivityDelegate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReactDelegate.onHostPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReactDelegate.onHostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReactDelegate.onHostDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mReactDelegate.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mReactDelegate.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mReactDelegate.onKeyUp(keyCode, event) || super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return mReactDelegate.onKeyLongPress(keyCode, event) || super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (!mReactDelegate.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (!mReactDelegate.onNewIntent(intent)) {
            super.onNewIntent(intent);
        }
    }

    @Override
    public void requestPermissions(String[] permissions, int requestCode, PermissionListener listener) {
        mReactDelegate.requestPermissions(permissions, requestCode, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mReactDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mReactDelegate.onWindowFocusChanged(hasFocus);
    }


    protected LoadReactDelegate createReactActivityDelegate() {
        ReactNativeHost reactNativeHost = ((ReactApplication) getApplication()).getReactNativeHost();
        return new LoadReactDelegate(this, reactNativeHost);
    }


    protected final void loadApp(String appKey) {
        loadApp(appKey, getIntent().getExtras());
    }

    protected final void loadApp(String appKey, Bundle launchOptions) {
        mReactDelegate.loadApp(appKey, launchOptions);
    }


    @Nullable
    protected abstract String getMainComponentName();


    protected final LoadReactDelegate getReactDelegate() {
        return mReactDelegate;
    }

    protected final ReactNativeHost getReactNativeHost() {
        return mReactDelegate.getReactNativeHost();
    }

    protected final ReactInstanceManager getReactInstanceManager() {
        return mReactDelegate.getReactInstanceManager();
    }


    /**
     * 发送消息到 前端页面
     *
     * @param eventName
     * @param params
     */
    protected void sendEvent(@NonNull String eventName, @Nullable WritableMap params) {
        ReactContext reactContext = mReactDelegate.getReactInstanceManager().getCurrentReactContext();
        if (null == reactContext) return;
        WritableMap eventData = null == params ? Arguments.createMap() : params;
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, eventData);
    }

}
