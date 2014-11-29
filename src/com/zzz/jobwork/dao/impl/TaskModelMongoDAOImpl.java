package com.zzz.jobwork.dao.impl;

import com.zzz.jobwork.dao.AbsDAO;
import com.zzz.jobwork.dao.MongoConnectManager;
import com.zzz.jobwork.dao.TaskModelDAO;
import com.zzz.jobwork.model.TaskModel;
import com.zzz.jobwork.utils.Configs;
import com.zzz.jobwork.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.io.Serializable;
import java.util.List;

/**
 * 任务模型DAO
 * Created by zl on 2014/11/26.
 */
public class TaskModelMongoDAOImpl extends AbsDAO<TaskModel> implements TaskModelDAO {



    static Logger logger= LogManager.getLogger(TaskModelMongoDAOImpl.class);

    protected BasicDAO baseDAO=new BasicDAO(TaskModel.class, MongoConnectManager.getMongoClient(),
            MongoConnectManager.getMorphia(), Configs.Mongo_DB);

    @Override
    public String save(TaskModel taskModel) throws Exception {
        if(taskModel != null && StringUtils.isEmpty(taskModel.getId())){
            taskModel.setId(new ObjectId().toString());
        }

        logger.info("save -->"+taskModel);
        return baseDAO.save(taskModel).getId().toString();
    }

    @Override
    public boolean delete(TaskModel taskModel) throws Exception {
        logger.info("delete -->"+taskModel);
        baseDAO.delete(taskModel);
        return false;
    }

    @Override
    public TaskModel findById(String id) {
        logger.info("findById -->"+id);
        return  ((Query<TaskModel>)(baseDAO.createQuery().field("id").equal(id))).get();
    }

    @Override
    public List<TaskModel> findAll() {
        return baseDAO.createQuery().asList();
    }

    @Override
    public List<TaskModel> find(TaskModel taskModel) {
        return null;
    }

    @Override
    public void deleteAll() throws Exception {
        baseDAO.deleteByQuery(baseDAO.createQuery());
    }


    @Override
    public BasicDAO<TaskModel,Serializable> getBasicDAO() {
        return baseDAO;
    }

    @Override
    protected Query<TaskModel> queryByOwner(TaskModel taskModel) {
        return null;
    }
}
