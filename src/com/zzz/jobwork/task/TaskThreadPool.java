package com.zzz.jobwork.task;

import com.squareup.okhttp.Response;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * Created by zl on 2014/11/25.
 */
public class TaskThreadPool {

    private static Logger logger= LogManager.getLogger(TaskThreadPool.class);

    private static final int CORE_THREAD=100; //核心线程
    private static final int MAX_THREAD=200;  //最多线程
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



    private static CopyOnWriteArrayList<Callable> waitWork=new CopyOnWriteArrayList<>();

    /**
     * 加入队列
     * @param callable
     */
    public static void addWork(Callable callable){
        logger.debug("add work:"+callable);
        waitWork.add(callable);

    }

    /**
     * 推送到线程池执行
     */
    public static void pushWork(){
        int i=0;
        //如果当前线程池繁忙  先把任务加入队列里面,避免线程池超时，崩溃
        ThreadPoolExecutor tp= (ThreadPoolExecutor) threadPool;
        int a=100-tp.getActiveCount()+5;
        if(!waitWork.isEmpty()){
            for(Callable callable:waitWork){
                i++;
                completionService.submit(callable);
                waitWork.remove(callable);
                if(i > a){
                    break;
                }
            }
        }

    }

    /**
     * 初始化当前队列
     */
    public static void initWork(){


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
            logger.error(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    public static void scanPool(){
        //注册观察线程池
        service.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        pushWork();
                        ThreadPoolExecutor tp= (ThreadPoolExecutor) threadPool;
                        logger.error("ThreadPool ActiveCount :" + tp.getActiveCount());
                        logger.error("ThreadPool PoolSize : " + tp.getPoolSize());
                        logger.error("ThreadPool CompletedTaskCount : " + tp.getCompletedTaskCount());
                        logger.error("Current wait queue size:" + waitWork.size());
                        logger.error("  -----------------  ");
                    }
                }, 1,
                3, TimeUnit.SECONDS);
    }

    public static void scanWork(int period){
        //每隔5秒
        service.scheduleAtFixedRate(
                new QueryTask(), 1,
                period, TimeUnit.SECONDS);
    }

}
