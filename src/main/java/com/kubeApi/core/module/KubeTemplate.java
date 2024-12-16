package com.kubeApi.core.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.kubeApi.core.define.CoreDefine;
import com.kubeApi.core.define.CoreDefine.HttpMethCd;
import com.kubeApi.core.define.CoreErrCode;
import com.kubeApi.core.exception.ApiCallException;
import com.kubeApi.core.exception.KubeException;
import com.kubeApi.core.iomodel.TokenReview;
import com.kubeApi.core.util.RestUtil;
import com.kubeApi.core.util.StringUtil;
import com.kubeApi.jdbc.model.AppTokenInfo;
import com.kubeApi.jdbc.repository.DaoAppTokenInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.kubeApi.apiService.define.KubeDefine.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class KubeTemplate {

    @Value("${kube.uri}")
    private String KUBE_URI;

    @Getter
    @Value("${kube.namespace}")
    private String KUBE_NAMESPACE;

    @Getter
    @Value("${kube.serviceAccount}")
    private String KUBE_SERVICE_ACCOUNT;

    @Value("${kube.tokenVer}")
    private String KUBE_TOKEN_VER;

    private final RestUtil restUtil;

    private final StringUtil stringUtil;

    private final ObjectMapper mapper;

    private final DaoAppTokenInfo daoAppTokenInfo;

    public <T> T callApi(Class<T> valueType, HttpMethCd httpMethCd, String apiUri) throws Exception
    {
        return callApi(valueType, httpMethCd, null, apiUri, null, null, null, null, 10, 120);
    }

    public <T> T callApi(Class<T> valueType, HttpMethCd httpMethCd, String apiUri, String token, String contentType, Object request) throws Exception
    {
        return callApi(valueType, httpMethCd, null, apiUri, token, contentType, request, null, 10, 120);
    }

    public <T> T callApi(Class<T> valueType, HttpMethCd httpMethCd, String apiUri, String contentType, Object request) throws Exception
    {
        return callApi(valueType, httpMethCd, null, apiUri, null, contentType, request, null, 10, 120);
    }

    public <T> T callApi(Class<T> valueType, HttpMethCd httpMethCd, String serverAddr, String apiUri, String token, String contentType, Object request) throws Exception
    {
        return callApi(valueType, httpMethCd, serverAddr, apiUri, token, contentType, request, null, 10, 120);
    }

    public <T> T callApi(Class<T> valueType, HttpMethCd httpMethCd, String apiUri, Object request) throws Exception
    {
        return callApi(valueType, httpMethCd, null, apiUri, null, null, request, null, 10, 120);
    }

    public <T> T callApi(Class<T> valueType, HttpMethCd httpMethCd, String apiUri, Object request,
                         int connectionTimeout, int readTimeout) throws Exception {
        return callApi(valueType, httpMethCd, null, apiUri, null, null, request, null, connectionTimeout, readTimeout);
    }

    public <T> T callApi(Class<T> valueType, HttpMethCd httpMethCd, String serverAddr, String apiUri, String token, String contentType, Object request, String filters,
                         int connectionTimeout, int readTimeout) throws Exception {
        String serviceUrl = StringUtils.isEmpty(serverAddr) ? KUBE_URI + apiUri : serverAddr + apiUri;

        //RestAPI 연결
        RestTemplate restTemplate = restUtil.getRestTemplate(connectionTimeout, readTimeout);

        //Http 요청처리부 셋팅
        HttpMethod httpMethod = HttpMethod.valueOf(httpMethCd.isValue());

        //Http Header부 셋팅
        HttpHeaders header = new HttpHeaders();
        header.setContentType(contentType == null ? MediaType.APPLICATION_JSON : MediaType.valueOf(contentType));
        header.setBearerAuth((token == null || StringUtils.isEmpty(token)) ? chkToken(serverAddr) : token);

        //Http body부 셋팅
        String requestBody ="";
        if (request != null) {
            requestBody = mapper.writeValueAsString(request);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>( requestBody , header);

        try {
            //API 호출 후 응답은 String으로
            ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl, httpMethod, requestEntity, String.class, filters);

            JavaTimeModule module = new JavaTimeModule();
            LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

            // JSON 응답을 지정된 타입으로 변환
            return mapper.registerModule(module).readValue(responseEntity.getBody(), valueType);
        }
        catch (HttpServerErrorException httpex)
        {
            log.error("HttpServerErrorException Exception [{}]", httpex.getResponseBodyAsString());

            throw new ApiCallException(CoreErrCode.UNKNOWN_SYSTEM_ERROR, httpex);
        }
        catch (HttpClientErrorException httpex)
        {
            log.error("HttpClientErrorException Exception [{}]", httpex.getResponseBodyAsString());

            /** Error Message 만 표시하려는 경우 사용 */
            throw new ApiCallException(CoreErrCode.UNKNOWN_SYSTEM_ERROR,
                    httpex.getMessage(), httpex.getResponseBodyAsString(), httpex);
        }
        catch (Exception e)
        {
            log.error("System Error ",e);

            throw new Exception();
        }
    }


    public String callApiForLogs(CoreDefine.HttpMethCd httpMethCd, String apiUri) throws Exception
    {
        String serviceUrl =  KUBE_URI + apiUri;

        //RestAPI 연결
        RestTemplate restTemplate = restUtil.getRestTemplate(5, 60);

        //Http 요청처리부 셋팅
        HttpMethod httpMethod = HttpMethod.valueOf(httpMethCd.isValue());

        //Http Header부 셋팅
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(chkToken(null));

        //Http body부 셋팅
        String requestBody ="";


        HttpEntity<String> requestEntity = new HttpEntity<>( requestBody , header);
        try {
            //API 호출 후 응답은 String으로
            ResponseEntity<String> responseEntity = restTemplate.exchange(serviceUrl,httpMethod, requestEntity,
                    String.class);

            return responseEntity.getBody();

        }
        catch (HttpServerErrorException httpex)
        {
            log.error("Api Call Exception [{}]",httpex.getResponseBodyAsString());

            throw new ApiCallException(CoreErrCode.UNKNOWN_SYSTEM_ERROR, httpex.getResponseBodyAsString());
//            throw new OneQApimException( StringUtil.msgFormat("[{}] Api Call Error ", apiUri),  httpex );
        }
        catch (HttpClientErrorException e)
        {
            log.error("Error Response",e);

            throw new Exception();
//            throw new OneQSystemException(e);
        }
    }

    private String chkToken(String serverAddr) throws Exception {
        /** Token 확인 방식 변경 - 20241129 (TokenRequest Quartz 비활성화 처리) - namespace 동일해야함 */

        // 1. APP_TOKEN_INFO 테이블에서 토큰 조회
        AppTokenInfo appTokenInfo = daoAppTokenInfo.findById(KUBE_NAMESPACE, KUBE_SERVICE_ACCOUNT);
        if(appTokenInfo == null) {
            log.debug("It is Not Exist Token Information. namespace = [{}], service account = [{}]", KUBE_NAMESPACE, KUBE_SERVICE_ACCOUNT);
            throw new KubeException(CoreErrCode.NOT_EXIST_TOKEN);
        }

        String token = appTokenInfo.getTokenInfo();
        if(StringUtils.isEmpty(token)) {
            // APP_TOKEN_INFO 테이블에 토큰 정보가 없으면 오류 (초기에는 존재해야함)
            log.debug("It is Not Exist Token");
            throw new KubeException(CoreErrCode.NOT_EXIST_TOKEN);
        }

        // 2. 토큰 검증
        TokenReview tokenResult = tokenReview(token, serverAddr);
        if(StringUtils.isEmpty(tokenResult.getStatus().getError())) {
            return token;
        }

        // 3. 토큰이 유효하지 않으면 토큰을 조회할 Pod 가 존재하는지 체크
        String apiUri = stringUtil.makeFullPath(TOKEN_INQUIRY, KUBE_NAMESPACE);
        token = Files.readString(Path.of(TOKEN_PATH)).trim();
        if(StringUtils.isEmpty(token)) {
            log.debug("Token is not exist. Not Pod. Namespace = [{}]", KUBE_NAMESPACE);
            throw new KubeException(CoreErrCode.NOT_EXIST_POD, "Namespace = [" + KUBE_NAMESPACE + "]");
        }

        Object result = callApi(Object.class, HttpMethCd.GET, serverAddr, apiUri, token, null, tokenResult);
        log.debug("Token Inquiry Result = [{}]", result.toString());

        // 4. APP_TOKEN_INFO 테이블 업데이트
        appTokenInfo.setTokenInfo(token);
        appTokenInfo.setSysUpdDtm(LocalDateTime.now());
        daoAppTokenInfo.update(appTokenInfo);

        return token;
    }

    private TokenReview tokenReview(String token, String serverAddr) throws Exception {
        TokenReview tokenReview = new TokenReview();
        tokenReview.setApiVersion(KUBE_TOKEN_VER);
        tokenReview.setKind("TokenReview");
        tokenReview.getSpec().setToken(token);

        TokenReview tokenResult = new TokenReview();
        try {
            tokenResult = callApi(TokenReview.class, HttpMethCd.POST, serverAddr, TOKEN_REVIEW, token, null, tokenReview);
        } catch (ApiCallException e) {
            Map<String, Object> errorResponse = e.getResponseBodyMap();
            if(errorResponse != null && !errorResponse.isEmpty()) {
                String code = errorResponse.get("code") == null ? "" : errorResponse.get("code").toString();
                String message = errorResponse.get("message") == null ? "" : errorResponse.get("message").toString();

                log.error("Code = [{}], Message = [{}]", code, message);

                tokenResult.setKind(errorResponse.get("kind") == null ? "" : errorResponse.get("kind").toString());
                tokenResult.setApiVersion(errorResponse.get("apiVersion") == null ? "" : errorResponse.get("apiVersion").toString());
                tokenResult.getStatus().setError(message);
                tokenResult.getSpec().setToken(token);
            }
        }

        log.debug("Token Review Result = [{}]", tokenResult.toString());

        return tokenResult;
    }
}
