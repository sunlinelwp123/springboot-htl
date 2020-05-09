package com.open.boot.test.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.open.boot.core.Result;
import com.open.boot.core.ResultGenerator;
import com.open.boot.test.model.ThymeleafDome;
import com.open.boot.user.model.HtlUser;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/thyme")
public class ThymeleafController {
/*
	@RequestMapping(value = "/html", method = RequestMethod.GET, produces = "text/html")//produces只返回text/html类型的, consumes="text/html"只处理Content type=text/html类型的请求
	public ModelAndView html(Model model) {
		 ArrayList<ThymeleafDome> list = new ArrayList<>();
	        list.add(new ThymeleafDome("Async：简洁优雅的异步之道","在异步处理方案中，目前最为简洁优雅的便是async函数（以下简称A函数）。","www.baidu.com"));
	        list.add(new ThymeleafDome("H5 前端性能测试实践","H5 页面发版灵活，轻量，又具有跨平台的特性，在业务上有很多应用场景。","www.baidu.com"));
	        list.add(new ThymeleafDome("学习Python的建议","Python是最容易入门的编程语言。","www.baidu.com"));
	        model.addAttribute("articleList",list);
		return  new ModelAndView("./test/thymeleafDemo");
	}
	*/
	@GetMapping(value = "/html", produces = "text/html")
    public ModelAndView userRegister(Model model){
		 HtlUser htluser =new HtlUser();
		 model.addAttribute("user", htluser);
		 ArrayList<ThymeleafDome> list = new ArrayList<>();
	        list.add(new ThymeleafDome("Async：简洁优雅的异步之道","在异步处理方案中，目前最为简洁优雅的便是async函数（以下简称A函数）。","www.baidu.com"));
	        list.add(new ThymeleafDome("H5 前端性能测试实践","H5 页面发版灵活，轻量，又具有跨平台的特性，在业务上有很多应用场景。","www.baidu.com"));
	        list.add(new ThymeleafDome("学习Python的建议","Python是最容易入门的编程语言。","www.baidu.com"));
	        model.addAttribute("articleList",list);
        return new ModelAndView("./test/thymeleafDemo");
    }
	
	@RequestMapping(value = "test", method = RequestMethod.POST)
	public Result test(HttpServletRequest request, Model model,HtlUser htluser) {
		return ResultGenerator.genSuccessResult(htluser);
	}
	
}
