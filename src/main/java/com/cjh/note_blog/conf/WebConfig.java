package com.cjh.note_blog.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/8/28
 */
@Component      //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "noteblog")  // 配置文件中的前缀
public class WebConfig {

    public String root;

    /**
     * 保存的文件根路径
     */
    public String fileStorageRootPath;

    /**
     * 用户图片前缀
     */
    public String userImgUrlPrefix;

    /**
     * 用户图片存储前缀
     */
    public String userImgStoragePrefix;

    public String fileStoragePrefix;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getFileStorageRootPath() {
        return fileStorageRootPath;
    }

    public void setFileStorageRootPath(String fileStorageRootPath) {
        this.fileStorageRootPath = fileStorageRootPath;
    }

    public String getUserImgUrlPrefix() {
        return userImgUrlPrefix;
    }

    public void setUserImgUrlPrefix(String userImgUrlPrefix) {
        this.userImgUrlPrefix = userImgUrlPrefix;
    }

    public String getUserImgStoragePrefix() {
        return userImgStoragePrefix;
    }

    public void setUserImgStoragePrefix(String userImgStoragePrefix) {
        this.userImgStoragePrefix = userImgStoragePrefix;
    }

    public String getFileStoragePrefix() {
        return fileStoragePrefix;
    }

    public void setFileStoragePrefix(String fileStoragePrefix) {
        this.fileStoragePrefix = fileStoragePrefix;
    }
}
