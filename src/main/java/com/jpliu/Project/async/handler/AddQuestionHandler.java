package com.jpliu.Project.async.handler;

import com.jpliu.Project.async.EventHandler;
import com.jpliu.Project.async.EventModel;
import com.jpliu.Project.async.EventType;
import com.jpliu.Project.controller.CommentController;
import com.jpliu.Project.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AddQuestionHandler implements EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);


    @Autowired
    private SearchService searchService;

    @Override public void doHandle(EventModel eventModel) {
        try {

            searchService.indexQuestion(eventModel.getEntityId(),
                    eventModel.getExt("title"),
                    eventModel.getExt("content"));
        } catch (Exception e) {
            logger.error("增加题目索引失败 " + e.getMessage());
        }
    }

    @Override public List<EventType> getSupportEventType() {
        return Arrays.asList(new EventType[] {EventType.COMMENT, EventType.FOLLOW});
    }
}
