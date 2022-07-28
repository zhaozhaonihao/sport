package com.naughty.userlogin02.common.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
@Data
public class LoginDto {
    @NotBlank(message = "账号不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
