package com.zzz.jobwork;

import com.squareup.okhttp.Response;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.task.OnHttpTaskListener;
import com.zzz.jobwork.task.SampleHttpTaskListener;
import com.zzz.jobwork.task.TaskThreadPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;


public class Main {

    static Logger logger= LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        SimpleHttpTaskConfig config1=new SimpleHttpTaskConfig("http://img.sfcdn.org/2496f390a0e05d3a58d193baa58dd51b9352sdf21ad.png!medium.icon");

        SimpleHttpTaskConfig config2=new SimpleHttpTaskConfig("http://www.qq.com");

        SimpleHttpTaskConfig config3=new SimpleHttpTaskConfig("http://www.163.com");

        config3.setOnHttpTaskListener(new SampleHttpTaskListener(){

            @Override
            public void onSuccess(Response response) {
                super.onSuccess(response);
                logger.debug("onSuccess "+response);

            }

            @Override
            public void onError(Response response, Exception e) {
                super.onError(response, e);
                logger.debug("onError "+response);
            }

            @Override
            public void onRetry(int overRetry) {
                super.onRetry(overRetry);
                logger.debug("onRetry"+overRetry);
            }

            @Override
            public void onFinsh() {
                super.onFinsh();
                logger.debug("onFinsh");
            }
        });



        TaskThreadPool.addWork(config1);
        TaskThreadPool.addWork(config2);
        TaskThreadPool.addWork(config3);


        //TaskThreadPool.seeWork();




        try {
            //Thread.sleep(5000);
            for(int i=0;i<5;i++){
                int r=new Random().nextInt(8)+3;
                logger.debug("暂停"+r+" s");
                Thread.sleep(r*1000);
                TaskThreadPool.addWork(config3);
            }

        }catch (Exception e){

            e.printStackTrace();
        }

    }
}
