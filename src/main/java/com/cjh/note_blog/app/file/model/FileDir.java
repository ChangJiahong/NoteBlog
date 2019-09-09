package com.cjh.note_blog.app.file.model;

import com.cjh.note_blog.pojo.DO.FileRev;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;

import java.util.Date;
import java.util.List;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/4
 */
public class FileDir {

    /**
     * 文件名
     */
    String name;

    /**
     * 文件类型
     */
    String type;

    /* 文件夹类型属性 */
    /**
     * 当前路径
     */
    String currentPath;

    /**
     * 文件夹下文件数量
     */
    Integer count;


    /* 文件类型属性 */
    /**
     * 文件id
     */
    String fileId;


    String path;

    String author;

    String protective;
    /**
     * 文件长度
     */
    Long length = 0L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date created;


    public FileDir(){}

    public FileDir(FileRev fileRev){
        this.name = fileRev.getName();
        this.fileId = fileRev.getFileId();
        this.type = fileRev.getType();
        this.author = fileRev.getAuthor();
        this.protective = fileRev.getProtective();
        this.created = fileRev.getCreated();
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProtective() {
        return protective;
    }

    public void setProtective(String protective) {
        this.protective = protective;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }
}
