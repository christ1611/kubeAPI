package com.kubeApi.core.transaction;


import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;

@Slf4j
public class OneQTransactionAttributeSource implements TransactionAttributeSource, Serializable {

    private final Integer DEFAULT_TIMEOUT;

    private final TransactionAttribute DEFAULT_TRANSACTION_ATTRIBUTE ;

    public OneQTransactionAttributeSource(Integer defaultTimeout)
    {
        if(defaultTimeout == null || defaultTimeout == 0)
        {
            DEFAULT_TIMEOUT = 600;
        }
        else
        {
            DEFAULT_TIMEOUT = defaultTimeout;
        }
        DEFAULT_TRANSACTION_ATTRIBUTE = getDefaultTransactionAttribute();
    }

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {

        RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
        transactionAttribute.setName("1QTransaction-" + targetClass.getSimpleName());
        transactionAttribute.setPropagationBehavior( PROPAGATION_REQUIRED );//PROPAGATION_REQUIRED

        transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class)));

        Integer txTimeOutSec = 600;

        transactionAttribute.setTimeout(txTimeOutSec);


        log.debug("Transaction Name[{}]  TimeOut[{}][{}]", transactionAttribute.getName(), transactionAttribute.getTimeout(), txTimeOutSec);
        return transactionAttribute;
    }

    private TransactionAttribute getDefaultTransactionAttribute()
    {
        if(DEFAULT_TRANSACTION_ATTRIBUTE == null)
        {
            RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
            transactionAttribute.setName("1QTransaction");
            transactionAttribute.setPropagationBehavior(PROPAGATION_REQUIRED); //현재 트랜잭션이 있으면 그 트랜잭션을 사용하고, 없으면 새로 생성
            //transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
            transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Throwable.class))); //모든 오류에 대해서 RollBack 함
            transactionAttribute.setTimeout(DEFAULT_TIMEOUT);
            return transactionAttribute;
        }
        else
        {
            return DEFAULT_TRANSACTION_ATTRIBUTE;
        }
    }
}
