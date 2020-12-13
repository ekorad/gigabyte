package com.gigabyte.application.repos;

import java.util.Optional;

import com.gigabyte.application.models.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT r FROM UserRole r WHERE r.name = :name")
    public Optional<UserRole> findByName(@Param("name") String name);

    @Query("DELETE FROM UserRole r WHERE r.name = :name")
    public void deleteByName(@Param("name") String name);

}
