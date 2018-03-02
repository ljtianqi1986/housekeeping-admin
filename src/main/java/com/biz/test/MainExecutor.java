package com.biz.test;
import org.springframework.core.task.TaskExecutor;
/**
 * Created by 刘佳佳 on 2016/8/17.
 */
public class MainExecutor {
    private TaskExecutor taskExecutor;


    public MainExecutor (TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    public void printMessages() {
        for(int i = 0; i < 25; i++) {
            taskExecutor.execute(new MessagePrinterTask("Message" + i));
        }
    }


    private class MessagePrinterTask implements Runnable {
        private String message;
        public MessagePrinterTask(String message) {
            this.message = message;
        }
        public void run() {
            System.out.println(message);
        }
    }
}
