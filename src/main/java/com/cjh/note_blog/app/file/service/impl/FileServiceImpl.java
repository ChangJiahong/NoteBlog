package com.cjh.note_blog.app.file.service.impl;

import com.cjh.note_blog.app.file.model.FileDir;
import com.cjh.note_blog.app.file.model.FileModel;
import com.cjh.note_blog.app.file.service.IFileService;
import com.cjh.note_blog.conf.WebConfig;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.StatusCodeException;
import com.cjh.note_blog.mapper.FileRevMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.FileRev;
import com.cjh.note_blog.utils.DateUtils;
import com.cjh.note_blog.utils.MD5;
import com.cjh.note_blog.utils.WebUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/3
 */

@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    WebConfig webConfig;


    @Autowired
    FileRevMapper fileRevMapper;

    /**
     * 多文件保存
     *
     * @param files 。。
     * @return 。。
     */
    @Override
    public Result<List<FileModel>> saves(List<MultipartFile> files, String email,
                                         String protective, String folderPath) {
        List<FileModel> fileModels = new ArrayList<>();

        files.forEach(multipartFile -> {
            try {
                Result<FileModel> result = save(multipartFile, email, protective, folderPath);
                fileModels.add(result.getData());
            } catch (StatusCodeException e) {
                fileModels.add(FileModel.fail(multipartFile.getOriginalFilename(), "保存失败"));
            }
        });
        return Result.ok(fileModels);
    }

    /**
     * 单个文件保存
     *
     * @param file       。。
     * @param email      。。
     * @param protective 。。
     * @param folderPath 。。
     * @return 。。
     * @throws StatusCodeException 。。
     */
    @Transactional(rollbackFor = {StatusCodeException.class})
    @Override
    public Result<FileModel> save(MultipartFile file, String email,
                                  String protective, String folderPath) throws StatusCodeException {
        String fileName = file.getOriginalFilename();
        String fileId = WebUtils.getUUID();
        String randomName = fileId + fileName.substring(fileName.lastIndexOf("."));
        // 用户相对路径
        String relativePath = "/" + email + webConfig.fileStoragePrefix + "/";
        if (StringUtils.isBlank(folderPath)) {
            relativePath += randomName;
        } else {
            relativePath += folderPath + "/" + randomName;
        }
        try {
            // 根路径加相对路径
            File saveFile = new File(webConfig.fileStorageRootPath + relativePath);
            InputStream fileInput = file.getInputStream();
            FileUtils.copyInputStreamToFile(fileInput, saveFile);
            // 保存相对路径
            FileRev fileRev = insert(fileId, fileName, file.getContentType(), relativePath, email, protective);
            if (fileRev == null) {
                saveFile.delete();
                return Result.ok(FileModel.fail(fileName, "保存失败"));
            }
            FileModel fileModel = FileModel.success(fileName, webConfig.root + "/file/" + fileRev.getFileId());
            return Result.ok(fileModel);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StatusCodeException(StatusCode.IOError, "文件写入失败");
        }
    }


    /**
     * 保存图片
     *
     * @param file  。。
     * @param email 。。
     * @return 。。
     */
    @Override
    public Result<FileModel> saveUserImg(MultipartFile file, String email, String username) {
        if (!file.getContentType().split("/")[0].equals("image")) {
            return Result.fail(StatusCode.ParameterIsInvalid, "文件必须是图片");
        }
        String fileName = MD5.Base64Encode(username);
        String saveFilePath = webConfig.fileStorageRootPath + webConfig.userImgStoragePrefix + "/" + fileName;
        try {
            File saveFile = new File(saveFilePath);
            InputStream fileInput = file.getInputStream();
            FileUtils.copyInputStreamToFile(fileInput, saveFile);
            FileModel fileModel = FileModel.success(fileName, webConfig.root + webConfig.userImgUrlPrefix + "/" + fileName);
            return Result.ok(fileModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok(FileModel.fail(fileName, "保存失败"));
    }

    /**
     * 插入
     *
     * @param name       文件名
     * @param type       文件类型
     * @param path       储存相对路径
     * @param email      。。
     * @param protective 。。
     * @return 。。
     */
    private FileRev insert(String fileId, String name, String type,
                           String path, String email, String protective) {
        FileRev fileRev = new FileRev();
        fileRev.setName(name);
        fileRev.setType(type);
        fileRev.setAuthor(email);
        fileRev.setPath(path);
        fileRev.setProtective(protective);
        fileRev.setFileId(fileId);
        fileRev.setCreated(DateUtils.getNow());

        int i = fileRevMapper.insert(fileRev);
        if (i <= 0) {
            return null;
        }
        return fileRev;
    }


    /**
     * 查找文件
     *
     * @param email  。。
     * @param fileId 文件id
     * @return 。。
     */
    @Override
    public Result<FileRev> selectFile(String email, String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return Result.fail(StatusCode.ParameterIsNull);
        }

        FileRev fileRev = fileRevMapper.selectByPrimaryKey(fileId);
        if (fileRev == null) {
            return Result.fail(StatusCode.DataNotFound, "文件没找到");
        }
        if (FileRev.PRIVATE.equals(fileRev.getProtective())) {
            if (StringUtils.isBlank(email)) {
                // 私有文件，未登录无法下载
                return Result.fail(StatusCode.DataNotFound, "文件没找到");
            }
            if (!email.equals(fileRev.getAuthor())) {
                // 私有文件，非作者无法下载
                return Result.fail(StatusCode.DataNotFound, "文件没找到");
            }
            return Result.ok(fileRev);
        }

        return Result.ok(fileRev);
    }

    /**
     * 获取当前文件夹内容
     *
     * @param folderPath 文件夹路径
     * @return 。。
     */
    @Override
    public Result<List<FileDir>> getFileList(String folderPath, String email) {
        String root = webConfig.fileStorageRootPath +"/"+email + webConfig.fileStoragePrefix;
        File dir = new File(root + "/" + folderPath);
        if (!dir.exists()) {
            return Result.fail(StatusCode.FileDoesNotExist, "文件夹不存在");
        }
        File[] files = dir.listFiles();
        List<FileDir> fileDirs = new ArrayList<>();

        for (File file : files) {
            FileDir fileDir = null;
            if (file.isDirectory()) {
                fileDir = new FileDir();
                fileDir.setType("dir");
                fileDir.setName(file.getName());
                fileDir.setCurrentPath(file.getAbsolutePath().substring(root.length()));
                fileDir.setCount(file.listFiles().length);
            } else {
                String fileName = file.getName();
                String fileId = fileName.substring(0, fileName.lastIndexOf("."));
                // 查询数据库
                FileRev fileRev = fileRevMapper.selectByPrimaryKey(fileId);
                if (fileRev != null) {
                    fileDir = new FileDir(fileRev);
                    fileDir.setLength(file.length());
                }
            }
            if (fileDir != null) {
                fileDirs.add(fileDir);
            }
        }

        return Result.ok(fileDirs);
    }
}
