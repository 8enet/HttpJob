package com.zzz.jobwork.model;

import com.sun.istack.internal.Nullable;
import com.zzz.jobwork.model.config.TaskConfigFactory;
import com.zzz.jobwork.utils.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
/**
 * 任务模型
 * Created by zl on 2014/11/25.
 */
@Entity
public class TaskModel extends BaseBean{

    /**
     *
     */
    @Id
    private String id;

    /**
     * 创建时间
     */
    private long creatTime;

    /**
     * 上次运行时间
     */
    private long lastRunTime;


    /**
     * 下一次运行时间
     */
    private long nextRunTime;

    /**
     * 执行次数
     */
    private int runCount;

    /**
     * 状态
     */
    private int state;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 配置
     */
    private String taskConfig;

    /**
     * 类型
     */
    private String type;

    /**
     * 结果比较
     */
    private String resultCompare;

    /**
     * 重试次数
     */
    private String retry;

    /**
     * 权重
     */
    private int weigth;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public long getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(long lastRunTime) {
        this.lastRunTime = lastRunTime;

    }

    public long getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(long nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskConfig() {
        return taskConfig;
    }

    public void setTaskConfig(String taskConfig) {
        this.taskConfig = taskConfig;
    }

    public String getResultCompare() {
        return resultCompare;
    }

    public void setResultCompare(String resultCompare) {
        this.resultCompare = resultCompare;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    public int getWeigth() {
        return weigth;
    }

    public void setWeigth(int weigth) {
        this.weigth = weigth;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TaskConfig getRealTaskConfig(){

        if(StringUtils.isEmpty(type))
            type=TaskConfig.Type.HTTP.name();
        return TaskConfigFactory.getTaskConfig(type,taskConfig);
    }


    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", creatTime=" + creatTime +
                ", lastRunTime=" + lastRunTime +
                ", nextRunTime=" + nextRunTime +
                ", runCount=" + runCount +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskConfig='" + taskConfig + '\'' +
                ", type='" + type + '\'' +
                ", resultCompare='" + resultCompare + '\'' +
                ", retry='" + retry + '\'' +
                ", weigth=" + weigth +
                '}';
    }
}
