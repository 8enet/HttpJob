package com.zzz.jobwork.task;

import com.squareup.okhttp.Response;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by zl on 2014/11/25.
 */
public class TaskThreadPool {

    private static Logger logger= LogManager.getLogger(TaskThreadPool.class);

    private static final int CORE_THREAD=10; //核心线程
    private static final int MAX_THREAD=50;  //最多线程
    private static final long TIME_OUT_THREAD=5; //超时 5分钟

    private static ExecutorService threadPool =  new ThreadPoolExecutor(CORE_THREAD, MAX_THREAD,
            TIME_OUT_THREAD, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
    private static CompletionService<Response> completionService = new ExecutorCompletionService<>(threadPool);

    public static void addWork(TaskModel taskModel,OnHttpTaskListener listener){
        SimpleHttpTaskConfig config= (SimpleHttpTaskConfig) taskModel.getRealTaskConfig();
        if(config != null){
            if(listener != null){
                config.setOnHttpTaskListener(listener);
            }
            addWork(config);
        }
    }

    private static List<Callable> currentWork=new LinkedList<>();

    /**
     * 加入队列
     * @param callable
     */
    public static void addWork(Callable callable){
        logger.debug("add work:"+callable);
        currentWork.add(callable);

    }

    /**
     * 推送到线程池执行
     */
    public static void pushWork(){
        if(!currentWork.isEmpty()){
            for(Callable callable:currentWork){
                completionService.submit(callable);
            }
        }
    }

    /**
     * 初始化当前队列
     */
    public static void initWork(){
        currentWork.clear();
        currentWork=Collections.synchronizedList(currentWork);
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


    public static void scanWork(int period){
        //每隔5秒

        service.scheduleAtFixedRate(
                new QueryTask(), 1,
                period, TimeUnit.SECONDS);
    }

}
