open module com.zzk.filehelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires com.google.common;
    requires lombok;
    requires io.netty.all;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires io.netty.codec;
    requires com.alibaba.fastjson2;
    requires rxcontrols;

    exports com.zzk.filehelper;
}