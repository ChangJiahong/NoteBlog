package com.cjh.note_blog.pojo.DO;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/3
 */
public class FileRev {

    public static final String PRIVATE = "private";

    public static final String PUBLIC = "public";

    @Id
    @Column(name = "file_id")
    String fileId;

    String name;

    String type;

    String path;

    String author;

    String protective;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
