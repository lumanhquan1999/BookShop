package com.springboot.BookShop.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.springboot.BookShop.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

	@Query("SELECT u FROM Role u WHERE u.name = ?1")
	Role findByName(String name);
}
