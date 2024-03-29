package com.zzk.filehelper.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zzk
 * @date 2021/11/28
 * description  RPC响应类型定义
 */

@AllArgsConstructor
@Getter
public enum RpcResponseCode {
    SUCCESS(200, "method call success"),
    FAIL(500, "method call fail"),
    NOT_FOUND_METHOD(501, "method not found"),
    NOT_FOUND_CLASS(502, "class not found"),
    ;
    
    private final int code;
    private final String message;


}
