package com.ihb.mytalentbackend.repository;

import com.ihb.mytalentbackend.domain.User;
import com.ihb.mytalentbackend.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String userId);

    @Modifying
    @Query("update User u set u.role = :role where u.email = :email")
    int updateUserRole(@Param("email") String email, @Param("role") Role role);

    String Id(Long id);
}
