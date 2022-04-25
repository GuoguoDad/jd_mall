package com.aries.rn.maskhub.download;


public interface DownloadProgressCallback {


    void onStarted();

    void onProgress(DownloadProgress progress);

    void onComplete(String downloadFile);

    void onError(Throwable e);
}
