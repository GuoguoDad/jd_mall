package com.aries.rn.maskhub;

import androidx.annotation.Nullable;

import com.facebook.react.ReactInstanceManager;
import com.aries.rn.maskhub.utils.MaskLog;

import java.io.File;

/**
 * 配置
 */
public class Options {

    private static final Options mOptions = new Options();

    //  private static final ThreadLocal<Options> CURRENT_OPTION = new ThreadLocal<>();
//    private String mAssetsBundleFileName;

    private ReactInstanceHolder mReactInstanceHolder;

    private VersionCheckerDelegate mDelegate;

    private boolean isDebug;

    private String appKey;

    public String appKey() {
        return appKey;
    }

    public Options appKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    /**
     * react native 运行环境目录
     */
    private String contextDir;

    public VersionCheckerDelegate delegate() {
        return mDelegate;
    }

    public Options delegate(VersionCheckerDelegate delegate) {
        this.mDelegate = delegate;
        return this;
    }

    public Options debug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public Options log(MaskLog.OnLogger logger) {
        MaskLog.setLogger(logger);
        return this;
    }

    public String contextDir() {

        return contextDir.endsWith(File.separator) ? contextDir : contextDir + File.separator;
    }

    public Options contextDir(String contextDir) {
        this.contextDir = contextDir;
        return this;
    }

    public Options contextDir(File file) {
        this.contextDir = file.getAbsolutePath();
        return this;
    }

    public Options reactInstanceHolder(ReactInstanceHolder reactInstanceHolder) {
        this.mReactInstanceHolder = reactInstanceHolder;
        return this;
    }

    public Options reactInstanceManager(ReactInstanceManager reactInstanceManager) {
        this.mReactInstanceHolder = () -> reactInstanceManager;
        return this;
    }

//    public Options assetsBundleFileName(String assetsBundleFileName) {
//        this.mAssetsBundleFileName = assetsBundleFileName;
//        return this;
//    }

//    public String assetsBundleFileName() {
//        return mAssetsBundleFileName;
//    }

    public boolean debug() {
        return isDebug;
    }


    public ReactInstanceHolder reactInstanceHolder() {
        return mReactInstanceHolder;
    }


    @Nullable
    public ReactInstanceManager reactInstanceManager() {
        return reactInstanceHolder().getReactInstanceManager();
    }


//
//  public MaskHub createMaskHub() {
//    return new MaskHub();
//  }
//

    public static Options me() {
//        Options options = CURRENT_OPTION.get();
//        Thread c = Thread.currentThread();
//        String n = c.getId() + '@' + c.getName();
//        if (null == options) {
//            LogUtils.d(n + " has no Options, create new Options.");
//            options = new Options();
//            CURRENT_OPTION.set(options);
//        } else {
//            LogUtils.d(n + " return Options");
//        }
//        return options;

        return mOptions;
    }
}
