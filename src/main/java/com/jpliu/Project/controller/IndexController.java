package com.jpliu.Project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/*
 * 入口层 就是controller 首页就是 index
 */
//@Controller
public class IndexController {

    @RequestMapping(path={"/", "/index"}, method = RequestMethod.GET) //访问这样的地址 就是返回这个字符串
    @ResponseBody //表示返回的不是模板
    public String index(HttpSession httpSession) {
        return "Hello NowCoder" + httpSession.getAttribute("msg");
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"}) //访问这样的地址 就是返回这个字符串
    @ResponseBody //表示返回的不是模板
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "testing", required = false) String key) {
        return String.format("ProgroupIdfile Page of %s / %d, t : %d, k: %s", groupId, userId, type, key);
    }

    @RequestMapping(path={"/vm"}, method = RequestMethod.GET) //访问这样的地址 就是返回这个字符串
    public String template(Model model) {
        model.addAttribute("value1", "vvv1");
        List<String> colors = Arrays.asList(new String[] {"RED", "GREEN", "BLUE"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }

        model.addAttribute("map", map);
//        model.addAttribute("user", new UserService("LEE"));
        return "home";
    }


    @RequestMapping(path={"/request"}, method = RequestMethod.GET) //访问这样的地址 就是返回这个字符串
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession httpSession,
                           @CookieValue("JSESSIONID") String sessionId) {

        StringBuilder sb = new StringBuilder();
        sb.append("COOKIE:" + sessionId);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name + " : " + request.getHeader(name) + "<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie : " + cookie.getName() + " value:" + cookie.getValue());
            }
        }
        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURL() + "<br>");

        response.addHeader("nowcoderID", "hello");
        response.addCookie(new Cookie("username", "nowcoder"));

        return sb.toString();
    }

    @RequestMapping(path={"/redirect/{code}"}, method = RequestMethod.GET) //访问这样的地址 就是返回这个字符串
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;
    }
    @RequestMapping(path={"/admin"}, method = RequestMethod.GET)
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "Hello Admin";
        }

        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error " + e.getMessage();
    }


}
