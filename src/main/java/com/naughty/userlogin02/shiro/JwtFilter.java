package com.naughty.userlogin02.shiro;

import com.naughty.userlogin02.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends AuthenticatingFilter {
    @Autowired
    private JwtUtil jwtUtil;
    //当拦截器执行时，会预先经过prehandle，进行一些逻辑处理  为response设置header，实现跨域
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest re=(HttpServletRequest)request;
        String jwt=re.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            return null;
        }
        //将客户端传递过来的请求头赋值给authenticationtoken 进行初始化
        return new JwtToken(jwt);
    }
    //进行token校验
    //在没有登陆的情况下,执行该方法  isAccessAllowed：在登录的情况下执行该方法，此方法返回true的情况下进入控制器
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest re = (HttpServletRequest) request;
        String token = re.getHeader("Authorization");
        System.out.println(token);
        if (StringUtils.isEmpty(token)){
            return true;
        }else {
            Claims jwt = jwtUtil.getClaimByToken(token);
            if (jwt==null || jwtUtil.isTokenExpira(jwt.getExpiration())){
                throw new ExpiredCredentialsException("token已失效，请重新登陆");
            }
        }
        return executeLogin(request,response);
    }
    /**
     * 为response设置header，实现跨域
     */
    /*private void setHeader(HttpServletRequest request, HttpServletResponse response){
        //跨域的header设置
        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getMethod());
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        //防止乱码，适用于传输JSON数据
        //Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
    }*/
}
