package com.naughty.userlogin02.util;

import com.naughty.userlogin02.shiro.SportProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil {
    public static SportProfile getProfilg(){
        return (SportProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
