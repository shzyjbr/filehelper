open module com.zzk.filehelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires hessian;
    requires org.slf4j;
    requires logback.classic;
    requires logback.core;
    requires com.google.common;
    requires lombok;
    requires io.netty.all;
    requires com.alibaba.fastjson2;
    requires org.apache.commons.lang3;
    requires rxcontrols;

    exports com.zzk.filehelper;
    exports com.zzk.filehelper.controller;
    exports com.zzk.filehelper.controller.component;
}