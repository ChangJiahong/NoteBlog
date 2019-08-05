package com.cjh.note_blog.CSD.Tag.service.Impl;

import com.cjh.note_blog.CSD.Tag.service.ITagService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.Table;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.mapper.TagMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Tag;
import com.cjh.note_blog.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * ：
 * 标签服务
 * @author ChangJiahong
 * @date 2019/7/19
 */
@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private TagMapper tagMapper ;

    /**
     * 查找tag
     *
     * @param tag
     * @return
     */
    @Override
    public Result select(Tag tag) {

        if (StringUtils.isBlank(tag.getName()) || StringUtils.isBlank(tag.getType())){
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Example example = new Example(Tag.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo(Table.Tag.type.name(), tag.getType());
        criteria.andEqualTo(Table.Tag.name.name(), tag.getName());

        Tag tag1 = tagMapper.selectOneByExample(example);
        if (tag1 != null){
            // 查找成功
            return Result.ok(tag1);
        }

        return Result.fail(StatusCode.DataNotFound);
    }

    /**
     * 创建标签
     *
     * @param tag
     */
    @Transactional(rollbackFor = {ExecutionDatabaseExcepeion.class})
    @Override
    public void create(Tag tag) {
        if (StringUtils.isBlank(tag.getName()) || StringUtils.isBlank(tag.getType())){
            return ;
        }

        Date now = DateUtils.getNow();
        tag.setCreated(now);
        tagMapper.insertUseGeneratedKeys(tag);
    }

}
