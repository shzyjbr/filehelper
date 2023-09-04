package com.zzk.filehelper;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author kelton
 * @Date 2023/7/1 20:30
 * @Version 1.0
 */
@Slf4j
public class Log4JTest {

    public static void main(String[] args) {
        log.debug("hello");
        log.info("hello");
        log.warn("hello");
        log.error("hello");
        log.trace("hello");
    }
}
