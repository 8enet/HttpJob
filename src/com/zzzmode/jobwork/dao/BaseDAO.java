package com.zzzmode.jobwork.dao;

import java.util.List;

/**
 * Created by zl on 2014/11/28.
 */
public interface BaseDAO<T> {
    String save(T t)throws Exception;
    boolean delete(T t)throws Exception;

    T findById(String id);

    List<T> findAll();

    List<T> find(T t);

    void deleteAll() throws Exception;
}
