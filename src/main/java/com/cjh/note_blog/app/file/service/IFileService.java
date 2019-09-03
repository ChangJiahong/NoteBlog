package com.cjh.note_blog.app.file.service;

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
     * 保存
     *
     * @param files
     * @return
     */
    Result<List<FileModel>> saves(List<MultipartFile> files, String email, String protective);

    Result<FileModel> save(MultipartFile file, String email, String protective);

    /**
     * 查找文件
     * @param email
     * @param fileId
     * @return
     */
    Result<FileRev> selectFile(String email, String fileId);

    /**
     * 保存图片
     * @param file
     * @param email
     * @return
     */
    Result<FileModel> saveUserImg(MultipartFile file, String email, String username);
}
