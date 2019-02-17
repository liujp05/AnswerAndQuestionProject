package com.jpliu.Project.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {

    //每个线程 都有一份拷贝
    /**
     * 其实底部 类似 一个MAP<Thread ID, value> 对应的thread 取相应的value
     */
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
