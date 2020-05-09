package com.open.boot.user.service;

import java.util.Map;

import com.open.boot.core.Service;
import com.open.boot.user.model.HtlUser;

public interface HtlUserService extends Service<HtlUser>{
	HtlUser findByConditions(Map<String, String> map);
}
