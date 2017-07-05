package com.loosoo100.campus100.beans;

/**
 * 版本信息
 *
 * @author yang
 */
public class VersionInfo {
    // 版本号
    private int version;
    // 版本名称
    private String versionName;
    // 下载链接
    private String url;
    //是否强制更新,1强制更新  0不强制
    private int status;
    //版本描述
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
