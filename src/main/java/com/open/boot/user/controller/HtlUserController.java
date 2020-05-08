package com.open.boot.user.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.open.boot.core.CorePager;
import com.open.boot.core.Result;
import com.open.boot.core.ResultCode;
import com.open.boot.core.ResultGenerator;
import com.open.boot.core.redis.RedisUtil;
import com.open.boot.core.utils.CommonUtil;
import com.open.boot.user.model.HtlUser;
import com.open.boot.user.service.HtlUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by actor-T on 2018/12/07.
 */
@RestController
@RequestMapping("/htlUser")
public class HtlUserController {
    @Autowired
    private HtlUserService htlUserService;
    @Resource
    RedisUtil RedisUtil;

    @RequestMapping("/add")
    public Result add(@RequestBody HtlUser user) {
        htlUserService.saveSelective(user);
        return ResultGenerator.genSuccessResult(user);
    }

    @RequestMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        htlUserService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping("/updateSelective")
    public Result update(@RequestBody HtlUser user) {
        htlUserService.updateSelective(user);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping("/updateAll")
    public Result updateAll(@RequestBody HtlUser user) {
        htlUserService.updateAll(user);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        HtlUser user = htlUserService.findById(id);
        return ResultGenerator.genSuccessResult(user);
    }
    @RequestMapping("/getUser")
    public Result detail(@RequestBody Map<String,String> map) {
        if(StringUtil.isEmpty(map.get("id"))){
            Result ret =new Result();
            ret.setCode(ResultCode.INTERNAL_SERVER_ERROR);
            ret.setMessage("ID未识别");
           return ret;
        }
        HtlUser user = htlUserService.findById(Integer.parseInt(map.get("id")));
        return ResultGenerator.genSuccessResult(user);
    }

    @RequestMapping("/listAll")
    public Result listAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<HtlUser> list = htlUserService.findAll();
        PageInfo<HtlUser> pageInfo = new PageInfo<HtlUser>(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping("/addList")
    public Result addList(@RequestBody List<HtlUser> list) {
        int count = htlUserService.saveList(list);
        return ResultGenerator.genSuccessResult(count);
    }

    @RequestMapping("/updateList")
    public Result updateListHtlUser(@RequestBody List<HtlUser> list) {
        for (HtlUser item : list) {
            htlUserService.updateSelective(item);
        }
        return ResultGenerator.genSuccessResult();
    }

    /**
     * actor-T 传分页对象就分页查询，不传就不分页
     *
     * @param map paper对象和实体类objBean条件对象
     * @return 查询结果
     */
    @RequestMapping("/listByCondition")
    public Result listByCondition(@RequestBody Map<String, Object> map) {
        String pagerStr = CommonUtil.getMapValue(map, "pager");
        HtlUser objBean = JSON.parseObject(map.get("objBean").toString(), HtlUser.class);
        if (StringUtil.isEmpty(pagerStr)) {
            Condition condition = new Condition(HtlUser.class);
            condition.createCriteria().andEqualTo(objBean);
            List<HtlUser> list = htlUserService.findByCondition(condition);
            return ResultGenerator.genSuccessResult(list);
        } else {
            CorePager pager = JSON.parseObject(pagerStr, CorePager.class);
            PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
            Condition condition = new Condition(HtlUser.class);
            condition.createCriteria().andEqualTo(objBean);
            List<HtlUser> list = htlUserService.findByCondition(condition);
            PageInfo<HtlUser> pageInfo = new PageInfo<>(list);
            return ResultGenerator.genSuccessResult(pageInfo);
        }
    }

    /**
     * actor-T 根据条件更新objBean不为空的值
     *
     * @param map objCondition为更新条件对象，objBean为更新实体内对象
     * @return 查询结果
     */
    @RequestMapping("/updateByConditionSelective")
    public Result updateByConditionSelective(@RequestBody Map<String, Object> map) {
        HtlUser objBean = JSON.parseObject(map.get("objBean").toString(), HtlUser.class);
        HtlUser objCondition = JSON.parseObject(map.get("objCondition").toString(), HtlUser.class);
        Condition condition = new Condition(HtlUser.class);
        condition.createCriteria().andEqualTo(objCondition);
        htlUserService.updateByConditionSelective(objBean, objCondition);
        return ResultGenerator.genSuccessResult();
    }
}
