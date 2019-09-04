package com.cjh.note_blog.app.file.service;

import com.cjh.note_blog.app.file.model.FileDir;
import com.cjh.note_blog.app.file.model.FileModel;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.FileRev;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/3
 */
public interface IFileService {
    /**
     * 多文件保存
     *
     * @param files      文件
     * @param email      邮箱
     * @param folderPath 文件夹名
     * @param protective 保护类型
     * @return 。。。
     */
    Result<List<FileModel>> saves(List<MultipartFile> files, String email,
                                  String protective, String folderPath);

    /**
     *  单文件保存
     * @param file 。。
     * @param email 。。
     * @param protective 。。
     * @param folderPath 。。
     * @return 。。
     */
    Result<FileModel> save(MultipartFile file, String email,
                           String protective, String folderPath);

    /**
     * 查找文件
     *
     * @param email 。。
     * @param fileId 。。
     * @return
     */
    Result<FileRev> selectFile(String email, String fileId);

    /**
     * 保存图片
     *
     * @param file 。。。
     * @param email 。。
     * @return
     */
    Result<FileModel> saveUserImg(MultipartFile file, String email, String username);

    /**
     * 获取当前文件夹内容
     * @param folderPath 文件夹路径
     * @return 。。
     */
    Result<List<FileDir>> getFileList(String folderPath, String email);
}
