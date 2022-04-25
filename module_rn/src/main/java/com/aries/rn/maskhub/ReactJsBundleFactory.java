package com.aries.rn.maskhub;

import android.content.Context;

import com.aries.rn.maskhub.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ReactJsBundleFactory {

    private final Context mContext;
    private final String mDocumentDir;

    public ReactJsBundleFactory(Context context, String rootDir) {
        this.mDocumentDir = rootDir;
        this.mContext = context;
    }

    /**
     * 安装jsbundle文件到工作目录
     *
     * @param jsBundleZipPath
     * @param destJsBundleFileName
     * @return
     * @throws IOException
     */
    public String install(String jsBundleZipPath, String destJsBundleFileName) throws IOException {
        File binFile = new File(jsBundleZipPath);
        String dist = new File(mDocumentDir, destJsBundleFileName).getAbsolutePath();
        File packageFile = new File(dist);
        if (!packageFile.exists()) packageFile.mkdirs();
        String unzippedFolderPath = getUnzippedFolderPath();
        FileUtils.unzipFile(binFile, unzippedFolderPath);
        FileUtils.deleteFileOrFolderSilently(binFile);
        FileUtils.copyDirectoryContents(unzippedFolderPath, dist);
        FileUtils.deleteFileAtPathSilently(unzippedFolderPath);
        return dist;
    }

    public String getContextDir() {
        return mDocumentDir;
    }

    private String getUnzippedFolderPath() {
        String temp = mContext.getCacheDir().getAbsolutePath();
        return FileUtils.appendPathComponent(temp, UUID.randomUUID().toString().replaceAll("-", ""));
    }

}
