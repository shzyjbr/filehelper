package com.zzk.filehelper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @Author kelton
 * @Date 2023/7/1 20:50
 * @Version 1.0
 */

@Slf4j
public class JUnitTest {

    @Test
    public void test(){
        log.debug("hello");
        log.info("hello");
        log.warn("hello");
        log.error("hello");
        log.trace("hello");
        new Thread( ()-> {
            log.debug("hello");
        },"test-thread").start();
    }

}
