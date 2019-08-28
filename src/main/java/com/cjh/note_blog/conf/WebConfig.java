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

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }
}
