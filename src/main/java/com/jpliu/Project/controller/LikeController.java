package com.jpliu.Project.controller;

import com.jpliu.Project.async.EventModel;
import com.jpliu.Project.async.EventProducer;
import com.jpliu.Project.async.EventType;
import com.jpliu.Project.model.Comment;
import com.jpliu.Project.model.EntityType;
import com.jpliu.Project.model.HostHolder;
import com.jpliu.Project.service.CommentService;
import com.jpliu.Project.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.MD5Util;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private CommentService commentService;



    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {

        if (hostHolder.getUser() == null) {
            return MD5Util.getJSONString(999);
        }
        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fileEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);

        return MD5Util.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {

        if (hostHolder.getUser() == null) {
            return MD5Util.getJSONString(999);
        }

        long dislikeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);

        return MD5Util.getJSONString(0, String.valueOf(dislikeCount));
    }

}
