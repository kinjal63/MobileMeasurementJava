//package com.hk.mm.dao;
//
//import java.util.List;
//
//import javax.transaction.Transactional;
//
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//
//import com.hk.mm.vo.UserGameResponse;
//
//@Transactional
//public interface UserGameResponseDao extends CrudRepository<UserGameResponse, Long> {
//
////	@Query("select u.id as user_id, u.image_path as user_image_path, "
////			+ "u.firstname, u.lastname, GROUP_CONCAT(distinct gl.id) as game_id, GROUP_CONCAT(distinct gl.game_name) as game_name, "
////			+ "GROUP_CONCAT(distinct gl.game_image_path) as game_image_path from game_profile gf "
////			+ "inner join game_profile gf1 on gf.game_id = gf1.game_id and gf.user_id <> gf1.user_id "
////			+ "join game_library gl on gl.id = gf.game_id join user u on u.id = gf.user_id where "
////			+ "u.id in (select u.id from user u j	oin user_availability ua on u.id = ua.user_id "
////			+ "where ua.availability = 1 and (ua.latitude -  58.5467316) < 10 ) "
////			+ "and gf.game_id in (select gf.game_id from game_profile gf where gf.user_id = 29"
////			+ ")and u.id <> 28 group by u.id")
////	public List<UserGameResponse> getMutalGames();	
//
//}
