package com.jpliu.Project.service;

import com.jpliu.Project.dao.MessageDAO;
import com.jpliu.Project.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageService {


    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));

        return messageDAO.addMessage(message) > 0 ? message.getId() : 0;
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList( int userId, int offset, int limit) {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }

    public void updateConversationUnread(int userId, String conversationId, int hasRead) {
        messageDAO.updateConversationUnread(userId, conversationId, hasRead);
    }
}
