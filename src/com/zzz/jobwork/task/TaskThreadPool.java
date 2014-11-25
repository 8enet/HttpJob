package com.zzz.jobwork.task;

import java.util.concurrent.*;

/**
 * Created by zl on 2014/11/25.
 */
public class TaskThreadPool {
    private static ExecutorService threadPool = Executors.newFixedThreadPool(5);

    private static CompletionService<String> completionService = new ExecutorCompletionService<String>(threadPool);

    public static void addWork(Callable callable){
        completionService.submit(callable);
    }


    public static void seeWork(){
        try {
            Future<String> future = null;
            while((future = completionService.take()) != null){
                if(future.get() != null)
                System.out.println(future.get().substring(0,20));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
