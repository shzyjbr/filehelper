package com.zzk.filehelper.serialize;


/**
 * @author zzk
 * @date 2021/12/8
 * description  自定义序列化器接口
 */
public interface Serializer {
    int KRYO_SERIALIZER = 0;
    int JSON_SERIALIZER = 1;
    int HESSIAN_SERIALIZER = 2;
    int PROTOBUF_SERIALIZER = 3;
    
    int DEFAULT_SERIALIZER = 0;
    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);

    int getCode();

    static Serializer getByCode(int code) {
        switch (code) {
            case HESSIAN_SERIALIZER:
                return new HessianSerializer();
            case JSON_SERIALIZER:
                return new JsonSerializer();

            default:
                return null;
        }
    }
}
