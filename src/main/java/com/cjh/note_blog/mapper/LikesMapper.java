package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.Likes;
import com.cjh.note_blog.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/3
 */
public interface LikesMapper extends MyMapper<Likes> {

    /**
     * 点赞数
     * @param aid
     * @return
     */
    Integer selectLikesByAid(@Param(value = "aid") Integer aid);

    /**
     * 赞
     * @param aid
     * @return
     */
    int like(@Param(value = "aid") Integer aid);

    /**
     * 取消赞
     * @param aid
     * @return
     */
    int unLike(@Param(value = "aid") Integer aid);
}
