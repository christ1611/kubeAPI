package com.kubeApi.core.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


@Slf4j
@Configuration
@EnableConfigurationProperties(QuartzProperties.class)
public class QuartzConfig
{
    /**
     * Spring Framework 의 Shutdown Hook 설정.
     * Quartz 의 Shutdown 동작을 위임받아 Graceful Shutdown 을 보장.
     * Quartz 의 자체 Shutdown Plugin 을 사용하면 Spring 의 Datasource 가 먼저 Close 되므로,
     * Spring 에게 Shutdown 동작을 위임하여, 상위에서 컨트롤.
     *
     * @param schedulerFactoryBean quartz schedulerFactoryBean.
     * @return SmartLifecycle
     */
    @Bean
    public SmartLifecycle gracefulShutdownHookForQuartz(SchedulerFactoryBean schedulerFactoryBean) {
        return new SmartLifecycle() {
            private boolean isRunning = false;
            private final Logger logger = LoggerFactory.getLogger(this.getClass());
            @Override
            public boolean isAutoStartup() {
                return true;
            }

            @Override
            public void stop(Runnable callback) {
                stop();
                logger.info("Spring container is shutting down.");
                callback.run();
            }

            @Override
            public void start() {
                logger.info("Quartz Graceful Shutdown Hook started.");
                isRunning = true;
            }

            @Override
            public void stop() {
                isRunning = false;
                try
                {
                    logger.info("Quartz Graceful Shutdown... ");
                    schedulerFactoryBean.destroy();
                }
                catch (SchedulerException e)
                {
                    try
                    {
                        logger.info( "Error shutting down Quartz: " + e.getMessage(), e);
                        schedulerFactoryBean.getScheduler().shutdown(false);
                    }
                    catch (SchedulerException ex)
                    {
                        logger.error("Unable to shutdown the Quartz scheduler.", ex);
                    }
                }
            }

            @Override
            public boolean isRunning() {
                return isRunning;
            }

            @Override
            public int getPhase() {
                return Integer.MAX_VALUE;
            }
        };
    }
}