package com.open.boot.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转控制
 * 
 * @author cuijia
 *
 */
//@Controller
@RequestMapping(value = "/path")
public class WebCoreController {
	private Logger logger = LoggerFactory.getLogger(WebCoreController.class);

	/**
	 * 控制页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(value = "/{modules}/{pages}")
	public String toPage(@PathVariable("modules") String modules,
			@PathVariable("pages") String pages) {
		StringBuffer path = new StringBuffer();
		path.append("./").append(modules).append("/").append(pages);
		logger.info("跳转页面=========" + path);
		return path.toString();
	}

}
