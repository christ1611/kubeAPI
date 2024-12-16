package com.kubeApi.core.config;
/*
 * Copyright WINDCOZEE (c) 2017.
 */

import com.kubeApi.core.transaction.OneQTransactionAttributeSource;
import com.kubeApi.core.transaction.OneQTransactionInterceptor;
import com.kubeApi.core.transaction.OneQTransactionManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Slf4j
@Aspect
@Configuration
public class TransactionConfig {

    @Autowired @Qualifier("transactionManager")
    //DataConfig에서 주입한 Bean 설정 가져오기
    private OneQTransactionManager transactionManager;

    @Value("#{new Integer('${kubeApi.transaction.defaultTimeoutSec:60}')}")
    private Integer DEFAULT_TIMEOUT;

    @Bean
    public TransactionInterceptor txAdvice() {
        log.debug("txAdvice start.....");
        OneQTransactionAttributeSource source = new OneQTransactionAttributeSource(DEFAULT_TIMEOUT);
        OneQTransactionInterceptor txInterceptor = new OneQTransactionInterceptor();
        txInterceptor.setTransactionAttributeSource(source);
        txInterceptor.setTransactionManager(transactionManager);
        return txInterceptor;
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        log.info("txAdviceAdvisor start.....");

        AspectJExpressionPointcut requiredTx = new AspectJExpressionPointcut();

//        requiredTx.setExpression("execution(public * com.kubeApi..*Controller.*(..))"); //kubeApi 패키지 하위의 모든 *Controller 의 모든 public 함수
        requiredTx.setExpression("execution(public * com.kubeApi..service.*.*(..))"); //kubeApi 하위 service 패키지의 모든 public 함수

        return new DefaultPointcutAdvisor(requiredTx, txAdvice());
    }
}