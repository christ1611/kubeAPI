package com.kubeApi.core.quartz.service;


import com.kubeApi.core.exception.JobException;
import com.kubeApi.core.quartz.iomodel.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.shaded.com.ongres.scram.common.util.Preconditions;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.kubeApi.core.define.CoreErrCode.*;


@Slf4j
@Service
public class ScheduleJobService {

    @Autowired
    private ListableBeanFactory beanFactory;

    @Autowired
    private Scheduler scheduler;


    public List<ScheduleJob> getAllTrigger(ScheduleJob scheduleJob){
        List<ScheduleJob> jobList = new ArrayList<>();
        try {
            JobKey jobKey = new JobKey(scheduleJob.getJobName(),scheduleJob.getJobGroup());
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers){
                ScheduleJob scheduleJobOut = new ScheduleJob();
                this.wrapScheduleJob(scheduleJobOut,scheduler,jobKey,trigger);
                jobList.add(scheduleJobOut);
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    public List<ScheduleJob> getAllJobList(){
        List<ScheduleJob> jobList = new ArrayList<>();
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeySet){
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers){
                    ScheduleJob scheduleJob = new ScheduleJob();
                    this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);
                    jobList.add(scheduleJob);
                }
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return jobList;
    }

    public List<ScheduleJob> getRunningJobList() throws SchedulerException{
        List<JobExecutionContext> executingJobList = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJob> jobList = new ArrayList<>(executingJobList.size());
        for(JobExecutionContext executingJob : executingJobList){
            ScheduleJob scheduleJob = new ScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);
            jobList.add(scheduleJob);
        }
        return jobList;
    }

    public void saveJobTrigger(ScheduleJob scheduleJob) throws Exception {
        Preconditions.checkNotNull(scheduleJob, "job is null");
        JobKey jobKey = new JobKey(scheduleJob.getJobName(),scheduleJob.getJobGroup());
        if (!scheduler.checkExists(jobKey)) {
            //add trigger
            addJob(scheduleJob);
        }
        if (StringUtils.isNotBlank(scheduleJob.getTriggerName()))
            addTrigger(scheduleJob);
    }

    public void updateJob(ScheduleJob scheduleJob) throws Exception{
        checkNotNullJob(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        if (scheduler.checkExists(jobKey)){

            try {
                Job j = beanFactory.getBean(scheduleJob.getBeanName(), Job.class);
                JobDetail jobDetail = JobBuilder.newJob(j.getClass())
                        .withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup())
                        .storeDurably()
                        .build();// Update bean name

                // Update the job in the scheduler
                scheduler.addJob(jobDetail, true);

            }catch (Exception e)
            {
                throw new JobException(NOT_EXIST_BEAN);
            }


        }else
        {
            throw new JobException(NOT_EXIST_JOB);
        }
    }


    public void updateTrigger(ScheduleJob scheduleJob) throws Exception{
        checkNotNullTrigger(scheduleJob);
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
        CronTrigger cronTrigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }

    private void addTrigger(ScheduleJob scheduleJob) throws Exception {
        checkNotNullJob(scheduleJob);
        checkNotNullTrigger(scheduleJob);
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");

        if (StringUtils.isBlank(scheduleJob.getTriggerGroup()))
            scheduleJob.setTriggerGroup(scheduleJob.getJobGroup());

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger != null){
            throw new Exception("trigger already exists!");
        }
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getTriggerName(), scheduleJob.getJobGroup())
                .forJob(new JobKey(scheduleJob.getJobName(),scheduleJob.getJobGroup()))
                .withSchedule(cronScheduleBuilder).build();

        scheduler.scheduleJob(trigger);
    }

    private void addJob(ScheduleJob scheduleJob) throws Exception{
        checkNotNullJob(scheduleJob);


        JobDetail jobDetail = null;
        Job j = beanFactory.getBean(scheduleJob.getBeanName(), Job.class);

        scheduleJob.setJobId( j.getClass().getCanonicalName() );
        jobDetail = JobBuilder.newJob(j.getClass())
                .withIdentity(scheduleJob.getJobName(),scheduleJob.getJobGroup())
                .storeDurably(true)
                .build();
        scheduler.addJob(jobDetail, true); // 'true' allows updating if the job already exists

        // Start the scheduler
        scheduler.start();


    }

    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException{
        checkNotNullJob(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException{
        checkNotNullJob(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    public void pauseTrigger(ScheduleJob scheduleJob) throws SchedulerException{
        checkNotNullTrigger(scheduleJob);
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
        scheduler.pauseTrigger(triggerKey);
    }

    public void resumeTrigger(ScheduleJob scheduleJob) throws SchedulerException{
        checkNotNullTrigger(scheduleJob);
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
        scheduler.resumeTrigger(triggerKey);
    }

    public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException{
        checkNotNullJob(scheduleJob);
        if (StringUtils.isBlank(scheduleJob.getTriggerName()))
        {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.deleteJob(jobKey);
        }
        else
        {
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getTriggerName(), scheduleJob.getTriggerGroup());
            scheduler.unscheduleJob(triggerKey);
        }

    }

    @SuppressWarnings("rawtypes")
    public void runJobOnce(ScheduleJob scheduleJob) throws SchedulerException{
        checkNotNullJob(scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        JobDataMap paramDt = new JobDataMap();
        Map<String, JobParameter<?>> merged = new HashMap<String, JobParameter<?>>();

        scheduleJob.getJobParameters().forEach((k,v)->{
            merged.put(k, new JobParameter(v, String.class));
        });
        JobParameters jpm = new JobParameters(merged);

        paramDt.put("jobParameters", jpm );
        paramDt.put("triggerJobParameters", jpm );

        log.debug("JPM [{}]", jpm);

        scheduler.triggerJob(jobKey, paramDt);
    }

    private void updateJobCronExpression(ScheduleJob scheduleJob) throws SchedulerException
    {
        checkNotNullJob(scheduleJob);
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExpression()), "CronExpression is null");

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }

    private void wrapScheduleJob(ScheduleJob scheduleJob,Scheduler scheduler,JobKey jobKey,Trigger trigger){
        try {
            scheduleJob.setJobName(jobKey.getName());
            scheduleJob.setJobGroup(jobKey.getGroup());

            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            ScheduleJob job = (ScheduleJob)jobDetail.getJobDataMap().get("scheduleJob");

            scheduleJob.setDesc(job.getDesc());
            scheduleJob.setJobId(job.getJobId());
            scheduleJob.setBeanName(job.getBeanName());
            scheduleJob.setTriggerName(trigger.getKey().getName());
            scheduleJob.setTriggerGroup(trigger.getKey().getGroup());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            scheduleJob.setJobStatus(triggerState.name());
            if(trigger instanceof CronTrigger){
                CronTrigger cronTrigger = (CronTrigger)trigger;
                String cronExpression = cronTrigger.getCronExpression();
                scheduleJob.setCronExpression(cronExpression);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void checkNotNullJob(ScheduleJob scheduleJob)
    {
        Preconditions.checkNotNull(scheduleJob, "job is null");
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobName()), "jobName is null");
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getJobGroup()), "jobGroup is null");
    }

    private void checkNotNullTrigger(ScheduleJob scheduleJob)
    {
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getTriggerName()), "triggerName is null");
        Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getTriggerGroup()), "triggerGroup is null");
    }

    public SchedulerMetaData getMetaData() throws SchedulerException
    {
        SchedulerMetaData metaData = scheduler.getMetaData();
        return metaData;
    }

    public String checkJobExist(ScheduleJob scheduleJob) throws SchedulerException
    {
        try
        {
            Job j = beanFactory.getBean(scheduleJob.getBeanName(), Job.class);
        }
        catch (Exception e)
        {
            return  "false";
        }
        return "true";
    }
}
