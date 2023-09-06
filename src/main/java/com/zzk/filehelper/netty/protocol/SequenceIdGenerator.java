package com.zzk.filehelper.netty.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用于做全局增长的id生成器
 */
public class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger(1);

    public static int nextId() {
        return id.incrementAndGet();
    }
}
