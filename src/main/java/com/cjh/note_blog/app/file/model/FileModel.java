package com.cjh.note_blog.app.file.model;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/3
 */
public class FileModel {
    /**
     * 状态，是否上传成功
     */
    Boolean state;

    String fileName;

    /**
     * 如果上传成功，资源路径
     */
    String url;

    /**
     * 如果上传失败，错误信息
     */
    String errorMsg;

    public FileModel(Boolean state, String url, String errorMsg, String fileName) {
        this.state = state;
        this.url = url;
        this.errorMsg = errorMsg;
        this.fileName = fileName;
    }

    public static FileModel success(String fileName, String url) {
        return new FileModel(true, url, "", fileName);
    }

    public static FileModel fail(String fileName, String errorMsg) {
        return new FileModel(false, "", errorMsg, fileName);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
