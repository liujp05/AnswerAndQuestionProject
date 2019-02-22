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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;
import util.MD5Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);


    @Autowired
    private SearchService searchService;

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;


    @RequestMapping(path = {"/search"}, method = {RequestMethod.GET})
    public String addComment(Model model, @RequestParam("q") String keyword,
                             @RequestParam(value = "offset", defaultValue = "0") int offset,
                             @RequestParam(value = "count", defaultValue = "10") int count) {

        try {

            List<Question> questionList = searchService.searchQuestion(keyword, offset, count, "<em>", "</em>");

            List<ViewObject> vos = new ArrayList<ViewObject>();

            for (Question question : questionList) {
                Question q = questionService.getById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent() != null) {
                    q.setContent(question.getContent());
                }
                if (question.getTitle() != null) {
                    q.setTitle(question.getTitle());
                }
                vo.set("question", q);
                vo.set("followCount", followService.getFollowerCount(EntityType.ENTITY_QUESTION, question.getId()));
                vo.set("user", userService.getUser(q.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
            model.addAttribute("keyword", keyword);

        } catch (Exception e) {
            logger.error("搜索评论失败" + e.getMessage());
        }

        return "result";
    }


}
