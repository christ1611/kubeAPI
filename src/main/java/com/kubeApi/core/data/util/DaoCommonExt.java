package com.kubeApi.core.data.util;

import com.kubeApi.core.data.mapper.OneQonRowMapper;
import com.kubeApi.core.data.util.model.PagingResult;
import com.kubeApi.core.define.CoreErrCode;
import com.kubeApi.core.exception.DBException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;


@Slf4j
@Repository
public class DaoCommonExt
{
    @Getter
    private boolean loggingYn = true;

    @Autowired
    private NamedParameterJdbcTemplate npjt;
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private OneQonRowMapper queryMapper;

    public void setLoggingYn(boolean loggingYn)
    {
        this.loggingYn = loggingYn;
    }

    public <T> List<T> findByDynamicList (String query, HashMap<String, Object> paramMap, Class<T> cls) throws Exception
    {
        if(loggingYn )
        {
            log.debug(query);
            log.debug(paramMap.toString());
        }

        try {
            return npjt.query(query
                                                  , paramMap
                                                  , new BeanPropertyRowMapper<>(cls)
                             );
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public <T> T findByDynamic ( String query, HashMap<String, Object> paramMap,Class<T> cls) throws Exception
    {
        if(loggingYn )
        {
            log.debug(query);
            log.debug("[{}]",paramMap);
        }

        try {
            return npjt.queryForObject(query
                                                    , paramMap
                                                    , new BeanPropertyRowMapper<>(cls)
                                      );
        }
        catch (EmptyResultDataAccessException e) {
              return null;
        }
        catch (IncorrectResultSizeDataAccessException e)
        {
            throw new DBException(CoreErrCode.DB_SELECT_ERROR, e);
        }
    }


    public <T> PagingResult<T> findByDynamicPaging(String query, HashMap<String, Object> paramMap, Class<T> cls , Long pageSize, Long pageNumber) throws Exception
    {
        if(loggingYn)
        {
            log.debug(query);
            log.debug("[{}]",paramMap);
        }

        if(pageSize == null || pageNumber == null) throw new DBException(CoreErrCode.DB_SELECT_ERROR);

        PagingResult<T> rtnList = new PagingResult<T>();

        try
        {
            rtnList.setPageCount( pageSize );

            paramMap.put("pageSize"  , pageSize);
            paramMap.put("pageNumber", pageNumber);

            rtnList.setQueryResult( findByDynamicList( getPagingSql(query), paramMap, cls) );

            if(rtnList.getQueryResult() != null)
                rtnList.setCurrentPage(pageNumber);

        }
        catch (EmptyResultDataAccessException e)
        {
            rtnList.setCurrentPage(-1L);
            rtnList.setQueryResult(null); //
        }

        return rtnList;
    }

    private String getCountSql(String sql) throws Exception
    {
        String prefix = "\nSELECT count(*) as cnt FROM (  \n";
        String postfix = "\n ) ";
        return prefix + sql + postfix;
    }

    private String getPagingSql(String sql) throws Exception
    {
//        String prefix = "\nSELECT * FROM (SELECT ROWNUM AS rowNumber, COUNT(*) OVER() total, A.* FROM ( \n";
//        String postfix = "\n ) A ) WHERE ROWNUM <= :pageSize AND ROW_NUMBER > (:pageNumber-1) * :pageSize\n";

        String prefix = " select s.* , count(*) over() as PG_TOT_CNT from ( ";
        //String postfix = "\n ) A  WHERE ROWNUM < ((:pageNumber * :pageSize) + 1 )) WHERE ROW_NUMBER >= (((:pageNumber-1) * :pageSize) + 1)";
        String postfix = "\n ) s  OFFSET ((:pageNumber-1) * :pageSize) ROWS FETCH NEXT  :pageSize ROWS ONLY ";
        return prefix + sql + postfix;
    }

    public void executeSql(String sql, Map<String,Object> paramMap) throws Exception
    {
        if(loggingYn )
        {
            log.debug(" executeSql [{}] param[{}]", sql, paramMap);
        }

        npjt.execute(sql, paramMap, new PreparedStatementCallback<Boolean>()
        {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }
        });
    }

    public void executeSql(String sql) throws Exception
    {
        if(loggingYn ) log.debug(" executeSql [{}]", sql);

        npjt.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = Exception.class)
    public void immediateCommit(String sql, Map<String,Object> paramMap) throws Exception
    {
        if(loggingYn )
        {
            log.debug(" executeSql [{}] param[{}]", sql, paramMap);
        }

        npjt.execute(sql, paramMap, new PreparedStatementCallback<Boolean>()
        {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                return ps.execute();
            }
        });
    }


//    public <T> T queryForSingleValue(String sql, Map<String, Object> param, Class<T> clazz) throws Exception {
//        Map<String, Object> rtnMap =  findByDynamic(sql, param);
//
//        if(rtnMap == null ) return null;
//
//        final Optional<String> optional = rtnMap.keySet().stream().findFirst();
//        if( optional.isPresent() ) {
//            String key = optional.get();
//            return ObjectUtil.getMapValue(rtnMap, key, clazz);
//        }
//        return null;
//    }
}
