package com.kubeApi.jdbc.repository;

import com.kubeApi.core.data.util.DaoCommonExt;
import com.kubeApi.jdbc.model.NogtSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DaoNogtSequence {

    private final DaoCommonExt daoCommonExt;

    public NogtSequence findById(String nogtKindCd, String nogtDvCd, String bascDt) throws Exception {
        String sql =
                """
                 SELECT *
                   FROM NOGT_SEQUENCE
                  WHERE NOGT_KIND_CD = :nogtKindCd
                   and NOGT_DV_CD = :nogtDvCd
                   and BASC_DT = :bascDt
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("nogtKindCd", nogtKindCd);
        param.put("nogtDvCd", nogtDvCd);
        param.put("bascDt", bascDt);

        NogtSequence nogtSequence = daoCommonExt.findByDynamic(sql, param, NogtSequence.class);

        log.debug("NogtSequence = [{}]", nogtSequence);

        return nogtSequence;
    }

    public void insert(NogtSequence nogtSequence) throws Exception {
        String sql =
                """
                 INSERT INTO NOGT_SEQUENCE
                    (NOGT_KIND_CD, NOGT_DV_CD, BASC_DT, SEQ_NO, REG_USER_ID, SYS_REG_DTM)
                 VALUES(:nogtKindCd, :nogtDvCd, :bascDt, :seqNo, :regUserId, :sysRegDtm)
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("nogtKindCd", nogtSequence.getNogtKindCd());
        param.put("nogtDvCd", nogtSequence.getNogtDvCd());
        param.put("bascDt", nogtSequence.getBascDt());
        param.put("seqNo", nogtSequence.getSeqNo());
        param.put("regUserId", nogtSequence.getRegUserId());
        param.put("sysRegDtm", nogtSequence.getSysRegDtm());

        daoCommonExt.executeSql(sql, param);
    }

    public void update(NogtSequence nogtSequence) throws Exception {
        String sql =
                """
                 UPDATE NOGT_SEQUENCE
                    SET SEQ_NO = :seqNo
                      , UPD_USER_ID = :updUserId
                      , SYS_UPD_DTM = :sysUpdDtm
                  WHERE NOGT_KIND_CD = :nogtKindCd
                    AND NOGT_DV_CD = :nogtDvCd
                    AND BASC_DT = :bascDt
                """;

        HashMap<String, Object> param = new HashMap<>();
        param.put("nogtKindCd", nogtSequence.getNogtKindCd());
        param.put("nogtDvCd", nogtSequence.getNogtDvCd());
        param.put("bascDt", nogtSequence.getBascDt());
        param.put("seqNo", nogtSequence.getSeqNo());
        param.put("updUserId", nogtSequence.getUpdUserId());
        param.put("sysUpdDtm", nogtSequence.getSysUpdDtm());
        
        daoCommonExt.executeSql(sql, param);
    }
}
