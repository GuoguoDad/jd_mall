package com.aries.rn;

import androidx.annotation.Nullable;

import java.util.Map;

public interface VersionCheckerDelegate {


    /**
     * 处理版本检查的结果
     *
     * @return
     */
    void processCheckResult(Map<String, Object> params, Callback callback);

    BundleEntity lastVersion(String name);

    void putVersion(String name, BundleEntity version);

    boolean hasVersion(@Nullable BundleEntity oldVersion, BundleEntity newVersion);

    interface Callback {
        void onResult(int code, BundleEntity result);
    }
}
