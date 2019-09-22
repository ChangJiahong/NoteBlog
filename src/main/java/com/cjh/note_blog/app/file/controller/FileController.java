package com.cjh.note_blog.app.file.controller;

import com.cjh.note_blog.annotations.AllowHeaders;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.app.file.model.FileDir;
import com.cjh.note_blog.app.file.model.FileModel;
import com.cjh.note_blog.app.file.service.IFileService;
import com.cjh.note_blog.conf.WebConfig;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.FileRev;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.utils.FileReadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.util.List;

/**
 * ：
 * 文件管理
 *
 * @author ChangJiahong
 * @date 2019/7/19
 */

@Api(tags = "文件上传下载接口")
@UserLoginToken
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);


    @Autowired
    IFileService fileService;

    @Autowired
    WebConfig webConfig;

    /**
     * 多文件上传
     *
     * @param files      文件
     * @param folderPath 文件所在的文件夹路径
     * @param protective 文件受保护性
     * @param request    。。。
     * @return 。。。
     */
    @ApiOperation(value = "多文件上传", notes = "一次性上传多个文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "files", value = "文件", dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "folderPath", value = "文件夹路径", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "protective", value = "受保护类型【public】【private】", dataType = "string", paramType = "form")
    })
    @PostMapping("/uploads")
    public RestResponse uploads(@RequestParam("files") List<MultipartFile> files,
                                @RequestParam(value = "folderPath", required = false, defaultValue = "") String folderPath,
                                @RequestParam(value = "protective", defaultValue = FileRev.PUBLIC) String protective,
                                HttpServletRequest request) {

        String email = getEmail(request);

        Result result = fileService.saves(files, email, protective, folderPath);


        return RestResponse.ok(result);
    }

    /**
     * 单文件上传
     *
     * @param file       。。
     * @param folderPath 。。
     * @param protective 。。
     * @param request    。。
     * @return 。。
     */
    @ApiOperation(value = "单文件上传", notes = "一次上传一个文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "file", value = "文件", dataType = "file", paramType = "form"),
            @ApiImplicitParam(name = "folderPath", value = "文件夹路径", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "protective", value = "受保护类型【public】【private】", dataType = "string", paramType = "form")
    })
    @PostMapping("/upload")
    public RestResponse upload(@RequestParam("file") MultipartFile file,
                               @RequestParam("folderPath") String folderPath,
                               @RequestParam(value = "protective", defaultValue = FileRev.PUBLIC) String protective,
                               HttpServletRequest request) {

        String email = getEmail(request);

        Result result = fileService.save(file, email, protective, folderPath);

        return RestResponse.ok(result);
    }


    /**
     * 上传用户头像
     *
     * @param file    。。
     * @param request 。。
     * @return 。。
     */
    @ApiOperation(value = "上传用户头像", notes = "上传用户头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "file", value = "文件", dataType = "file", paramType = "form"),
    })
    @PostMapping("/upuserimg")
    public RestResponse upUserImg(@RequestParam("file") MultipartFile file,
                                  HttpServletRequest request) {
        String email = getEmail(request);
        String username = getUsername(request);
        Result<FileModel> result = fileService.saveUserImg(file, email, username);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /**
     * 文件下载
     *
     * @param fileId   文件id
     * @param request  。。
     * @param response 。。
     * @return 。。
     */
    @ApiOperation(value = "文件下载", notes = "文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "fileId", value = "文件id", dataType = "string", paramType = "path"),
    })
    @AllowHeaders({"Accept-Ranges"})
    @PassToken
    @GetMapping("/download/{fileId}")
    public RestResponse download(@PathVariable(value = "fileId") String fileId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String email = getEmail(request);

        Result<FileRev> result = fileService.selectFile(email, fileId);
        if (result.isSuccess()) {
            try {
                FileRev fileRev = result.getData();
                File file = new File(webConfig.fileStorageRootPath + fileRev.getPath());

                //开始下载位置
                long startByte = 0;
                //结束下载位置
                long endByte = file.length() - 1;
                String range = request.getHeader("Accept-Ranges");
                if (StringUtils.isNotBlank(range) && range.startsWith("bytes=")) {
                    range = range.substring(range.lastIndexOf("=") + 1).trim();
                    String[] ranges = range.split("-");
                    try {
                        //判断range的类型
                        if (ranges.length == 1) {
                            //类型一：bytes=-2343
                            if (range.startsWith("-")) {
                                endByte = Long.parseLong(ranges[0]);
                            }
                            //类型二：bytes=2343-
                            else if (range.endsWith("-")) {
                                startByte = Long.parseLong(ranges[0]);
                            }
                        }
                        //类型三：bytes=22-2343
                        else if (ranges.length == 2) {
                            startByte = Long.parseLong(ranges[0]);
                            endByte = Long.parseLong(ranges[1]);
                        }

                    } catch (NumberFormatException e) {
                        startByte = 0;
                        endByte = file.length() - 1;
                    }
                    //坑爹地方一：看代码
                    response.setHeader("Accept-Ranges", "bytes");
                    //坑爹地方二：http状态码要为206
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                }
                long contentLength = endByte - startByte + 1;
                response.setHeader("Content-Type", fileRev.getType());
                response.setContentType(fileRev.getType());
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileRev.getName(), "UTF-8"));
                //坑爹地方三：Content-Range，格式为
                // [要下载的开始位置]-[结束位置]/[文件总大小]
                response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());


                writeFileToResponseFromStartAndEnd(file, response, startByte, endByte);
                LOGGER.info("【下载文件】：" + fileRev.getName());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return RestResponse.fail(StatusCode.IOError);
        }

        return RestResponse.fail(result);
    }

    /**
     * 遍历文件夹
     *
     * @param folderPath 文件夹路径
     * @param request    。，。
     * @return 。。
     */
    @ApiOperation(value = "浏览文件夹", notes = "浏览文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "folderPath", value = "文件夹路径", dataType = "string", paramType = "form"),
    })
    @GetMapping("/list")
    public RestResponse folderPreview(@RequestParam("folderPath") String folderPath,
                                      HttpServletRequest request) {

        String email = getEmail(request);
        Result<List<FileDir>> result = fileService.getFileList(folderPath, email);

        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    /**
     * 写入文件
     *
     * @param fromFile 源文件
     * @param response 响应
     * @param start 开始字节
     * @param end 结束字节
     */
    private void writeFileToResponseFromStartAndEnd(File fromFile, HttpServletResponse response, long start, long end) {
        long contentLength = end - start + 1;
        LOGGER.info("内容长度：" + contentLength);
        BufferedOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(fromFile, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(start);
            //坑爹地方四：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）要放前面！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
            //不然会会先读取randomAccessFile，造成后面读取位置出错，找了一天才发现问题所在
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                transmitted += len;
            }
            //处理不足buff.length部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            response.setHeader("Content-Length", transmitted + "");
            LOGGER.info("下载完毕：" + start + "-" + end + "：" + transmitted);

        } catch (ClientAbortException e) {
            LOGGER.info("用户停止下载：" + start + "-" + end + "：" + transmitted);
            //捕获此异常表示拥护停止下载
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
