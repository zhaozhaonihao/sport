package com.naughty.userlogin02.shiro;

import com.naughty.userlogin02.bean.User;
import com.naughty.userlogin02.dao.UserDao;
import com.naughty.userlogin02.util.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class SportRealm extends AuthorizingRealm {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDao userDao;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) token;
        System.out.println("jwtToken:"+token);
        String userId = jwtUtil.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userDao.getUpdateUser(Integer.parseInt(userId));
        if (user == null){
            throw new UnknownAccountException("用户不存在");
        }
        SportProfile sport=new SportProfile();
        BeanUtils.copyProperties(user,sport);
        return new SimpleAuthenticationInfo(sport,jwtToken.getCredentials(),getName());
    }
}
