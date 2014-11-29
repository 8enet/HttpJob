package com.zzz.jobwork.task;

import com.zzz.jobwork.http.HttpResponse;
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

    private static final int CORE_THREAD=Runtime.getRuntime().availableProcessors()*50; //核心线程
    private static final int MAX_THREAD=Runtime.getRuntime().availableProcessors()*80;  //最多线程
    private static final long TIME_OUT_THREAD=5; //超时回收 5分钟
    private static final int SCAN_POOL_STATUS=3; //每隔3秒扫描线程池状态

    private static ThreadPoolExecutor threadPool =  new ThreadPoolExecutor(CORE_THREAD, MAX_THREAD,
            TIME_OUT_THREAD, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
    private static CompletionService<HttpResponse> completionService = new ExecutorCompletionService<>(threadPool);

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
        //如果当前线程池繁忙  先把任务加入队列里面,避免任务超时

        int a=threadPool.getCorePoolSize()-threadPool.getActiveCount()+5;
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

    @Deprecated
    public static void seeWork(){
        try {
            Future<HttpResponse> future = null;
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

    /**
     * 观察线程池状态
     */
    public static void scanPool(){
        service.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        pushWork();

                        logger.error("ThreadPool ActiveCount :" + threadPool.getActiveCount());
                        logger.error("ThreadPool PoolSize : " + threadPool.getPoolSize());
                        logger.error("ThreadPool CompletedTaskCount : " + threadPool.getCompletedTaskCount());
                        logger.error("Current wait queue size:" + waitWork.size());
                        logger.error("  -----------------  ");
                    }
                }, SCAN_POOL_STATUS,
                SCAN_POOL_STATUS, TimeUnit.SECONDS);
    }


    /**
     * 扫描任务并添加到线程池
     * @param period
     */
    public static void scanWork(int period){
        //每隔5秒
        service.scheduleAtFixedRate(
                new QueryTask(), 1,
                period, TimeUnit.SECONDS);
    }

}
