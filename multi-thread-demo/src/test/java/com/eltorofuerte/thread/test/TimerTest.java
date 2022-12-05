package com.eltorofuerte.thread.test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author xinyu.zhang
 * @since 2022/12/5 17:43
 */
public class TimerTest {
    public static void main(String[] args) throws ParseException {
        //        tl();
        clearFile();
    }

    public static void tl() throws ParseException {
        // 1、创建一个定时器
        Timer t = new Timer();
        // 在3秒后执行run中的代码
        // t.schedule(new MyTask(t), 3000);

        String s = "2021-04-15 22:01:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(s);
        // 指定日期执行task，然后每3秒再执行一次
        t.schedule(new MyTask(t), date, 3000);
    }

    static class MyTask extends TimerTask {
        private Timer t;

        public MyTask(Timer t) {
            this.t = t;
        }

        @Override
        public void run() {
            System.out.println("炸弹爆炸");
            // t.cancel(); // 终止定时器
        }
    }

    public static void clearFile() {
        Timer t = new Timer();
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2022-12-05 17:53:20");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        t.schedule(new ClearFileTask(), d);
    }

    static class ClearFileTask extends TimerTask {

        @Override
        public void run() {
            File file = new File("D:\\test");
            deleteFolder(file);
        }

        private void deleteFolder(File folder) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFolder(f);
                    } else {
                        System.out.println(f.getAbsolutePath());
                        f.delete();
                    }
                }
                System.out.println(folder.getAbsolutePath());
                folder.delete();
            }
        }
    }
}
