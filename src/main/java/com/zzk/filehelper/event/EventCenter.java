//package com.zzk.filehelper.event;
//
//import com.google.common.eventbus.EventBus;
//
//public class EventCenter {
//
//    private static EventBus eventBus = new EventBus("filehelper");
//
//    private EventCenter(){}
//
//    public static EventBus getEventBus() {
//        return eventBus;
//    }
//
//    public static void register(Object object){
//        eventBus.register(object);
//    }
//
//    public static void unregister(Object object){
//        eventBus.unregister(object);
//    }
//
//    public static void postMessage(Object object){
//        eventBus.post(object);
//    }
//
//}
