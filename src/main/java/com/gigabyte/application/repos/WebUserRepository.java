package com.gigabyte.application.repos;

import java.util.List;
import java.util.Optional;

import com.gigabyte.application.models.WebUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WebUserRepository extends JpaRepository<WebUser, Long> {

    @Query("SELECT u FROM WebUser u WHERE u.username = :username")
    public Optional<WebUser> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM WebUser u WHERE u.email = :email")
    public Optional<WebUser> findByEmail(@Param("email") String email);

    @Query("SELECT u FROM WebUser u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
    public List<WebUser> findAllByFirstName(@Param("firstName") String firstName);

    @Query("SELECT u FROM WebUser u WHERE LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    public List<WebUser> findAllByLastName(@Param("lastName") String lastName);

    @Query("SELECT u FROM WebUser u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) AND LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))")
    public List<WebUser> findAllByFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("DELETE FROM WebUser u WHERE u.username = :username")
    public void deleteByUsername(@Param("username") String username);

    @Query("DELETE FROM WebUser u WHERE u.email = :email")
    public void deleteByEmail(@Param("email") String email);
}
