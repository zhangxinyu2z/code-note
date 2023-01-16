package com.eltorofuerte.thread.test;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author xinyu.zhang
 * @since 2022/12/9 16:14
 */
public class OtherTest {
    @Test
    public void test() {
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(now);
    }

    @Test
    public void testSync() throws ExecutionException, InterruptedException {
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(() -> {
            System.out.println("do something....");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "result";
        });


        CompletableFuture<String> cf2 = cf.thenApply((result) -> {
            System.out.println(Thread.currentThread() + " cf2 do something-----" + result);
            return result + 1;
        });

        //等待任务执行完成
        System.out.println("结果->" + cf.get());
        System.out.println("结果->" + cf2.get());
    }

    private final ExecutorService executor = Executors.newFixedThreadPool(30);

    @Test
    public void testCompleteSync() {
        List<? extends Number> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7.8, 9, 10, 11, 12, 13, 14, 15);

        List<CompletableFuture<String>> futureList = numbers.stream().map(webLink -> CompletableFuture.supplyAsync(() -> {
            try {
                return download(webLink);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "";
        }, executor)).collect(Collectors.toList());
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));

    }

    private String download(Number webLink) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return "hello" + webLink;
    }

    @Test
    public void tl() throws ExecutionException, InterruptedException {

//        CompletableFuture.supplyAsync(()-> "topOne").thenCompose(CompletableFuture.supplyAsync(s-> s + "  topTwo"));

        ArrayList<String> strings = new ArrayList<>();

        CompletableFuture<Void> c1 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                strings.add("hello");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        CompletableFuture<Void> c2 = c1.thenCompose(value -> CompletableFuture.runAsync(() -> {
            String s = strings.get(0) + "dafdadfasadfsa";
            strings.add(s);
        }));

        c2.get();

        System.out.println(strings);

        //            CompletableFuture<CompletableFuture<String>> future2 = getLastOne().thenApply(s -> getLastTwo(s));
//            System.out.println(future2.get().get());
        }




    public  CompletableFuture<String> getLastTwo(String s){
        return CompletableFuture.supplyAsync(()-> s + "  topTwo");
    }
}
