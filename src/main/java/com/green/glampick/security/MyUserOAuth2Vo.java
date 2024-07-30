package com.green.glampick.security;

import com.green.glampick.common.Role;
import lombok.Getter;

@Getter
public class MyUserOAuth2Vo extends MyUser {

    private final String nm;
    private final String pic;

    public MyUserOAuth2Vo(long userId, Role role, String nm, String pic) {
        super(userId, role);
        this.nm = nm;
        this.pic = pic;
    }

}
