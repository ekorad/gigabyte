package com.gigabyte.application.repos;

import java.util.Optional;

import com.gigabyte.application.models.UserPermission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {
    
    @Query("SELECT p FROM UserPermission p WHERE p.name = :name")
    public Optional<UserPermission> findByName(@Param("name") String name);

    @Query("DELETE FROM UserPermission p WHERE p.name = :name")
    public void deleteByName(@Param("name") String name);
}
