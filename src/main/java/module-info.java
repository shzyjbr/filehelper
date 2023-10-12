open module com.zzk.filehelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires static lombok;
    requires io.netty.all;
    requires com.alibaba.fastjson2;
    requires rxcontrols;

    exports com.zzk.filehelper;
}