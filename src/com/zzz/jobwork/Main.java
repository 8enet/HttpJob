package com.zzz.jobwork;

import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.task.TaskThreadPool;

public class Main {

    public static void main(String[] args) {

        SimpleHttpTaskConfig config1=new SimpleHttpTaskConfig("http://www.baidu.com");

        SimpleHttpTaskConfig config2=new SimpleHttpTaskConfig("http://www.qq.com");

        SimpleHttpTaskConfig config3=new SimpleHttpTaskConfig("http://www.163.com");


        TaskThreadPool.addWork(config1);
        TaskThreadPool.addWork(config2);
        TaskThreadPool.addWork(config3);


        TaskThreadPool.seeWork();
    }
}
