package com.aries.rn.maskhub.navigate;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class NavigateModule extends ReactContextBaseJavaModule {
    private Context context;

    public NavigateModule(@Nullable ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "Navigate";
    }


    @ReactMethod
    public void pushTo(String url) {

    }


    /**
     * 关闭当前页面
     */
    @ReactMethod
    public void close() {
        Activity activity = getCurrentActivity();
        if (null != activity) {
            activity.finish();
        }
    }

    /**
     * react native 发送消息到jsbundle前端
     *
     * @param eventName
     * @param params
     */
    private void event(String eventName, @Nullable WritableMap params) {
        ReactApplicationContext context = this.getReactApplicationContext();
        if (null == context) {
            return;
        }
        if (null == params) {
            params = Arguments.createMap();
        }
        Log.d(this.getClass().getSimpleName(), String.format("event -> name: %s", eventName));
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

}

