package com.kubeApi.core.quartz.iomodel;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ScheduleJob implements Serializable
{

    private String jobId;

    private String jobName;

    private String jobGroup;

    private String jobStatus;

    private String triggerName;

    private String triggerGroup;

    private String cronExpression;

    private String desc;

//    private String interfaceName;

    private String beanName;

    private Map<String, String> jobParameters;
}
