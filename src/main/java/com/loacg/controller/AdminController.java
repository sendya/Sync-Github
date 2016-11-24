package com.loacg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Project: Sync-Github
 * Author: Sendya <18x@loacg.com>
 * Time: 11/23/2016 9:06 PM
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping(value = {"/repo/add", "/addRepo"})
    public String addRepo() {
        return "index";
    }

    @RequestMapping(value = {"/repo/edit/{id}", "/editRepo"})
    public String editRepo(@PathVariable("id") Integer id) {

        return "index";
    }

    @RequestMapping(value = "/repo/del/{id}")
    public String deleteRepo(@PathVariable("id") Integer id) {

        return "index";
    }
}
