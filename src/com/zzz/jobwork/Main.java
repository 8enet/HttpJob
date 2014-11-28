package com.zzz.jobwork;

import com.squareup.okhttp.Response;
import com.zzz.jobwork.dao.MongoConnectManager;
import com.zzz.jobwork.dao.TaskModelDAO;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.task.SampleHttpTaskListener;
import com.zzz.jobwork.task.TaskThreadPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;


public class Main {

    static Logger logger= LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        try {



            TaskThreadPool.scanPool();
            //TaskThreadPool.scanWork(10);
           // for(int i=0;i<1;i++) {
                TaskThreadPool.scanWork(3);
            TaskThreadPool.scanWork(6);
                TaskThreadPool.scanWork(5);
               // TaskThreadPool.scanWork(2);
               // TaskThreadPool.scanWork(1);
           // }

            if(true)
                return;




            SimpleHttpTaskConfig cfg1=new SimpleHttpTaskConfig("http://tieba.baidu.com/f?kw=java");

            cfg1.setDefaultHeader();
            cfg1.setDefaultReferer();


            logger.debug(cfg1.format2String());


            final TaskModelDAO<TaskModel> dao= MongoConnectManager.getTaskModelDAO();
            logger.debug(dao.findAll());

            TaskModel tm3=dao.findById("5475e053269af139bfc62734");
            logger.debug(tm3);
            if(tm3 != null){
                tm3.setTaskConfig(cfg1.format2String());
                System.out.println(dao.save(tm3));
            }
            TaskThreadPool.initWork();

            for (int i=0;i<5;i++){
                        try {
                            TimeUnit.SECONDS.sleep(1);
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

            }

            TaskThreadPool.pushWork();


            try {

                TimeUnit.SECONDS.sleep(3);

            }catch (Exception e){

            }


            TaskThreadPool.initWork();

            for (int i=0;i<5;i++){
                try {
                    TimeUnit.SECONDS.sleep(1);
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

            }

            TaskThreadPool.pushWork();




        }catch (Exception e){

            e.printStackTrace();
        }



        if(true)
            return;


        long s=System.currentTimeMillis();

        TaskModel m1=new TaskModel();

        m1.setTaskConfig("{\"url\":\"http://img.sfcdn.org/2496f390a0e05d3a58d193baa58dd51b9352sdf21ad.png!medium.icon\",\"proxyPort\":0}");


        TaskModel m2=new TaskModel();
        m2.setTaskConfig("{\"url\":\"http://http://api.map.baidu.com/location/ip\",\"proxyPort\":0}");


        TaskModel m3=new TaskModel();
        m3.setTaskConfig("{\"url\":\"http://www.163.com\",\"proxyPort\":0}");


        TaskThreadPool.addWork(m1,null);
        TaskThreadPool.addWork(m2,null);
        TaskThreadPool.addWork(m3,null);


       try {

           for(int i=0;i<5;i++){

               TimeUnit.SECONDS.sleep(3);
               TaskThreadPool.addWork(m3,null);
           }


       }catch (Exception e){

       }

    }
}
