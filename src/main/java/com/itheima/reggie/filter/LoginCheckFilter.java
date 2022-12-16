package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器：检查用户是否已经登录完成
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI = request.getRequestURI();//backend/index.html

        log.info("拦截到请求：{}", requestURI);

        //定义不需要处理的请求路径
        String[] uris = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端登录
        };

        //判断本次请求是否需要放行
        boolean check = check(uris, requestURI);

        //如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //判断登录状态（网页端），如果是已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            //自定义基于ThreadLocal封装工具类，存入当前用户id,用于MyMetaObjectHandler类sql字段自动填充时获取id。
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);

            /*long id = Thread.currentThread().getId();
            log.info("线程id为：{}",id);*/

            return;
        }

        //判断登录状态（移动端），如果是已登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("user"));
            Long userId = (Long) request.getSession().getAttribute("user");
            //自定义基于ThreadLocal封装工具类，存入当前用户id,用于MyMetaObjectHandler类sql字段自动填充时获取id。
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);

            /*long id = Thread.currentThread().getId();
            log.info("线程id为：{}",id);*/

            return;
        }

        log.info("用户未登录");
        //如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param uris
     * @param requestURI
     * @return
     */
    public boolean check(String[] uris, String requestURI) {
        for (String url : uris) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {//匹配上返回true
                return true;
            }
        }
        return false;
    }
}
