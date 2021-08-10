package com.springboot.BookShop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.BookShop.entity.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	public User getUserByUsername(@Param("username") String username);
	
	@Query("SELECT u FROM User u WHERE u.email= :email")
	public User getUserByEmail(@Param("email") String email);
	
	@Query("UPDATE User u SET u.enable = true WHERE u.id = ?1")
	@Modifying
	public void enable(Integer id);
	
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1")
	public User findByVerificationCode(String code);
	
	@Query("SELECT u FROM User u WHERE u.username = ?1")
	public User findByUsername(String username);

}
