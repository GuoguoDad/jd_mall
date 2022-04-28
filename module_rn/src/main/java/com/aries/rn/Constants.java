package com.aries.rn;

public interface Constants {
    String ASSETS_BUNDLE_PREFIX = "assets://";
    int DOWNLOAD_BUFFER_SIZE = 1024 * 256;
    String DEFAULT_JS_BUNDLE_NAME = "index.android.bundle";

    String DOWNLOAD_FILE_NAME = "download.zip";
    String DOWNLOAD_FILE_PATH = "downloadFilePath";
    String INDEX_JS_BUNDLE = "indexJsBundle";
    String UNZIPPED_FOLDER_NAME = "unzipped";
    String BUNDLE_FOLDER_PREFIX = "mask_hub";

    String DEFAULT_COMPONENT_NAME = "app";


    int LOAD_EVENT_JS_BUNDLE_CHECK_VERSION = 0;      // 检测是否有新版本
    int LOAD_EVENT_JS_BUNDLE_DOWNLOADING = 1;        // 下载最新版本
    int LOAD_EVENT_DOWNLOADED = 2;                   // 下载成功
    int LOAD_EVENT_RELOAD_INSTALLED = 3;             // 安装成功
    int LOAD_EVENT_RELOAD_JS_BUNDLE = 4;             // 重新加载本地Bundle
    int LOAD_EVENT_RE_CREATE = -1;                   // 刷新当前窗口view


    String PAGE_NAME = "pageName";
    String KEY_INTENT_PAGE = "page";
    String JS_BUNDLE_ON_PROGRESS = "JsBundle:onProgress";

    int SUCCESS = 1;
    int FAILURE = -1;

}
