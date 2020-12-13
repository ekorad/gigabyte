package com.gigabyte.application.services;

import java.util.Collection;
import java.util.List;

import com.gigabyte.application.helpers.ChecksEntity;
import com.gigabyte.application.models.UserPermission;
import com.gigabyte.application.repos.UserPermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionService implements ChecksEntity<UserPermission> {
    
    @Autowired
    private UserPermissionRepository repo;

    public UserPermission getPermissionByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Permission name cannot be null or empty");
        }
        
        return repo.findByName(name).orElse(null);
    }

    public List<UserPermission> getAllPermissions() {
        return repo.findAll();
    }

    public UserPermission addPermission(UserPermission permission) {
        if (permission == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }
        if (isEntityInvalid(permission)) {
            throw new IllegalArgumentException("Permission is invalid (permission name cannot be null or empty)");
        }
        return repo.save(permission);
    }

    public List<UserPermission> addAllPermissions(Collection<UserPermission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            throw new IllegalArgumentException("The collection of permissions to be added cannot be null or empty");
        }
        if (permissions.stream().anyMatch(this::isEntityInvalid)) {
            throw new IllegalArgumentException("Permission is invalid (permission name cannot be null or empty)");
        }
        return repo.saveAll(permissions);
    }

    public void removePermissionByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Permission name cannot be null or empty");
        }

        repo.deleteByName(name);
    }

    public void removeAllPermissions() {
        repo.deleteAll();
    }

    @Override
    public boolean isEntityInvalid(UserPermission permission) {
        String name = permission.getName();
        return (name == null || name.isEmpty());
    }
}
