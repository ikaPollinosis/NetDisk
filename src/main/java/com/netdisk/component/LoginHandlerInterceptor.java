package com.netdisk.component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: LoginHandlerInterceptor
 * @Description: 登录句柄拦截器
 * @Date: 2022/4/29 18:25
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
        * @Description:  在Controller前拦截未登录用户
        * @Param: [request, response, handler]
        * @return: boolean
        * @Date: 2022/4/29
        */
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser==null){
            response.sendRedirect("/netdisk/"); // 未登录，返回主页
            return false;
        }else {
            return true;    // 已登入，放行
        }
    }

}
