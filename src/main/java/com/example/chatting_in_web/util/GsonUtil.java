package com.example.chatting_in_web.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

// 自定义 LocalDateTime 适配器（核心：处理字符串与 LocalDateTime 互转）
class LocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {
    // 时间格式与前端保持一致
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(JsonWriter out, LocalDateTime value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            // 序列化：LocalDateTime → 字符串
            out.value(FORMATTER.format(value));
        }
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == com.google.gson.stream.JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        // 反序列化：字符串 → LocalDateTime
        String timeStr = in.nextString();
        return LocalDateTime.parse(timeStr, FORMATTER);
    }
}

public class GsonUtil {

    // 线程安全的 Gson 实例（过滤空值 + 支持 LocalDateTime）
    private static final Gson GSON;
    // 线程安全的 Gson 实例（不过滤空值 + 支持 LocalDateTime）
    private static final Gson GSON_NULL;

    static {
        // 基础配置（复用你原来的逻辑，新增 LocalDateTime 适配器）
        GsonBuilder baseBuilder = new GsonBuilder()
                .enableComplexMapKeySerialization() // 支持复杂MapKey
                .setDateFormat("yyyy-MM-dd HH:mm:ss") // 兼容传统 Date 类型
                .disableHtmlEscaping() // 防止特殊字符乱码
                // 核心：注册 LocalDateTime 适配器，解决字符串转 LocalDateTime 问题
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());

        // 过滤空值的 GSON 实例
        GSON = baseBuilder
                // 注释掉 serializeNulls，即过滤空值
                .create();

        // 不过滤空值的 GSON_NULL 实例
        GSON_NULL = baseBuilder
                .serializeNulls() // 保留空值字段
                .create();
    }

    // 获取过滤空值的 Gson 解析器
    public static Gson getGson() {
        return GSON;
    }

    // 获取不过滤空值的 Gson 解析器
    public static Gson getWriteNullGson() {
        return GSON_NULL;
    }

    /**
     * 根据对象返回json  过滤空值字段
     */
    public static String toJsonStringIgnoreNull(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 根据对象返回json  不过滤空值字段
     */
    public static String toJsonString(Object object) {
        return GSON_NULL.toJson(object);
    }

    /**
     * 将字符串转化为实体对象（支持 LocalDateTime）
     *
     * @param json     源字符串
     * @param classOfT 目标对象类型
     * @param <T>      泛型
     * @return 解析后的实体对象
     */
    public static <T> T strToJavaBean(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * 通用 JSON 转对象（支持复杂类型：List/Map 等，且支持 LocalDateTime）
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * 转成 List 集合（支持包含 LocalDateTime 的实体）
     * @param gsonString JSON 字符串
     * @param cls        集合元素类型
     * @return List 集合
     */
    public static <T> List<T> strToList(String gsonString, Class<T> cls) {
        return GSON.fromJson(gsonString, new TypeToken<List<T>>() {}.getType());
    }

    /**
     * 转成 List<Map> 集合（支持 Map 中包含 LocalDateTime 字符串转对象）
     */
    public static <T> List<Map<String, T>> strToListMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {}.getType());
    }

    /**
     * 转成 Map 集合（支持 Map 中包含 LocalDateTime 字符串转对象）
     */
    public static <T> Map<String, T> strToMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<Map<String, T>>() {}.getType());
    }
}