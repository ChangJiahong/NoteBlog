package com.cjh.note_blog.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 调用控制台命令
 *
 * @author ChangJiahong
 * @date 2019/8/29
 */
public class Terminal {
    String[] commands;
    Logger LOOGER = LoggerFactory.getLogger(MdParser.class);

    public Terminal(String command) {
        commands = command.split(" ");
    }

    public Boolean awate() {
        Process p = null;
        try {
            p = new ProcessBuilder().command(commands).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            p.waitFor();
            LOOGER.info("执行命令[" + StringUtils.join(commands, " ") + "]\n" + sb.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
