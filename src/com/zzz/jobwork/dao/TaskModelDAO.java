package com.zzz.jobwork.dao;

import java.util.List;

/**
 * Created by zl on 14/11/27.
 */
public interface TaskModelDAO<T> {

    String save(T t)throws Exception;

    boolean delete(T t)throws Exception;

    T findById(String id);

    List<T> findAll();

    List<T> find(T t);

}
