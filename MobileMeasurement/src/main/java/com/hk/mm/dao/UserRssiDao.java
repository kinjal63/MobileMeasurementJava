package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.Rssi;

@Transactional
public interface UserRssiDao extends CrudRepository<Rssi, Long> {

}
