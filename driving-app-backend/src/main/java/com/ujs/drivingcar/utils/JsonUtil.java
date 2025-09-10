package com.ujs.drivingcar.utils;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 工具类 将json字符串转为map类型
 */
public class JsonUtil {


    /**
     * 将 JSON 字符串数据转换为 Map
     *
     * @param json JSON 字符串
     * @return Map<String, Object>
     * 注：通常以此方法为转换的首要方法，之后对应数据类型采用相应函数进行转换
     */
    public static Map<String, String> toMap(String json) {
        return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
    }

    public static Map<String, Object> toMap1(String json) {
        return new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
    }


    /**
     * 将 JSON 字符串数据转换为 Java Bean
     *
     * @param json   JSON 字符串
     * @param tClass 数据类型的 Class 对象
     * @param <T>    指定的 Java Bean 类型
     * @return T Java Bean 对象
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        return new Gson().fromJson(json, tClass);
    }

    /**
     * 获取 Map 嵌套 Map类型数据
     *
     * @param map   Map<String,Object>对象
     * @param label 要获取 Object 对象在 map 中的键
     * @return Map<String, Object>
     */
    public static Map<String, Object> getMap(Map<String, Object> map, String label) {
        if (map.get(label) instanceof String) {
            throw new JsonUtilException("The result is a String, expect to use getString()");
        } else {
            return (Map<String, Object>) map.get(label);
        }

    }

    /**
     * 获取 List 嵌套 Map 类型数据
     *
     * @param map   Map<String,Object>对象
     * @param label 要获取 Object 对象在 map 中的键
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> getListMap(Map<String, Object> map, String label) {
        if (map.get(label) instanceof String) {
            throw new JsonUtilException("The result is a String, expect to use getString()");
        } else {
            return (List<Map<String, Object>>) map.get(label);
        }

    }

    /**
     * 获取 List 嵌套 List 类型数据
     *
     * @param map    Map<String,Object>对象
     * @param label  要获取 Object 对象在 map 中的键
     * @param tClass 数据类型的 Class 对象
     * @param <T>    内层 List 内的数据类型
     * @return List<List < T>>
     */
    public static <T> List<List<T>> getList(Map<String, Object> map, String label, Class<T> tClass) {
        if (map.get(label) instanceof String) {
            throw new JsonUtilException("The result is a String, expect to use getString()");
        } else {
            return (List<List<T>>) map.get(label);
        }

    }

    /**
     * 获取 Map 对象中的字符串
     *
     * @param map   Map<String,Object>对象
     * @param label 要获取字符串对象在 map 中的键
     * @return String
     * 注：当 Map<String,Object> 中 Object 为字符串时，建议使用此方法
     */
    public static String getString(Map<String, Object> map, String label) {
        return String.valueOf(map.get(label));
    }

    /**
     * 异常类
     */
    public static class JsonUtilException extends RuntimeException {

        public JsonUtilException(String message) {
            super(message);
        }
    }

}