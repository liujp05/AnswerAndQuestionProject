package com.jpliu.Project.async.handler;

import com.jpliu.Project.async.EventHandler;
import com.jpliu.Project.async.EventModel;
import com.jpliu.Project.async.EventType;
import com.jpliu.Project.model.EntityType;
import com.jpliu.Project.model.Message;
import com.jpliu.Project.model.User;
import com.jpliu.Project.service.MessageService;
import com.jpliu.Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.MD5Util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowHandler implements EventHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override public void doHandle(EventModel model) {

        Message message = new Message();
        message.setFromId(MD5Util.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());


        if (model.getEntityType() == EntityType.ENTITY_QUESTION) {
            message.setContent("用户" + user.getName() +
                    " 关注了你的问题, http://127.0.0.1:8080/question/" + model.getEntityId());
        } else if (model.getEntityType() == EntityType.ENTITY_USER){
            message.setContent("用户" + user.getName() +
                    " 关注了你, http://127.0.0.1:8080/user/" + model.getActorId());
        }

        messageService.addMessage(message);
    }

    @Override public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
