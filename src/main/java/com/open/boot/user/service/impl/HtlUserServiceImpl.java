package com.open.boot.user.service.impl;
/**
 * 
 * @author lwp
 *
 */

import com.open.boot.core.AbstractService;
import com.open.boot.user.dao.HtlUserMapper;
import com.open.boot.user.model.HtlUser;
import com.open.boot.user.service.HtlUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
@Service
@Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class HtlUserServiceImpl extends AbstractService<HtlUser> implements HtlUserService{
	@Autowired
	private HtlUserMapper htlUserMapper;

	@Override
	public HtlUser findByConditions(Map<String, String> map) {
		// TODO Auto-generated method stub
		return htlUserMapper.queryByConditions(map);
	}
	
}
