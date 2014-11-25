package com.zzz.jobwork.model;

/**
 * 任务模型
 * Created by zl on 2014/11/25.
 */
public class TaskModel extends BaseBean{

    /**
     *
     */
    private int id;

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
     * 结果比较
     */
    private String resultCompare;

    /**
     * 重试次数
     */
    private String retry;
}
