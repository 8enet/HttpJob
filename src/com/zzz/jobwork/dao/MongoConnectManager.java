package com.zzz.jobwork.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.utils.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.Morphia;

import java.net.UnknownHostException;
import java.util.Arrays;

import static com.zzz.jobwork.utils.Configs.*;

/**
 * Created by zl on 14/11/27.
 */
public class MongoConnectManager {

    static Logger logger= LogManager.getLogger(MongoConnectManager.class);

    public static  MongoClient getMongoClient() {
        return SingletonHolder.mongoClient.get();
    }


    public static Morphia getMorphia(){
        return SingletonHolder.morphia;
    }

    public static TaskModelDAO getTaskModelDAO(){
        return SingletonHolder.taskModelDAO;
    }

    private static final class SingletonHolder{

        private static final ThreadLocal<MongoClient> mongoClient = new ThreadLocal<MongoClient>() {
            @Override
            protected MongoClient initialValue() {
                try {
                    MongoClient client= new MongoClient(new ServerAddress(Mongo_Host + ":" + Mongo_Port),
                            Arrays.asList(MongoCredential.createMongoCRCredential(Mongo_UserName, Mongo_DB, Mongo_Password.toCharArray())),
                            new MongoClientOptions.Builder().connectTimeout(20*1000).cursorFinalizerEnabled(false).build());
                    logger.debug("MongoClient connect & auth  success");
                    return client;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    logger.debug("MongoClient connect | auth  fail !!");
                }
                return null;
            }
        };
        private static final Morphia morphia=new Morphia();
        private static final TaskModelDAO<TaskModel> taskModelDAO=new TaskModelMongoDAOImpl();

    }

}
