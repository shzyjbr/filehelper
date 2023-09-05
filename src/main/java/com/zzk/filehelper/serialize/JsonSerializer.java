package com.zzk.filehelper.serialize;

import com.alibaba.fastjson2.JSON;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author kelton
 * @Date 2023/8/31 20:55
 * @Version 1.0
 */
public class JsonSerializer implements Serializer{

    private static final Charset utf8 = StandardCharsets.UTF_8;

    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes(utf8);

    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        return JSON.parseObject(bytes, clazz);
    }

    @Override
    public int getCode() {
        return 0;
    }
}
