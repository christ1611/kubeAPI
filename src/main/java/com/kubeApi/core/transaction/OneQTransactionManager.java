package com.kubeApi.core.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;


@Slf4j
public class OneQTransactionManager extends DataSourceTransactionManager {

    public OneQTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        log.debug("Transaction Begin [{}][{}]", definition.getName(), definition.getPropagationBehavior());

        super.doBegin(transaction, definition);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        log.debug("Transaction doRollback Start [{}]", status);
        super.doRollback(status);
        //doCleanupAfterCompletion(status.getTransaction());
        log.debug("Transaction doRollback End ");
    }
}
