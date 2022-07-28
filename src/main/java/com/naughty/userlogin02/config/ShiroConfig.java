package com.naughty.userlogin02.config;

import com.naughty.userlogin02.shiro.JwtFilter;
import com.naughty.userlogin02.shiro.SportRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager,
                                                         ShiroFilterChainDefinition shiroFilterChainDefinition){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加自定义过滤器
        Map<String, Filter> map=new HashMap<>();
        map.put("jwt",jwtFilter);
        shiroFilterFactoryBean.setFilters(map);
        Map<String, String> filterChainMap = shiroFilterChainDefinition.getFilterChainMap();
        //设置访问路径需要什么权限才能访问
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition defaultShiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        Map<String,String> map=new LinkedHashMap<>();
        map.put("/**","jwt");
        defaultShiroFilterChainDefinition.addPathDefinitions(map);
        return defaultShiroFilterChainDefinition;
    }
    @Bean
    public DefaultWebSecurityManager securityManager(SportRealm sportRealm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager(sportRealm);
        ThreadContext.bind(defaultWebSecurityManager);
        return defaultWebSecurityManager;
    }
}
