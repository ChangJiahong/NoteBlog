package com.cjh.note_blog.CSD.Type.controller;

import com.cjh.note_blog.CSD.Type.service.ITypeService;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Type;
import com.cjh.note_blog.pojo.VO.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * :
 * 标签管理
 * @author ChangJiahong
 * @date 2019/8/7
 */

@RestController
@RequestMapping("api/type")
public class TypeController extends BaseController {

    @Autowired
    private ITypeService typeService;

    /**
     * 获取所有标签
     * @return 统一返回对象
     */
    @PassToken
    @GetMapping("/tag")
    public RestResponse getTags() {

        return getTypes(Type.TAG);
    }

    /**
     * 获取所有种类
     * @return 统一返回对象
     */
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
