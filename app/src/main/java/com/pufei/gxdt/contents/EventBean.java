package com.pufei.gxdt.contents;

import com.pufei.gxdt.module.user.bean.UserBean;

/**
 * Created by wangwenzhang on 2017/8/11.
 */

public class EventBean {
    private String  content;
    private UserBean user;

    public EventBean(String content, UserBean user) {
        this.content = content;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
