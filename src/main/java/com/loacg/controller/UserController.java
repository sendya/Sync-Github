package com.loacg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/23/2016 8:41 PM
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping(value = {"/index","/home", "/my", "/{user}"})
    public ModelAndView home(HttpServletRequest req, HttpServletResponse res, @PathVariable("user") String user) {
        ModelAndView mv = new ModelAndView();
        if (!StringUtils.isEmpty(user)) {
            mv.addObject("name", user);
        } else {
            mv.addObject("name", "Default");
        }

        mv.setViewName("user/home");
        return mv;
    }
}
