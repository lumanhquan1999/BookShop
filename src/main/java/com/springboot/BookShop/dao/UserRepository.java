package com.springboot.BookShop.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.BookShop.entity.User;

@Repository
@Transactional
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

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

	public Long countById(Integer id);
	
	@Query("UPDATE User u Set u.enable = ?2 WHERE u.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enable);

	@Query("SELECT p FROM User p WHERE "
			+ "CONCAT(p.id, ' ', p.name, ' ', p.email) "
			+ "LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
}
