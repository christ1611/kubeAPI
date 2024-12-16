package com.kubeApi.core.quartz;

import com.kubeApi.core.quartz.iomodel.ScheduleJob;
import com.kubeApi.core.quartz.service.ScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/quartz")
@RestController
public class QuartzController {



    @Autowired
    private ScheduleJobService scheduleJobService;


    @RequestMapping("/metaData")
    public Object metaData() throws SchedulerException
    {
        SchedulerMetaData metaData = scheduleJobService.getMetaData();

        return metaData;
    }

    @RequestMapping("/getAllJobs")
    public Object getAllJobs() throws SchedulerException
    {
        List<ScheduleJob> jobList = scheduleJobService.getAllJobList();

        return jobList;
    }


    @RequestMapping("/getTriggers")
    public List<ScheduleJob> getTriggers(@RequestBody ScheduleJob job) throws SchedulerException
    {
        List<ScheduleJob> jobList = scheduleJobService.getAllTrigger(job);

        return jobList;
    }

    @RequestMapping("/getRunningJobs")
    public Object getRunningJobs() throws SchedulerException
    {
        List<ScheduleJob> jobList = scheduleJobService.getRunningJobList();

        return jobList;
    }

    @RequestMapping(value = "/pauseJob", method = {RequestMethod.GET, RequestMethod.POST})
    public String pauseJob( @RequestBody ScheduleJob job) throws SchedulerException
    {
        log.info("params, job = {}", job);

        scheduleJobService.pauseJob(job);

        return "OK";
    }

    @RequestMapping(value = "/resumeJob", method = {RequestMethod.GET, RequestMethod.POST})
    public String resumeJob( @RequestBody ScheduleJob job) throws SchedulerException
    {
        log.info("params, job = {}", job);

        scheduleJobService.resumeJob(job);

        return "OK";
    }

    @RequestMapping(value = "/pauseTrigger", method = {RequestMethod.GET, RequestMethod.POST})
    public String pauseTrigger( @RequestBody ScheduleJob job) throws SchedulerException
    {
        log.info("params, job = {}", job);

        scheduleJobService.pauseTrigger(job);

        return "OK";
    }

    @RequestMapping(value = "/resumeTrigger", method = {RequestMethod.GET, RequestMethod.POST})
    public String resumeTrigger( @RequestBody ScheduleJob job) throws SchedulerException
    {
        log.info("params, job = {}", job);

        scheduleJobService.resumeTrigger(job);

        return "OK";
    }

    @RequestMapping(value = "/deleteJob", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteJob( @RequestBody ScheduleJob job) throws SchedulerException
    {
        log.info("params, job = {}", job);

        scheduleJobService.deleteJob(job);

        return "OK";
    }

    @RequestMapping(value = "/runJob", method = {RequestMethod.GET, RequestMethod.POST})
    public String runJob( @RequestBody ScheduleJob job) throws SchedulerException
    {
        log.info("params, job = {}", job);

        scheduleJobService.runJobOnce(job);

        return "OK";
    }

    @RequestMapping(value = "/saveJobTrigger", method = {RequestMethod.GET, RequestMethod.POST})
    public String saveJobTrigger( @RequestBody ScheduleJob job) throws Exception
    {
        log.info("params, job = {}", job);
        //Message message = Message.failure();

        scheduleJobService.saveJobTrigger(job);

        return "OK";
    }

    @RequestMapping(value = "/updateJob", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateJob( @RequestBody ScheduleJob job) throws Exception
    {
        log.info("params, job = {}", job);
        //Message message = Message.failure();

        scheduleJobService.updateJob(job);

        return "OK";
    }

    @RequestMapping(value = "/updateTrigger", method = {RequestMethod.GET, RequestMethod.POST})
    public String updateTrigger( @RequestBody ScheduleJob job) throws Exception
    {
        log.info("params, job = {}", job);
        //Message message = Message.failure();

        scheduleJobService.updateTrigger(job);

        return "OK";
    }

    @RequestMapping(value = "/checkJob", method = {RequestMethod.GET, RequestMethod.POST})
    public String checkJobExist( @RequestBody ScheduleJob job) throws Exception
    {
        log.info("params, job = {}", job);
        return scheduleJobService.checkJobExist(job);
    }

}
