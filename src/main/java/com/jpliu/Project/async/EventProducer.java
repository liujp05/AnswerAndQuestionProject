package com.jpliu.Project.async;

import com.alibaba.fastjson.JSONObject;
import com.jpliu.Project.service.JedisAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.RedisKeyUtil;

@Service
public class EventProducer {

    @Autowired
    private JedisAdapter jedisAdapter;

    public boolean fileEvent(EventModel eventModel) {
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
