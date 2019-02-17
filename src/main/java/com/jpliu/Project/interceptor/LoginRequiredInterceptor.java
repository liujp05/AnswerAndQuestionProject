package com.jpliu.Project.interceptor;

import com.jpliu.Project.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问某个一定要登录的页面 如果没有登录 那么跳转到登录页面，登录之后跳回 之前页面
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor{


    @Autowired
    private HostHolder hostHolder;


    //请求开始之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if (hostHolder.getUser() == null) {//passport interceptor 里面先执行 为null时 代表没登录
            httpServletResponse.sendRedirect("/relogin?next=" + httpServletRequest.getRequestURL());
        }
        return true;
    }

    //handler处理完了之后 在回调
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    //整个处理完了之后 在执行
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }
}
