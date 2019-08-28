package com.cjh.note_blog.app.type.controller;

import com.cjh.note_blog.app.type.service.ITypeService;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Type;
import com.cjh.note_blog.pojo.VO.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * :
 * 标签管理
 * @author ChangJiahong
 * @date 2019/8/7
 */
@Api( tags = "标签管理接口")
@RestController
@RequestMapping("api/type")
public class TypeController extends BaseController {

    @Autowired
    private ITypeService typeService;

    /**
     * 【公有接口】
     * 【个人用户权限】
     * 获取所有标签
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取所有标签", notes = "获取所有标签")
    @PassToken
    @GetMapping("/tag")
    public RestResponse getTags() {

        return getTypes(Type.TAG);
    }

    /**
     * 【公有接口】
     * 【个人用户权限】
     * 获取所有种类
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取所有种类", notes = "获取所有种类")
    @PassToken
    @GetMapping("/category")
    public RestResponse getCategorys() {

        return getTypes(Type.CATEGORY);
    }

    /**
     * 获取标签种类
     * @param val tag or category
     * @return 统一返回对象
     */
    private RestResponse getTypes(String val) {
        Result result = typeService.getTypes(val);
        if (!result.isSuccess()) {
            return RestResponse.fail(result);
        }

        return RestResponse.ok(result);
    }



}
