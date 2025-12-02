package com.ihb.mytalentbackend.repository;

import com.ihb.mytalentbackend.domain.Role;
import com.ihb.mytalentbackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository <User, Long> {
    User findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update User set role=:role where username=:username")
    int updateUserRole(@Param("username") String username, @Param("role") Role role);
}
