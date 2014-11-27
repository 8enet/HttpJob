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

    @Override
    public void run(){
        final TaskModelDAO<TaskModel> dao= MongoConnectManager.getTaskModelDAO();
        logger.debug(dao.findAll());


            try {
                TaskThreadPool.initWork();
                //TimeUnit.SECONDS.sleep(1);
                //logger.debug(dao.findById("5475e053269af139bfc62734"));
                final TaskModel t=dao.findById("5475e053269af139bfc62734");
                t.setTaskConfig("{\"url\":\"http://api.map.baidu.com/location/ip?ak=E4805d16520de693a3fe707cdc962045&ip=202.198.16.3&coor=bd09ll\",\"proxyPort\":0}");
                //t.setLastRunTime(System.currentTimeMillis());
               // dao.save(t);
                for (int i=0;i<100;i++){
                    TaskThreadPool.addWork(t,null);

                }
//                TaskThreadPool.addWork(t,new SampleHttpTaskListener(){
//                    @Override
//                    public void onSuccess(Response response) {
//                        super.onSuccess(response);
//                        try {
//                            t.setTaskConfig("{\"url\":\"http://api.map.baidu.com/location/ip\",\"proxyPort\":0}");
//                            t.setLastRunTime(System.currentTimeMillis());
//                            dao.save(t);
//                            logger.debug(dao.findById("5475e053269af139bfc62734"));
//                        }catch (Exception e){
//                        }
//                    }
//                });
            }catch (Exception e){
                logger.error(e);
            }finally {
                TaskThreadPool.pushWork();
            }

    }

}
