package com.zzz.jobwork.task;


import com.zzz.jobwork.dao.MongoConnectManager;
import com.zzz.jobwork.dao.TaskModelDAO;
import com.zzz.jobwork.dao.WorkRecordDAO;
import com.zzz.jobwork.http.HttpResponse;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.model.WorkRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zl on 14/11/27.
 */
public class QueryTask implements Runnable{

    static Logger logger= LogManager.getLogger(QueryTask.class);

    private   TaskModelDAO dao= MongoConnectManager.getTaskModelDAO();
    private WorkRecordDAO workRecordDAO=MongoConnectManager.getWorkRecordDAO();

    @Override
    public void run(){


        logger.debug(dao.findAll());
            try {
                TaskThreadPool.initWork();
                //TimeUnit.SECONDS.sleep(1);
                //logger.debug(dao.findById("5475e053269af139bfc62734"));
                final TaskModel t=dao.findById("5475e053269af139bfc62734");
                t.setTaskConfig("{\"url\":\"http://api.map.baidu.com/location/ip?ak=E4805d16520de693a3fe707cdc962045&ip=202.198.16.3&coor=bd09ll\",\"proxyPort\":0}");

                for (int i=0;i<100;i++){
                    TaskThreadPool.addWork(t,new SampleHttpTaskListener(){
                        @Override
                        public void onSuccess(HttpResponse response) {
                            super.onSuccess(response);
                            try {
                                WorkRecord r=new WorkRecord();
                                r.setDoneTime(System.currentTimeMillis());
                                r.setStatus(String.valueOf( response.statCode));
                                r.setResult(response.body);
                                workRecordDAO.save(r);
                            }catch (Exception e){
                                logger.error(e);
                            }

                        }
                    });

                }
            }catch (Exception e){
                logger.error(e);
            }finally {
                TaskThreadPool.pushWork();
            }

    }

}
