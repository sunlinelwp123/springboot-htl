package com.open.boot.user.dao;

import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.open.boot.core.Mapper;
import com.open.boot.user.dao.provider.HtlUserProvider;
import com.open.boot.user.model.HtlUser;

public interface HtlUserMapper extends Mapper<HtlUser> {
	@SelectProvider(type = HtlUserProvider.class, method = "queryByConditions")
	HtlUser queryByConditions(Map<String, String> map);
}