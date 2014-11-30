package com.zzzmode.jobwork.model.config;

import com.zzzmode.jobwork.model.TaskConfig;
import com.zzzmode.jobwork.utils.JsonUtil;
import com.zzzmode.jobwork.utils.StringUtils;

/**
 * Created by zl on 2014/11/25.
 */
public class TaskConfigFactory {

    public static TaskConfig getTaskConfig(String type,String config){

        if(!StringUtils.isEmpty(type)){
            if(type.equals(TaskConfig.Type.HTTP.name())){
                return JsonUtil.getGson().fromJson(config,SimpleHttpTaskConfig.class);
            }
        }
        return null;
    }
}
