package com.jpliu.Project.async.handler;

import com.jpliu.Project.async.EventHandler;
import com.jpliu.Project.async.EventModel;
import com.jpliu.Project.async.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpliu.Project.service.MailSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    private MailSender mailSender;


    @Override public void doHandle(EventModel eventModel) {
        //xxx判断发现这个用户登录异常
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("email"), "登录IP异常", "mails/login_exception.html", map);
    }

    @Override public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
