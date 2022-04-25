package com.aries.rn.maskhub.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.aries.common.constants.RouterPaths;
import com.aries.common.dialog.LoadingDialog;
import com.aries.rn.maskhub.BundleEntity;
import com.aries.rn.maskhub.Options;
import com.aries.rn.maskhub.Page;
import com.aries.rn.maskhub.ReactJsBundleFactory;
import com.aries.rn.maskhub.RootViewReload;
import com.aries.rn.maskhub.VersionCheckerDelegate;
import com.aries.rn.maskhub.download.DownloadProgress;
import com.aries.rn.maskhub.download.DownloadProgressCallback;
import com.aries.rn.maskhub.download.Downloader;
import com.aries.rn.maskhub.download.Request;
import com.aries.rn.maskhub.utils.MaskLog;
import com.aries.rn.maskhub.utils.Md5Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aries.rn.maskhub.Constants.DEFAULT_COMPONENT_NAME;
import static com.aries.rn.maskhub.Constants.JS_BUNDLE_ON_PROGRESS;
import static com.aries.rn.maskhub.Constants.KEY_INTENT_PAGE;
import static com.aries.rn.maskhub.Constants.LOAD_EVENT_DOWNLOADED;
import static com.aries.rn.maskhub.Constants.LOAD_EVENT_JS_BUNDLE_CHECK_VERSION;
import static com.aries.rn.maskhub.Constants.LOAD_EVENT_JS_BUNDLE_DOWNLOADING;
import static com.aries.rn.maskhub.Constants.LOAD_EVENT_RELOAD_INSTALLED;
import static com.aries.rn.maskhub.Constants.LOAD_EVENT_RELOAD_JS_BUNDLE;
import static com.aries.rn.maskhub.Constants.LOAD_EVENT_RE_CREATE;
import static com.aries.rn.maskhub.Constants.PAGE_NAME;

@Route(path = RouterPaths.RN_PAGE)
public class LoadReactActivity extends LazyLoadReactActivity {
    private RootViewReload mReload;
    private ReactJsBundleFactory mReactJsBundleFactory;
    private LoadingDialog loadingDialog;

    private Page mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        Options options = Options.me();

        mReactJsBundleFactory = new ReactJsBundleFactory(this, options.contextDir());
        mReload = new RootViewReload(this, getReactInstanceManager());
        mPage = (Page) getIntent().getParcelableExtra(KEY_INTENT_PAGE);

