package com.kitfrankenstein.newspusher.component;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kit
 * @date: 2019/8/14 18:01
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        if (url.endsWith("console") && request.getSession().getAttribute("loginUser") == null) {
            request.getRequestDispatcher("/view/login").forward(request, response);
            return false;
        }
        return true;
    }
}
