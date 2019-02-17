package com.jpliu.Project.service;

import com.jpliu.Project.dao.QuestionDAO;
import com.jpliu.Project.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private SensitiveService sensitiveService;

    public Question getById(int id) {
        return  questionDAO.getById(id);
    }

    public int addQuestion(Question question) {

        question.setContent(HtmlUtils.htmlEscape(question.getContent()));//过滤html的标签
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));//过滤title里面的html标签

        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }


    public int updateCommentCount(int id, int count) {
        return questionDAO.updateCommentCount(id, count);
    }




}
