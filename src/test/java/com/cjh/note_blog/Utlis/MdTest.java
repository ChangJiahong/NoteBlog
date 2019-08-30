package com.cjh.note_blog.Utlis;

import com.cjh.note_blog.utils.MarkDown2HtmlWrapper;
import com.cjh.note_blog.pojo.VO.MarkdownEntity;
import com.cjh.note_blog.utils.MdParser;
import com.vladsch.flexmark.Extension;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/8/6
 */
public class MdTest {

    @Test
    public void md2Html() throws UnsupportedEncodingException {
        // 从文件中读取markdown内容
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("README.md");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

        List<String> list = reader.lines().collect(Collectors.toList());
        StringBuffer content = new StringBuffer();

        for (String s : list){
            content.append(s+"\n");
        }

//        String content = Joiner.on("\n").join(list);



        // markdown to image
        MutableDataSet options = new MutableDataSet();
        options.setFrom(ParserEmulationProfile.MARKDOWN);
        options.set(Parser.EXTENSIONS, Arrays.asList(new Extension[] { TablesExtension.create()}));
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        Node document = parser.parse(content.toString());
        String html = renderer.render(document);
        System.out.println(html);
    }


    @Test
    public void md2Html2() throws UnsupportedEncodingException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("README.md");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

        List<String> list = reader.lines().collect(Collectors.toList());
        StringBuffer content = new StringBuffer();

        for (String s : list){
            content.append(s+"\n");
        }
//        String html = MDTool.markdown2Html(content.toString());
//        System.out.println(html);
    }

    @Test
    public void md2Html3() throws UnsupportedEncodingException, FileNotFoundException {
        String file = "tutorial.md";
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("README.md");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));

        List<String> list = reader.lines().collect(Collectors.toList());
        StringBuffer content = new StringBuffer();

        for (String s : list) {
            content.append(s + "\n");
        }
        MarkdownEntity html = MarkDown2HtmlWrapper.ofContent(content.toString());

        PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(new File("D:/text.html"))));
        pw.print(html.toString());
        System.out.println(html.toString());
    }

    @Test
    public void md2html4(){
        String html = MdParser.getInstance().md2html("# 搜索 \n### 手动换行符");
        System.out.println(html);
    }

    @Test
    public void sub(){

        String root = "http://www.pythong.top";

        String frontCoverImgUrl = "http://www.hong.top/img/fsd.png";
        if (frontCoverImgUrl.startsWith(root)) {
            String s = frontCoverImgUrl.substring(root.length());
            System.out.println(s);
        }else {
            System.out.println("fffffff");
        }

        for (int i=0;i<20;i++) {
            int n = new Random().nextInt(19)+1;
            System.out.println(n);
        }
    }

}
