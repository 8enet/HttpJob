package com.zzz.jobwork.model.config;

import com.zzz.jobwork.model.TaskConfig;
import com.zzz.jobwork.utils.StringUtils;

/**
 * Created by zl on 2014/11/25.
 */
public class TaskConfigFactory {

    public static TaskConfig getTaskConfig(String type){

        if(!StringUtils.isEmpty(type)){
            if(type.equals(TaskConfig.Type.HTTP.name())){
                return new SimpleHttpTaskConfig();
            }
        }
        return null;
    }
}
