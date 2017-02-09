package com.hk.mm.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.mm.dao.CallLogsDao;
import com.hk.mm.entity.CallLogs;

@Service
public class AppService {

	@Autowired
	private CallLogsDao callLogsDao;
	
	public void aggregateCallDuration(CallLogs callLogs) {
		callLogsDao.aggregateCallDuration();
	}
}
