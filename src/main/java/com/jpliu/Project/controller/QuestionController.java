package com.jpliu.Project.controller;

import com.jpliu.Project.async.EventModel;
import com.jpliu.Project.async.EventProducer;
import com.jpliu.Project.async.EventType;
import com.jpliu.Project.model.*;
import com.jpliu.Project.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import util.MD5Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 发布问题的controller
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HostHolder hostHolder; // 当前用户

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                question.setUserId(MD5Util.ANONYMOUS_USERID);
//                return MD5Util.getJSONString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                //当增加题目成功之后， 增加一个事件出来
                eventProducer.fileEvent(new EventModel(EventType.ADD_QUESTION)
                .setActorId(question.getUserId()).setEntityId(question.getId())
                .setExt("title", question.getTitle()).setExt("content", question.getContent()));
                return MD5Util.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("增加问题失败 " + e.getMessage());
        }
        return MD5Util.getJSONString(1, "失败");

    }

    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {


        Question question = questionService.getById(qid);
        model.addAttribute("question", question);

        //找到全部的comment
        List<Comment> commentList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);

        //获得用户的信息
        List<ViewObject> vos = new ArrayList<ViewObject>();

        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            vos.add(vo);
        }

        model.addAttribute("comments", vos);

        List<ViewObject> followUsers = new ArrayList<ViewObject>();

        //获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }

        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }

        return "detail";
    }

}
