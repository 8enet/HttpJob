package com.zzz.jobwork.dao;

import com.mongodb.MongoClient;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.utils.Configs;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zl on 2014/11/26.
 */
public class TaskModelDAO extends BasicDAO<TaskModel,Serializable> {


    protected TaskModelDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, Configs.Mongo_DB);
    }

    public TaskModelDAO(MongoClient mongoClient, Morphia morphia) {
        super(mongoClient, morphia, Configs.Mongo_DB);
    }


    public TaskModel findById(String id){
        return createQuery().field("id").equal(id).get();
    }

    public List<TaskModel> findAll(){
        return createQuery().asList();
    }

}
