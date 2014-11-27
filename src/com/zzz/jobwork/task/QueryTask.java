package com.zzz.jobwork.task;


import com.squareup.okhttp.Response;
import com.zzz.jobwork.dao.MongoConnectManager;
import com.zzz.jobwork.dao.TaskModelDAO;
import com.zzz.jobwork.model.TaskModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by zl on 14/11/27.
 */
public class QueryTask implements Runnable{

    static Logger logger= LogManager.getLogger(QueryTask.class);

    public static final int WAIT_TIME=2*1000;

    private Object object=new Object();
    private boolean run=true;

    public void startScan(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object){
                    while (true){
                        try {
                            object.wait();
                        }catch (Exception e){
                            e.printStackTrace();
                            try {
                                TimeUnit.SECONDS.sleep(2);
                            }catch (Exception e1){
                                e1.printStackTrace();
                            }
                        }finally {

                        }

                    }
                }
            }
        }).start();
    }


    public void execWork(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (object){
                    run();
                    object.notifyAll();
                }
            }
        }).start();


    }


    @Override
    public void run(){
        final TaskModelDAO<TaskModel> dao= MongoConnectManager.getTaskModelDAO();
        logger.debug(dao.findAll());
        TaskThreadPool.initWork();

            try {
                //TimeUnit.SECONDS.sleep(1);
                //logger.debug(dao.findById("5475e053269af139bfc62734"));
                final TaskModel t=dao.findById("5475e053269af139bfc62734");
                TaskThreadPool.addWork(t,new SampleHttpTaskListener(){
                    @Override
                    public void onSuccess(Response response) {
                        super.onSuccess(response);
                        try {
                            t.setLastRunTime(System.currentTimeMillis());
                            dao.save(t);
                            logger.debug(dao.findById("5475e053269af139bfc62734"));
                        }catch (Exception e){
                        }
                    }
                });
            }catch (Exception e){
            }
        TaskThreadPool.pushWork();
    }

}
