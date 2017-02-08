package com.hk.mm.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.hk.mm.entity.GameLibrary;

@Transactional
public interface GameLibraryDao extends CrudRepository<GameLibrary, Long>{

}
