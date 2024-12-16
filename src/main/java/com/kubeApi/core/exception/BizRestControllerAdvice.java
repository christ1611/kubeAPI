package com.kubeApi.core.exception;

import com.kubeApi.core.mapper.MapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class BizRestControllerAdvice {

    @ExceptionHandler(value = {CoreException.class})
    public ResponseEntity<String> prcOneQOnCoreException(CoreException ex) throws Exception {
        log.error("OneQOnCoreException", ex);

        return getResponseModel(ex.getErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> prcBizException(Exception ex) throws Exception {
        log.error("Exception", ex);

        ErrorResponse errResponse = new ErrorResponse();
        errResponse.setErrorCode(-1);
        errResponse.setMessage("An error that should never occur.");
        errResponse.setTimeStamp(LocalDateTime.now());
        errResponse.setException(ex);

        return getResponseModel(errResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<String> getResponseModel(ErrorResponse response, HttpStatus statusCode) throws Exception {
        /** ModelAndView 로 Return 하려는 경우 사용 */
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("jsonView");
//        mv.addObject("output", response);

        return ResponseEntity.status(statusCode).body(MapperUtil.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }
}
