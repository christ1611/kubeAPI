package com.kubeApi.core.data.util.model;


import com.kubeApi.core.util.ObjectUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class PagingResult<T> {

    private Long totalCount = 0L;
    private Long currentPage = 0L;
    private Long pageCount = 0L;
    private Long totalPage = 0L;

    List<T> queryResult;

    public PagingResult() {
        queryResult = new ArrayList<>();
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setQueryResult(List<T> queryResult) {
        this.queryResult = queryResult;

        if(queryResult != null && ! queryResult.isEmpty() )
        {
            Object row = queryResult.get(0);
            if(row instanceof PagingRow prow)
            {
                setTotalCount( prow.getPgTotCnt() );
            }
            else if(row instanceof Map mRow)
            {
                Long totCnt = ObjectUtil.getMapValue( mRow, "pgTotCnt", Long.class);
                setTotalCount(totCnt);
            }
        }
    }


    public void setTotalCount(final Long totalCount) {
        if (totalCount != null && pageCount != null) {
            if (pageCount > 0)
                this.totalPage = (long) Math.ceil((float) totalCount / (float) pageCount);
        }
        this.totalCount = totalCount;
    }


    public void setCurrentPage(final Long currentPage) {
        this.currentPage = currentPage;
    }


    public void setPageCount(Long pageCount) {
        if (totalCount != null && pageCount != null) {
            if (pageCount > 0)
                this.totalPage = (long) Math.ceil((float) totalCount / (float) pageCount);
        }
        this.pageCount = pageCount;
    }
}
