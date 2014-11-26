package com.zzz.jobwork.task;

import com.squareup.okhttp.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * Created by zl on 2014/11/25.
 */
public class TaskThreadPool {

    private static Logger logger= LogManager.getLogger(TaskThreadPool.class);

    private static final int CORE_THREAD=10; //核心线程
    private static final int MAX_THREAD=50;
    private static final long TIME_OUT_THREAD=10;  //超时  10分钟

    private static ExecutorService threadPool =  new ThreadPoolExecutor(CORE_THREAD, Integer.MAX_VALUE,
            TIME_OUT_THREAD, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());

    private static CompletionService<Response> completionService = new ExecutorCompletionService<Response>(threadPool);

    public static void addWork(Callable callable){
        logger.debug("add work:"+callable);
        completionService.submit(callable);
    }


    public static void seeWork(){
        try {
            Future<Response> future = null;
            while((future = completionService.take()) != null){
                if(future.get() != null)
                logger.debug(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
