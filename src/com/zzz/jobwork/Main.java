package com.zzz.jobwork;

import com.mongodb.*;
import com.squareup.okhttp.Response;
import com.zzz.jobwork.dao.TaskModelDAO;
import com.zzz.jobwork.model.TaskConfig;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.model.config.SimpleHttpTaskConfig;
import com.zzz.jobwork.task.SampleHttpTaskListener;
import com.zzz.jobwork.task.TaskThreadPool;

import com.zzz.jobwork.utils.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Morphia;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Main {

    static Logger logger= LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        try {

            MongoClient mongoClient = new MongoClient(new ServerAddress(Configs.Mongo_Host+":"+ Configs.Mongo_Port),
                    Arrays.asList(MongoCredential.createMongoCRCredential(Configs.Mongo_UserName, Configs.Mongo_DB, Configs.Mongo_Password.toCharArray())),
                    new MongoClientOptions.Builder().cursorFinalizerEnabled(false).build());

           // DB db = mongoClient.getDB(Configs.Mongo_DB);
            //db.authenticate(Configs.Mongo_UserName, Configs.Mongo_Password.toCharArray());




           // MongoClient mongoClient = new MongoClient(Configs.Mongo_Host, Configs.Mongo_Port );

           // DB db = mongoClient.getDB( Configs.Mongo_DB );
//            System.out.println("Connect to database successfully");
//            boolean auth = db.authenticate(Configs.Mongo_UserName, Configs.Mongo_Password.toCharArray());
//            System.out.println("Authentication: "+auth);
//            DBCollection coll = db.createCollection("task_queue",null);
//            System.out.println("Collection created successfully");
            Morphia morphia = new Morphia();
            morphia.map(TaskModel.class);
            TaskModelDAO dao=new TaskModelDAO(mongoClient,morphia);


            TaskModel tm1=new TaskModel();
            tm1.setCreatTime(System.currentTimeMillis());
            tm1.setRetry("2");
            tm1.setTitle("dsf");
            tm1.setDescription("dsfsdfdsf");

            //dao.save(tm1);
//
//
//
            TaskModel tm2=new TaskModel();
            tm2.setCreatTime(System.currentTimeMillis());
            tm2.setTitle("谁的空间呵呵");
            tm2.setRetry("9");
            //dao.save(tm2);

           // logger.debug(dao.findAll());
            TaskModel tm3=dao.findById("5475e053269af139bfc62734");
            logger.debug(tm3);
            if(tm3 != null){
                tm3.setTaskConfig("ksdhs-09===");
                dao.save(tm3);
            }

            logger.debug(dao.findById("5475e053269af139bfc62734"));

//            DBCollection coll = db.createCollection("taskConfigs",null);
//            System.out.println("Collection created successfully");
//            DBCollection coll2 = db.getCollection("taskConfigs");
//            System.out.println("Collection mycol selected successfully");
//
//
//            coll.remove(new BasicDBObject("title", "MongoDB"));
//
//
//
//            //coll.find().remove();
//
//
//            BasicDBObject doc = new BasicDBObject("title", "MongoDB").
//                    append("description", "database").
//                    append("likes", 100).
//                    append("url", "http://www.w3cschool.cc/mongodb/").
//                    append("by", "w3cschool.cc");
//            coll2.insert(doc);
//            System.out.println("Document inserted successfully");
//
//
//            DBCursor cursor = coll.find();
//            int i=1;
//            while (cursor.hasNext()) {
//                System.out.println("Inserted Document: "+i);
//                System.out.println(cursor.next());
//                i++;
//            }

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
