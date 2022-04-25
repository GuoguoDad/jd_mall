package com.aries.rn.maskhub.download;


import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class DownloadProgress {

  private final long mTotalBytes;
  private final long mReceivedBytes;

  public DownloadProgress(long totalBytes, long receivedBytes) {
    mTotalBytes = totalBytes;
    mReceivedBytes = receivedBytes;
  }

  public boolean isCompleted() {
    return mTotalBytes == mReceivedBytes;
  }

  public WritableMap createWritableMap() {
    WritableMap map = new WritableNativeMap();
    if (mTotalBytes < Integer.MAX_VALUE) {
      map.putInt("totalBytes", (int) mTotalBytes);
      map.putInt("receivedBytes", (int) mReceivedBytes);
    } else {
      map.putDouble("totalBytes", mTotalBytes);
      map.putDouble("receivedBytes", mReceivedBytes);
    }
    return map;
  }

  @Override
  public String toString() {
    return "DownloadProgress{ mTotalBytes=" + mTotalBytes + ", mReceivedBytes=" + mReceivedBytes + '}';
  }
}
