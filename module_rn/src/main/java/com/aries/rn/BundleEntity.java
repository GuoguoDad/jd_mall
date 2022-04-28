package com.aries.rn;

public class BundleEntity {

    /**
     * 远程下载地址
     */
    private String url;

    /**
     * 本地存储地址
     */
    private String filePath;

    /**
     * 本地版本信息
     */
    private String version;

    /**
     * 文件合法性验证码
     */
    private String verifyCode;

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
