package com.zzz.jobwork.model;

import com.google.gson.annotations.Expose;

import java.util.concurrent.Callable;

/**
 * 定时任务配置
 * Created by zl on 2014/11/25.
 */
public abstract class TaskConfig<T> extends BaseBean implements Callable<String> {

    public enum Type{
        HTTP,SEND_MSG
    }

    @Expose
    public String result;
    public abstract String format2String();

    public abstract T getConfig();

}
