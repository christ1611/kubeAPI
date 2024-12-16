package com.kubeApi.core.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
//@Component
@RequiredArgsConstructor
public class ControllerInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        log.info("ControllerInterceptor postHandle START!, uri = [{}] handler = [{}]-[{}] ", uri, handler.getClass() , handler);

        return true;
    }

    @Override
    public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String uri = request.getRequestURI();

        log.info("ControllerInterceptor postHandle START!, uri = [{}] handler = [{}]-[{}] ", uri, handler.getClass() , handler);
        log.trace("ModelAndView = [{}] ",modelAndView);

        if( modelAndView == null ) return ;

        modelAndView.setViewName("jsonView");

        log.info("ControllerInterceptor postHandle End...");
    }

    /**
     * View 작업까지 완료된 후 Client에 응답하기 바로 전에 호출됩니다.
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        log.info("Interceptor Request Complete!");
    }
}
