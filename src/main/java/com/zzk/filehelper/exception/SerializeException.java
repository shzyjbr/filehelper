package com.zzk.filehelper.exception;

/**
 * @author zzk
 * @date 2021/12/10
 * description 自定义RPC序列化异常
 */
public class SerializeException extends RuntimeException {
    public SerializeException() {
    }

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }
    
}
