package cn.itcast.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        //判断是否是相关登录的资源
        //强制转换为
        HttpServletRequest request = (HttpServletRequest) req;
         //1获取资源的请求路径
        String uri = ((HttpServletRequest) req).getRequestURI();
        // //判断是否是相关登录的资源,注意排除掉，css/js图片/验证码等资源
        if (uri.contains("/login.jsp")|| uri.contains("/loginServlet")||uri.contains("/css/")||uri.contains("/js/")||uri.contains("/fonts/")||uri.contains("/checkCodeServlet")){
            //包含，证明用户就是想登录，放行
            chain.doFilter(req, resp);

        }else {
            //不包含，需要验证用户是否登录
            //3从session中获取user
            Object user = request.getSession().getAttribute("user");
            if (user != null){
                //已经登录了
                chain.doFilter(req, resp);
            }else {
                //没有登录。跳转登录信息
                request.setAttribute("login_msg","您没有登录，请你登录");
                request.getRequestDispatcher("/login.jsp").forward(request,resp);
            }
        }

        /*chain.doFilter(req, resp);*/
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
