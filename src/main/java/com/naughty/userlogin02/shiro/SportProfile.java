package com.naughty.userlogin02.shiro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportProfile implements Serializable {
    private int id;
    private String username;
    private String email;
    private boolean state;
}
