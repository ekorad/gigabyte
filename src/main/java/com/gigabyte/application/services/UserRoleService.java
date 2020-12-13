package com.gigabyte.application.services;

import java.util.Collection;
import java.util.List;

import com.gigabyte.application.helpers.ChecksEntity;
import com.gigabyte.application.models.UserRole;
import com.gigabyte.application.repos.UserRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService implements ChecksEntity<UserRole> {

    @Autowired
    private UserRoleRepository repo;

    public UserRole getRoleByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }

        return repo.findByName(name).orElse(null);
    }

    public List<UserRole> getAllRoles() {
        return repo.findAll();
    }

    public UserRole addRole(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (!isEntityValid(role)) {
            throw new IllegalArgumentException("Role is invalid (role name cannot be null or empty)");
        }

        return repo.save(role);
    }

    public List<UserRole> addAllRoles(Collection<UserRole> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("The collection of roles to be added cannot be null or empty");
        }
        if (roles.stream().anyMatch(this::isEntityValid)) {
            throw new IllegalArgumentException("Role is invalid (role name cannot be null or empty)");
        }

        return repo.saveAll(roles);
    }

    public void removeAllRoles() {
        repo.deleteAll();
    }

    public void removeRoleByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        repo.deleteByName(name);
    }

    @Override
    public boolean isEntityValid(UserRole role) {
        String name = role.getName();
        return (name != null && !name.isEmpty());
    }

}
