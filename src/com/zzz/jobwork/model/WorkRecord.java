package com.zzz.jobwork.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

/**
 * 工作运行记录
 * Created by zl on 2014/11/28.
 */
@Entity
public class WorkRecord implements Serializable{
    @Id
    private String id=new ObjectId().toString();

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 完成时间
     */
    private long doneTime;


    /**
     * 返回状态
     */
    private String status;

    /**
     * 结果
     */
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(long doneTime) {
        this.doneTime = doneTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
