package com.cjh.note_blog.app.type.service.Impl;

import com.cjh.note_blog.app.type.service.ITypeService;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.Table;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.mapper.TypeMapper;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Type;
import com.cjh.note_blog.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * ：
 * 标签服务
 *
 * @author ChangJiahong
 * @date 2019/7/19
 */
@Service
public class TypeServiceImpl implements ITypeService {

    @Autowired
    private TypeMapper typeMapper;

    /**
     * 查找tag
     *
     * @param type
     * @return
     */
    @Override
    public Result<Type> selectOne(Type type) {

        if (StringUtils.isBlank(type.getName()) || StringUtils.isBlank(type.getType())) {
            return Result.fail(StatusCode.ParameterIsNull);
        }

        Example example = new Example(Type.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo(Table.Tag.type.name(), type.getType());
        criteria.andEqualTo(Table.Tag.name.name(), type.getName());

        Type type1 = typeMapper.selectOneByExample(example);
        if (type1 != null) {
            // 查找成功
            return Result.ok(type1);
        }

        return Result.fail(StatusCode.DataNotFound);
    }

    /**
     * 创建标签
     *
     * @param type 标签对象
     * @return 统一返回对象
     */
    @Override
    public Result<Type> create(Type type) {
        if (StringUtils.isBlank(type.getName()) || StringUtils.isBlank(type.getType())) {
            // error 参数为空
            return Result.fail(StatusCode.ParameterIsNull,
                    "创建种类标签时，参数不能为空");
        }

        Date now = DateUtils.getNow();
        type.setCreated(now);
        int i = typeMapper.insertUseGeneratedKeys(type);
        if (i <= 0) {
            throw new ExecutionDatabaseExcepeion("新建种类标签失败");
        }
        return Result.ok(type);
    }

    /**
     * 获取种类标签
     *
     * @param val category or tag
     * @return 统一返回对象
     */
    @Override
    public Result<List<Type>> getTypes(String val) {
        if (!StringUtils.equals(val, Type.TAG) && !StringUtils.equals(val, Type.CATEGORY)) {
            // error: 参数无效
            return Result.fail(StatusCode.ParameterIsInvalid);
        }
        Type sleType = new Type();
        sleType.setType(val);
        List<Type> types = typeMapper.select(sleType);
        if (types == null || types.isEmpty()) {
            // error: 没找到
            return Result.fail(StatusCode.DataNotFound);
        }
        return Result.ok(types);
    }
}
