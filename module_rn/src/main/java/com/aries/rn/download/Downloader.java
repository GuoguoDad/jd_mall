package com.aries.rn.download;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.aries.rn.Constants;
import com.aries.rn.utils.MaskLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;


public class Downloader {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    @NonNull
    private final Context mContext;

    @NonNull
    private Request mRequest = null;

//  private Handler mH = new Handler(Looper.getMainLooper());

    public Downloader(@NonNull Context context) {
        this.mContext = context;
    }


    public Downloader request(Request request) {
        this.mRequest = request;
        return this;
    }

    public void execute(DownloadProgressCallback callback) {
        EXECUTOR_SERVICE.execute(() -> {
            HttpURLConnection connection = null;
            BufferedInputStream bin = null;
            FileOutputStream fos = null;
            BufferedOutputStream bout = null;
            File downloadFile;
            try {
                URL downloadUrl = new URL(mRequest.getUrl());
                connection = (HttpURLConnection) (downloadUrl.openConnection());
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && downloadUrl.toString().startsWith("https")) {
//        try {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(new TLSSocketFactory());
//        } catch (Exception e) {
//          throw new MaskHubUnknownException("Error set SSLSocketFactory. ", e);
//        }
                }
                connection.setRequestProperty("Accept-Encoding", "identity");
                bin = new BufferedInputStream(connection.getInputStream());

                long totalBytes = connection.getContentLength();
                long receivedBytes = 0;

                File downloadFolder = new File(mContext.getCacheDir(), Constants.BUNDLE_FOLDER_PREFIX);
                boolean canMkdir = downloadFolder.mkdirs();
                MaskLog.d(downloadFolder.getAbsolutePath() + " mkdir " + canMkdir);
                String path = downloadUrl.getPath();
                String fileName = path.substring(path.lastIndexOf('/'));
//      downloadFile = new File(downloadFolder, DOWNLOAD_FILE_NAME);
                downloadFile = new File(downloadFolder, TextUtils.isEmpty(fileName) ? Constants.DOWNLOAD_FILE_NAME : fileName);
                fos = new FileOutputStream(downloadFile);
                bout = new BufferedOutputStream(fos, Constants.DOWNLOAD_BUFFER_SIZE);
                byte[] data = new byte[Constants.DOWNLOAD_BUFFER_SIZE];
//                byte[] header = new byte[4];
                callback.onStarted();
                int numBytesRead;
                while ((numBytesRead = bin.read(data, 0, Constants.DOWNLOAD_BUFFER_SIZE)) >= 0) {
//                    if (receivedBytes < 4) {
//                        for (int i = 0; i < numBytesRead; i++) {
//                            int headerOffset = (int) (receivedBytes) + i;
//                            if (headerOffset >= 4) {
//                                break;
//                            }
//                            header[headerOffset] = data[i];
//                        }
//                    }

                    receivedBytes += numBytesRead;
                    bout.write(data, 0, numBytesRead);
                    callback.onProgress(new DownloadProgress(totalBytes, receivedBytes));
//          final long received = receivedBytes;
//          mH.post(() -> callback.onProgress(new DownloadProgress(totalBytes, received)));
                }
                fos.flush();
                bout.flush();
                if (totalBytes == receivedBytes) {
                    callback.onComplete(downloadFile.getAbsolutePath());
                } else {
                    callback.onError(new RuntimeException("Received " + receivedBytes + " bytes, expected " + totalBytes));
//          throw e;
                }
            } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                callback.onError(e);
            }//      throw new MaskHubUnknownException("Error set SSLSocketFactory. ", e);
            finally {
                try {
                    if (null != bout) bout.close();
                    if (null != fos) fos.close();
                    if (null != bin) bin.close();
                    if (null != connection) connection.disconnect();
                } catch (IOException e) {
                    throw new RuntimeException("Error closing IO resources.", e);
                }
            }
        });
    }
}
