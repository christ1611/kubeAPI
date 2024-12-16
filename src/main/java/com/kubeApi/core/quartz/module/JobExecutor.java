package com.kubeApi.core.quartz.module;

import com.kubeApi.core.quartz.iomodel.ScheduleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * Quartz Schedule 에 등록된 Job 을 Spring Batch Job 으로 실행시키기 위한 Executor class.
 */
@Slf4j
@DisallowConcurrentExecution
public class JobExecutor implements Job {

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private JobLauncher jobLauncher;

    public static List<ScheduleJob> jobList = new ArrayList<>();

    // simulate data from db
    public static List<ScheduleJob> getInitAllJobs() {
        return jobList;
    }


    /**
     * Quartz Job 으로 들어온 Parameter 를 Spring Batch Parameter 로 변환하여 Spring Batch Job 실행
     *
     * @param context quartz execution context
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            log.info("[{}] started.", context);
            log.info("[{}] MergedJobDataMap.", context.getMergedJobDataMap());

            String jobName = context.getJobDetail().getKey().getName();
            log.info("[{}] started.", jobName);
            JobParameters jobParameters = new JobParameters(); //BatchHelper.getJobParameters(context);
            log.info("jobParameters - [{}] .", jobParameters);

//            jobParameters.addString(JOB_PARAMETERS_QUARTZ_KEY,
//                      context.getScheduler().getSchedulerName()
//                              + TOKEN + context.getJobDetail().getKey().getGroup()
//                              + TOKEN + context.getJobDetail().getKey().getName())
//                    .addLong("timestamp", System.currentTimeMillis())
//                          .toJobParameters();
            jobLauncher.run(jobLocator.getJob(jobName), jobParameters);
            log.info("[{}] completed.", jobName);


        }
        catch ( NoSuchJobException
              | JobExecutionAlreadyRunningException
              | JobRestartException
              | JobInstanceAlreadyCompleteException
              | JobParametersInvalidException
                e
              )
        {
            log.error("job execution exception! - {}", e.getCause());
            throw new JobExecutionException();
        }
    }
}
