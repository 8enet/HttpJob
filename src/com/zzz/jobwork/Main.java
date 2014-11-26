package com.zzz.jobwork;

import com.mongodb.*;
import com.squareup.okhttp.Response;
import com.zzz.jobwork.model.TaskConfig;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.task.SampleHttpTaskListener;
import com.zzz.jobwork.task.TaskThreadPool;

import com.zzz.jobwork.utils.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Main {

    static Logger logger= LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        try {
            MongoClient mongoClient = new MongoClient(Configs.Mongo_Host, Configs.Mongo_Port );

            DB db = mongoClient.getDB( Configs.Mongo_DB );
            System.out.println("Connect to database successfully");
            boolean auth = db.authenticate(Configs.Mongo_UserName, Configs.Mongo_Password.toCharArray());
            System.out.println("Authentication: "+auth);

            DBCollection coll = db.createCollection("taskConfigs",null);
            System.out.println("Collection created successfully");
            DBCollection coll2 = db.getCollection("taskConfigs");
            System.out.println("Collection mycol selected successfully");


            BasicDBObject doc = new BasicDBObject("title", "MongoDB").
                    append("description", "database").
                    append("likes", 100).
                    append("url", "http://www.w3cschool.cc/mongodb/").
                    append("by", "w3cschool.cc");
            coll2.insert(doc);
            System.out.println("Document inserted successfully");


            DBCursor cursor = coll.find();
            int i=1;
            while (cursor.hasNext()) {
                System.out.println("Inserted Document: "+i);
                System.out.println(cursor.next());
                i++;
            }

        }catch (Exception e){

            e.printStackTrace();
        }









        if(true)
            return;


        long s=System.currentTimeMillis();

        TaskModel m1=new TaskModel();

        m1.setTaskConfig("{\"url\":\"http://img.sfcdn.org/2496f390a0e05d3a58d193baa58dd51b9352sdf21ad.png!medium.icon\",\"proxyPort\":0}");


        TaskModel m2=new TaskModel();
        m2.setTaskConfig("{\"url\":\"http://www.qq.com\",\"proxyPort\":0}");


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
