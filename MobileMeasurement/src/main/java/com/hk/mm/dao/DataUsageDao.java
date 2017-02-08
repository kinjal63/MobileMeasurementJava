package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.DataUsage;

@Transactional
public interface DataUsageDao extends CrudRepository<DataUsage, Long> {

}
