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
public class TaskModelMongoDAOImpl implements TaskModelDAO<TaskModel>  {

    private BaseTaskModelDAO baseDAO=new BaseTaskModelDAO(MongoConnectManager.getMongoClient(),MongoConnectManager.getMorphia());

    @Override
    public String save(TaskModel taskModel) throws Exception {
        return baseDAO.save(taskModel).getId().toString();
    }

    @Override
    public boolean delete(TaskModel taskModel) throws Exception {
        baseDAO.delete(taskModel);
        return false;
    }

    @Override
    public TaskModel findById(String id) {

        return  baseDAO.createQuery().field("id").equal(id).get();
    }

    @Override
    public List<TaskModel> findAll() {
        return baseDAO.createQuery().asList();
    }

    @Override
    public List<TaskModel> find(TaskModel taskModel) {
        return null;
    }

    private class BaseTaskModelDAO extends BasicDAO<TaskModel,Serializable>{

        protected BaseTaskModelDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
            super(mongoClient, morphia, dbName);
        }

        protected BaseTaskModelDAO(MongoClient mongoClient, Morphia morphia) {
            super(mongoClient, morphia, Configs.Mongo_DB);
        }
    }

}
