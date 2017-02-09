package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.CallLogs;

@Transactional
public interface CallLogsDao extends CrudRepository<CallLogs, Long> {

	String sql = "insert into call_logs (userId, country, total_mobileRx, "
			+ "total_mobileTx, total_wifiRx, total_wifiTx, timestamp) (select userId, country, sum(mobileRx) as totalMobileRx, sum(mobileTx) as totalMobileTx, "
			+ "sum(wifiRx) as totalWifiRx, sum(wifiTx) as totalWifiTx, now() from data_usage group by userId, country)";

	@Query(value = sql, nativeQuery = true)
	void aggregateCallDuration();
}
