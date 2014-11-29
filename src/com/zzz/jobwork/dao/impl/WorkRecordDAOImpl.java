package com.zzz.jobwork.dao.impl;

import com.zzz.jobwork.dao.AbsDAO;
import com.zzz.jobwork.dao.MongoConnectManager;
import com.zzz.jobwork.dao.WorkRecordDAO;
import com.zzz.jobwork.model.WorkRecord;
import com.zzz.jobwork.utils.Configs;
import com.zzz.jobwork.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zl on 2014/11/28.
 */
public class WorkRecordDAOImpl extends AbsDAO<WorkRecord> implements WorkRecordDAO {



    static Logger logger= LogManager.getLogger(WorkRecordDAOImpl.class);

    protected BasicDAO baseDAO=new BasicDAO(WorkRecord.class, MongoConnectManager.getMongoClient(),
            MongoConnectManager.getMorphia(), Configs.Mongo_DB);


    @Override
    public String save(WorkRecord workRecord) throws Exception {
        logger.info("save -->"+workRecord);
        return baseDAO.save(workRecord).getId().toString();
    }

    @Override
    public boolean delete(WorkRecord workRecord) throws Exception {
        logger.info("delete -->"+workRecord);
        baseDAO.delete(workRecord);



        return false;
    }



    @Override
    public WorkRecord findById(String id) {
        logger.info("findById -->"+id);


        return  ((Query<WorkRecord>)(baseDAO.createQuery().field("id").equal(id))).get();
    }

    @Override
    public List<WorkRecord> findAll() {
       return baseDAO.createQuery().asList();
    }

    @Override
    public List<WorkRecord> find(WorkRecord workRecord) {
        return null;
    }

    @Override
    public void deleteAll() throws Exception {
        baseDAO.deleteByQuery(baseDAO.createQuery());
    }

    @Override
    public BasicDAO<WorkRecord,Serializable> getBasicDAO() {
        return baseDAO;
    }

    @Override
    protected Query<WorkRecord> queryByOwner(WorkRecord workRecord) {
        Query<WorkRecord> query=null;
        query=baseDAO.createQuery();
        if(workRecord != null){
            if(StringUtils.isEmpty(workRecord.getTaskId())){
                query.filter("taskId", workRecord.getTaskId());
            }
            if(StringUtils.isEmpty(workRecord.getStatus())){
                query.filter("status", workRecord.getStatus());
            }
            if(StringUtils.isEmpty(workRecord.getResult())){
                query.filter("result", workRecord.getResult());
            }
            if(workRecord.getDoneTime() > 0){
                query.filter("doneTime",workRecord.getDoneTime());
            }
        }
        return query;
    }
}
