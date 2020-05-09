package com.open.boot.user.controller;

import javax.annotation.Resource;

import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.open.boot.core.redis.RedisUtil;
//import com.open.boot.user.model.HtlUser;
@RestController
@RequestMapping("/htl")
public class HtlModelController {
	@Resource
	RedisUtil RedisUtil;
	  /**
     * 注冊頁面跳转
     * @param model
     * @return
     */
	@RequestMapping(value = "/home", produces = "tex/html")
	public ModelAndView  homeModel(Model model/*, @RequestBody HtlUser htlUser*/) {
			/*model.addAttribute("user", htlUser);*/
			return new ModelAndView("./htl/home.html");
	}
}
