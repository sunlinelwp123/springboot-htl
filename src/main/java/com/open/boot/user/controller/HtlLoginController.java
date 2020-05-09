package com.open.boot.user.controller;

import com.open.boot.common.CommonConstant;
import com.open.boot.core.Result;
import com.open.boot.core.ResultGenerator;
import com.open.boot.core.redis.RedisUtil;
import com.open.boot.core.utils.CommonUtil;
import com.open.boot.core.utils.DigestUtil;
import com.open.boot.user.model.HtlUser;
import com.open.boot.user.service.HtlUserService;
import com.open.boot.user.service.IAliyunDysmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author lwp
 */
@RestController
@RequestMapping("/htllogin")
public class HtlLoginController {
	@Autowired
	private HtlUserService htluserService;
	@Resource
	RedisUtil redisUtil;
	@Autowired
	IAliyunDysmsService aliyunDysmsService;

	/**
	 * 注册
	 * 
	 * @param htlUser 用户对象
	 * @return 注册对象
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody HtlUser htlUser) {
		if (StringUtils.isEmpty(htlUser.getUserName()) || StringUtils.isEmpty((htlUser.getPassword()))) {
			return ResultGenerator.genFailResult("用户名或密码不能为空!");
		}
		htlUser.setPassword((DigestUtil.MD5Encode(htlUser.getPassword(), CommonConstant.TOKEN_SERECT, false)));
		htluserService.saveSelective(htlUser);
		htlUser.setPassword("");
		return ResultGenerator.genSuccessResult(htlUser);
	}
	
    /**
     * 注冊頁面跳转
     * @param model
     * @return
     */
	@GetMapping(value = "/register/html", produces = "text/html")
	public ModelAndView registModel(Model model) {
		return new ModelAndView("./htl/register");
	}
	
	/**
	 * 登录页面跳转
	 * @param model
	 * @return 登录页面
	 * produces只返回text/html类型的, consumes="text/html"只处理Content
	 */

	@GetMapping(value = "/html", produces = "text/html")
	public ModelAndView loginModel(Model model) {
		HtlUser htluser = new HtlUser();
		model.addAttribute("user", htluser);
		return new ModelAndView("./htl/login");
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result htlLogin(HttpServletRequest request, @RequestBody HtlUser htluser) {
		htluser.setPassword((DigestUtil.MD5Encode(htluser.getPassword(), CommonConstant.TOKEN_SERECT, false)));
		return login(request, htluser);
	}

	/**
	 * 登录
	 * 
	 * @param request HttpServletRequest
	 * @return 返回登录结果
	 */
	public Result login(HttpServletRequest request, HtlUser htlUser) {
		if (StringUtils.isEmpty(htlUser.getUserName()) || StringUtils.isEmpty((htlUser.getPassword()))) {
			return ResultGenerator.genFailResult("用户名或密码不能为空!");
		}
		HtlUser htluser2 = checkLoginWay(htlUser);
		if (htluser2 == null) {
			return ResultGenerator.genFailResult("用户名或密码错误");
		}
		// 将登陆日志写入数据库
		writeLog(htluser2, request);
		HttpSession session = request.getSession();
		String token = DigestUtil.MD5Encode(session.getId(), htlUser.getUserName(), false);
		htluser2.setToken(token);
		redisUtil.hmset(token + "_" + htlUser.getId(), redisUtil.objectToMapFast(htluser2), 60 * 60);
		return ResultGenerator.genSuccessResult(htluser2);
	}

	public void writeLog(HtlUser htluser, HttpServletRequest request) {

	}

	public HtlUser checkLoginWay(HtlUser htluser) {
		Map<String, String> map = new HashMap<>(5);
		HtlUser htluser2 = null;
		if (htluser.getUserName().matches(CommonUtil.tel)) {
			map.put("tel", htluser.getUserName());
			map.put("password", htluser.getPassword());
			htluser2 = htluserService.findByConditions(map);
			return htluser2;
		} else {
			map.put("userName", htluser.getUserName());
			map.put("password", htluser.getPassword());
			htluser2 = htluserService.findByConditions(map);
			return htluser2;
		}
	}

	/**
	 * 发送短信验证码 (区分 |注册|找回密码|时发送验证码)
	 * 
	 * @param tel  value = "发送短信,0注册短信 1找回密码", required = true, dataType = "String"
	 * @param type type
	 * @return 发送结果
	 */
	@RequestMapping(value = "/queryMsgCode")
	public Result queryMsgCode(@RequestParam("tel") String tel, @RequestParam("type") int type) {
		/*加上一些字母，就可以生成pc站的验证码*/
		String sources = "123456789";
		Random rand = new Random();
		StringBuffer verifyCode = new StringBuffer();
		int codelen = 4;
		for (int j = 0; j < codelen; j++) {
			verifyCode.append(sources.charAt(rand.nextInt(8)) + "");
		}
		boolean falg = aliyunDysmsService.sendVcodeSms(tel, verifyCode.toString(), "outId", type);
		if (falg) {
			/*默认180S*/
			redisUtil.set(tel, verifyCode, 180);
			return ResultGenerator.genSuccessResult("发送成功");
		} else {
			return ResultGenerator.genFailResult("发送失败");
		}
	}

	/**
	 * 验证验证码
	 * 
	 * @param tel        电话号码
	 * @param verifyCode 验证码
	 * @return 验证结果
	 */
	@RequestMapping(value = "/verityMsgCode")
	public Result verityMsgCode(@RequestParam("tel") String tel, @RequestParam("verifyCode") String verifyCode) {
		if (redisUtil.hasKey(tel)) {
			String vCode = redisUtil.get(tel).toString();
			if (vCode.equals(verifyCode)) {
				return ResultGenerator.genSuccessResult("验证成功");
			} else {
				return ResultGenerator.genFailResult("验证失败");
			}
		} else {
			return ResultGenerator.genFailResult("验证码过期");
		}
	}
}
