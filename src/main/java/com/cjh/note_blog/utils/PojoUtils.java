package com.cjh.note_blog.utils;

import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.StatusCodeException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/9/23
 */
public class PojoUtils {


    /**
     * pojo copy to model
     * 属性以model为准
     *
     * @param pojo
     * @param modelClass
     * @param <T>
     * @return
     */
    public static <T> T copyToModel(Object pojo, Class<T> modelClass) {
        return copyToModelExclude(pojo, modelClass, null);
    }

    public static <T> T copyToModelExclude(Object pojo, Class<T> modelClass, String[] exclude) {
        T model = null;
        try {
            model = modelClass.newInstance();
            copyPojoToModel(pojo, model, exclude);
        } catch (StatusCodeException e) {
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * model copy to pojo
     * 属性以model为准
     *
     * @param model
     * @param pojoClass
     * @param <T>
     * @return
     */
    public static <T> T copyToPojo(Object model, Class<T> pojoClass) {
        return copyToPojoExclude(model, pojoClass, null);
    }

    public static <T> T copyToPojoExclude(Object model, Class<T> pojoClass, String[] exclude) {
        T pojo = null;
        try {
            pojo = pojoClass.newInstance();
            copyModelToPojo(model, pojo, exclude);
        } catch (StatusCodeException e) {
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return pojo;
    }

    /**
     * model copy to pojo
     * 属性以model为准
     *
     * @param model
     * @param pojo
     * @return
     */
    public static void copyModelToPojo(Object model, Object pojo, String[] exclude) throws StatusCodeException {
        copyOTO(pojo, model, true, exclude);
    }

    /**
     * pojo copy to model
     * 属性以model为准
     *
     * @param pojo
     * @param model
     * @return
     */
    public static void copyPojoToModel(Object pojo, Object model, String[] exclude) throws StatusCodeException {
        copyOTO(pojo, model, false, exclude);
    }


    /**
     * basisFirst 如果是ture,属性以第一个对象为准
     *
     * @param obj1
     * @param obj2
     * @param basisFirst
     */
    public static void copyOTO(Object obj1, Object obj2, Boolean basisFirst, String[] exclude) throws StatusCodeException {
        if (obj1 == null || obj2 == null) {
            throw new StatusCodeException(StatusCode.ParameterIsNull);
        }
        String[] names = getModelProperties(basisFirst ? obj1 : obj2, exclude);
        copyPropertiesInclude(obj1, obj2, names);

    }

    private static String[] getModelProperties(Object model, String[] exclude) {
        Method[] methods = model.getClass().getDeclaredMethods();
        List<String> names = new ArrayList<>();
        List<String> excludeList = exclude != null ? Arrays.asList(exclude) : null;
        for (Method method : methods) {
            String fromMethodName = method.getName();
            if (!fromMethodName.startsWith("get")) {
                continue;
            }
            String name = fromMethodName.substring(3).toLowerCase();
            if (excludeList != null && excludeList.size() > 0 && excludeList.contains(name)) {
                continue;
            }
            names.add(name);
        }
        return names.toArray(new String[names.size()]);
    }


    /**
     * 利用反射实现对象之间相同属性复制
     *
     * @param source 要复制的
     * @param target 复制给
     */
    public static void copyProperties(Object source, Object target) {
        copyPropertiesExclude(source, target, null);
    }

    /**
     * 复制对象属性
     *
     * @param from
     * @param to
     * @param excludsArray 排除属性列表
     * @throws Exception
     */
    public static void copyPropertiesExclude(Object from, Object to,
                                             String[] excludsArray) {

        List<String> excludesList = null;

        if (excludsArray != null && excludsArray.length > 0) {
            // 构造列表对象
            excludesList = Arrays.asList(excludsArray);
        }

        Method[] fromMethods = from.getClass().getDeclaredMethods();

        List<String> include = new ArrayList<>();

        for (Method fromMethod : fromMethods) {
            String fromMethodName = fromMethod.getName();
            String propertyName = fromMethodName.substring(3).toLowerCase();
            if (excludesList != null && excludesList.contains(propertyName)) {
                continue;
            }
            include.add(propertyName);
        }
        copyPropertiesInclude(from, to, include.toArray(new String[include.size()]));

    }

    /**
     * 对象属性值复制，仅复制指定名称的属性值
     *
     * @param from
     * @param to
     * @param includsArray
     * @throws Exception
     */
    public static void copyPropertiesInclude(Object from, Object to,
                                             String[] includsArray) {


        List<String> includesList = null;

        if (includsArray != null && includsArray.length > 0) {

            includesList = Arrays.asList(includsArray);

        } else {

            return;
        }

        try {
            Method[] fromMethods = from.getClass().getDeclaredMethods();
            Method[] toMethods = to.getClass().getDeclaredMethods();
            Method fromMethod = null, toMethod = null;
            String fromMethodName = null, toMethodName = null;

            for (int i = 0; i < fromMethods.length; i++) {

                fromMethod = fromMethods[i];
                fromMethodName = fromMethod.getName();

                if (!fromMethodName.contains("get")) {
                    continue;
                }

                // 排除列表检测
                String propertyName = fromMethodName.substring(3).toLowerCase();

                if (!includesList.contains(propertyName)) {
                    continue;
                }

                toMethodName = "set" + fromMethodName.substring(3);
                toMethod = findMethodByName(toMethods, toMethodName);

                if (toMethod == null) {
                    continue;
                }

                Object value = fromMethod.invoke(from);

                if (value == null) {
                    continue;
                }

                // 集合类判空处理
                if (value instanceof Collection) {

                    Collection<?> newValue = (Collection<?>) value;

                    if (newValue.size() <= 0) {
                        continue;
                    }
                }

                toMethod.invoke(to, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从方法数组中获取指定名称的方法
     *
     * @param methods
     * @param name
     * @return
     */
    public static Method findMethodByName(Method[] methods, String name) {
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }
}
