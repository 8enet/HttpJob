package com.zzz.jobwork.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.io.Serializable;

/**
 * Created by zl on 2014/11/28.
 */
public abstract class AbsDAO<T>  {



    protected String[][] fieldMap;

    static Logger logger= LogManager.getLogger(AbsDAO.class);

    public abstract BasicDAO<T,Serializable> getBasicDAO();

    protected abstract Query<T> queryByOwner(T t);
}