        // 测试代码
        if (null == mPage) {
            mPage = new Page().name("app");
            mPage.url("rn://app/index.android.jsbundle");
        }
        toEvent(LOAD_EVENT_JS_BUNDLE_CHECK_VERSION, null);
    }

    @Nullable
    @Override
    protected String getMainComponentName() {
        return null;
    }


    private void reloadJSBundle(String jsBundleFile, String componentName) {
        try {
            mReload.setJSBundle(jsBundleFile);
            loadApp(getModuleName(componentName));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private String getModuleName(@Nullable String moduleName) {
        String mainComponentName = null;
        if (null == moduleName) {
            mainComponentName = getMainComponentName();
        }
        if (null == mainComponentName) {
            mainComponentName = DEFAULT_COMPONENT_NAME;
        }
        return mainComponentName;
    }


    private void showLoading() {
        runOnUiThread(() -> {
            if (null != loadingDialog) loadingDialog.show();
        });
    }

    private void dismissLoading() {
        runOnUiThread(() -> {
            if (null != loadingDialog) loadingDialog.dismiss();
        });
    }


    private void initViews() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
    }


    /**
     * bundle更新加载流程:
     * <p>
     * LOAD_EVENT_JS_BUNDLE_CHECK_VERSION           检测版本是否有最新版本
     * LOAD_EVENT_JS_BUNDLE_DOWNLOADING             下载最新版本
     * LOAD_EVENT_DOWNLOADED                        下载完成后执行bundle加载
     * LOAD_EVENT_RELOAD_INSTALLED                  执行部署完成
     */
    private final Handler mEvents = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Options options = Options.me();
            VersionCheckerDelegate checker = options.delegate();
            String name = mPage.name();

            switch (msg.what) {
                case LOAD_EVENT_JS_BUNDLE_CHECK_VERSION: //版本检测
                    Map<String, Object> params = new HashMap<>();
                    params.put(PAGE_NAME, name);
                    BundleEntity lastVersion = checker.lastVersion(name);
                    checker.processCheckResult(params, (code, newResult) -> {
                        boolean hasVersion = checker.hasVersion(lastVersion, newResult);
                        if (hasVersion) {
                            toEvent(LOAD_EVENT_JS_BUNDLE_DOWNLOADING, newResult);
                        } else {
                            MaskLog.d("no version");
                            toEvent(LOAD_EVENT_RELOAD_JS_BUNDLE, checker.lastVersion(name));
                        }
                    });
                    break;
                case LOAD_EVENT_JS_BUNDLE_DOWNLOADING: // 下载最新版本
                    BundleEntity bundleData = (BundleEntity) msg.obj;
                    String url = bundleData.getUrl();
                    Request request = new Request.Builder().url(url).build();
                    Downloader mDownloader = new Downloader(LoadReactActivity.this);
                    mDownloader.request(request).execute(new DownloadProgressCallback() {

                        @Override
                        public void onStarted() {
                            showLoading();
                        }

                        @Override
                        public void onProgress(DownloadProgress progress) {
                            MaskLog.d(progress.toString());
                            sendEvent(JS_BUNDLE_ON_PROGRESS, progress.createWritableMap());
                        }

                        @Override
                        public void onComplete(String downloadFile) {
                            bundleData.setFilePath(downloadFile);
                            String md5 = Md5Utils.getFileMD5(new File(downloadFile));
                            if (Objects.equals(md5, bundleData.getVerifyCode())) {
                                toEvent(LOAD_EVENT_DOWNLOADED, bundleData);
                            } else {
                                MaskLog.w("downloadFile md5 is Bad.");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            MaskLog.e(e);
                            dismissLoading();
                        }
                    });
                    break;
                case LOAD_EVENT_DOWNLOADED: // 下载最新版本
                    BundleEntity result = (BundleEntity) msg.obj;
                    String indexJsBundle = Options.me().contextDir() + mPage.filePath();
                    try {
                        String downloadFile = result.getFilePath();
                        String path = mReactJsBundleFactory.install(downloadFile, name);
                        result.setFilePath(path);
                        MaskLog.w("jsBundle install path:" + path);
//                        String indexJsBundle = new File(path, assetsBundleFileName).getAbsolutePath();
                        reloadJSBundle(indexJsBundle, name);
                        dismissLoading();

                        toEvent(LOAD_EVENT_RELOAD_INSTALLED, result);
                    } catch (IOException e) {
                        MaskLog.e(e);
                    }
                    break;
                case LOAD_EVENT_RELOAD_JS_BUNDLE: //重新加载已经存在的jsbundle
                    reloadJSBundle(Options.me().contextDir() + mPage.filePath(), name);
                    dismissLoading();
                    break;
                case LOAD_EVENT_RELOAD_INSTALLED: // 安装完成
                    checker.putVersion(name, (BundleEntity) msg.obj);
                    break;
                case LOAD_EVENT_RE_CREATE:
                    if (null != mReload) mReload.reload();
                    break;

            }
        }
    };


    /**
     * 发送事件
     *
     * @param what
     * @param obj
     */
    private void toEvent(int what, @Nullable Object obj) {
        Message message = Message.obtain();
        message.what = what;
        message.obj = obj;
        mEvents.sendMessage(message);
    }


    /**
     * @param context 上下文
     * @param page    页面描述信息
     */
    public static void start(Context context, Page page) {
        Intent intent = new Intent(context, LoadReactActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INTENT_PAGE, page);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
