package com.zzk.filehelper.serialize;


import java.util.HashMap;
import java.util.Map;

/**
 * @Author kelton
 * @Date 2023/8/31 21:21
 * @Version 1.0
 */
public class SerializerFactory {

    private static final Map<Integer, Serializer> serializerMap = new HashMap<>();

    static {
        serializerMap.put(Serializer.JSON_SERIALIZER, new JsonSerializer());
        serializerMap.put(Serializer.HESSIAN_SERIALIZER, new HessianSerializer());
    }

    public static Serializer getSerializer(int code) {
        return serializerMap.get(code);
    }


}
