package com.naughty.userlogin02.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.naughty.userlogin02.bean.User;
import com.naughty.userlogin02.common.dto.LoginDto;
import com.naughty.userlogin02.common.lang.Result;
import com.naughty.userlogin02.dao.UserDao;
import com.naughty.userlogin02.util.JwtUtil;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    @Autowired
    UserDao userDao;
    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result userLogin(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        System.out.println(1);
        User users = userDao.getUser(loginDto.getUsername());
        System.out.println(users);
        String token = jwtUtil.generateToken(users.getId());
        System.out.println(loginDto.getUsername());
        Assert.notNull(users,"用户不存在");
        if (!users.getPassword().equals(loginDto.getPassword())){
            Result.faile("密码输入错误11");
        }
        System.out.println("tokem" +
                ""+token);

        response.setHeader("Authorization",token);
        response.setHeader("Access-control-Expose-Headers","Authorization");
        return Result.succ(MapUtil.builder()
                .put("id",users.getId()).put("username",users.getUsername())
                .put("email",users.getEmail()).put("state",users.isState()).map());
    }
}
