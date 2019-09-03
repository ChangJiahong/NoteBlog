package com.cjh.note_blog.app.file.controller;

import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.app.file.model.FileModel;
import com.cjh.note_blog.app.file.service.IFileService;
import com.cjh.note_blog.conf.WebConfig;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.FileRev;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * ：
 * 文件管理
 *
 * @author ChangJiahong
 * @date 2019/7/19
 */

@UserLoginToken
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);


    @Autowired
    IFileService fileService;

    @PostMapping("/uploads")
    public RestResponse uploads(@RequestParam("files") List<MultipartFile> files,
                                @RequestParam(value = "protective", defaultValue = FileRev.PUBLIC) String protective,
                                HttpServletRequest request) {

        String email = getEmail(request);

        Result result = fileService.saves(files, email, protective);


        return RestResponse.ok(result);
    }

    @PostMapping("/upload")
    public RestResponse upload(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "protective", defaultValue = FileRev.PUBLIC) String protective,
                               HttpServletRequest request) {

        String email = getEmail(request);

        Result result = fileService.save(file, email, protective);

        return RestResponse.ok(result);
    }


    @PostMapping("/upuserimg")
    public RestResponse upUserImg(@RequestParam("file") MultipartFile file,
                                  HttpServletRequest request){
        String email = getEmail(request);
        String username = getUsername(request);
        Result<FileModel> result = fileService.saveUserImg(file, email, username);
        if (!result.isSuccess()){
            return RestResponse.fail(result);
        }
        return RestResponse.ok(result);
    }

    @PassToken
    @GetMapping("/download/{fileId}")
    public RestResponse download(@PathVariable(value = "fileId") String fileId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String email = getEmail(request);

        Result<FileRev> result = fileService.selectFile(email, fileId);
        if (result.isSuccess()){
            try {
                FileRev fileRev = result.getData();
                File file = new File(fileRev.getPath());
                response.setHeader("content-type", fileRev.getType());
                response.setContentType(fileRev.getType());
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileRev.getName(),"UTF-8"));
                FileUtils.copyFile(file, response.getOutputStream());
                LOGGER.info("【下载文件】："+fileRev.getName());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return RestResponse.fail(StatusCode.IOError);
        }

        return RestResponse.fail(result);
    }



}
