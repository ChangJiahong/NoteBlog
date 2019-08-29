package com.cjh.note_blog.utils;


import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.StatusCodeException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * markdown解析器
 *
 * @author ChangJiahong
 * @date 2019/8/28
 */
public class MdParser {

    private static Logger LOOGER = LoggerFactory.getLogger(MdParser.class);

    private static String root = null;

    private static MdParser instance;

    public static MdParser getInstance() {
        Terminal terminal = new Terminal("pandoc -v");
        if (!terminal.awate()) {
            LOOGER.error("没有检测到pandoc组件。可能会导致文章格式转换异常。");
            throw new RuntimeException("pandoc 组件未安装");
        }
        if (instance == null) {
            instance = new MdParser();
        }
        return instance;
    }


    public String md2html(String mdSource) {
        String htmlSource = "";

        long udd = System.currentTimeMillis();
        String root = System.getProperty("user.dir");

        String mdName = root + "/temp/pandoc/" + udd + ".md";
        String htmlName = root + "/temp/pandoc/" + udd + ".html";
        File mdFile = new File(mdName);
        File htmlFile = new File(htmlName);

        try {
            FileUtils.writeStringToFile(mdFile, mdSource, Charset.forName("utf-8"));
            Terminal terminal = new Terminal("pandoc " + mdName + " -o " + htmlName);
            if (terminal.awate()) {
                htmlSource = FileUtils.readFileToString(htmlFile, Charset.forName("utf-8"));
            } else {
                LOOGER.error("markdown 转换失败，请检查pandoc是否已安装");
                throw new StatusCodeException(StatusCode.ServerError, "markdown 转换失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mdFile.delete();
            htmlFile.delete();
        }

        return htmlSource;
    }


}
