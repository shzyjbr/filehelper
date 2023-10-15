package com.zzk.filehelper.event;



/**
 * @Author kelton
 * @Date 2023/10/1 23:06
 * @Version 1.0
 */
public class EventTest {

    private static EventBus eventBus = new EventBus("filehelper");
    public static void main(String[] args) {
        eventBus.register(new RegisterA());
        eventBus.register(new RegisterB());
        eventBus.register(new RegisterC());
        eventBus.post("hello");
        eventBus.post(111);
        eventBus.post(new UpdateFileListViewEvent());


    }

    public static class RegisterA {
        @Subscribe
        public void handle(String msg) {
            System.out.println("A: " + msg);
        }
    }
    public static class RegisterB {
        @Subscribe
        public void handle(Integer msg) {
            System.out.println("B: " + msg);
        }
    }

    public static class RegisterC {
        @Subscribe
        public void handle(UpdateFileListViewEvent msg) {
            System.out.println("C: " + msg.getClass().getName());
        }
    }


}
