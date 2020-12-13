package com.gigabyte.application.services;

import java.util.Collection;
import java.util.List;

import com.gigabyte.application.helpers.ChecksEntity;
import com.gigabyte.application.models.WebUser;
import com.gigabyte.application.repos.WebUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebUserService implements ChecksEntity<WebUser> {

    @Autowired
    private WebUserRepository repo;

    public WebUser getUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        return repo.findByUsername(username).orElse(null);
    }

    public WebUser getUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }

        return repo.findByEmail(email).orElse(null);
    }

    public List<WebUser> getAllUsers() {
        return repo.findAll();
    }

    public List<WebUser> getAllUsersByFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("User first name cannot be null or empty");
        }

        return repo.findAllByFirstName(firstName);
    }

    public List<WebUser> getAllUsersByLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("User last name cannot be null or empty");
        }

        return repo.findAllByLastName(lastName);
    }

    public List<WebUser> getAllUsersByFullName(String firstName, String lastName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("User first name cannot be null or empty");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("User last name cannot be null or empty");
        }

        return repo.findAllByFullName(firstName, lastName);
    }

    public WebUser addUser(WebUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (isEntityInvalid(user)) {
            throw new IllegalArgumentException("User is invalid (one or more of the required fields is either null or empty)");
        }

        return repo.save(user);
    }

    public List<WebUser> addAllUsers(Collection<WebUser> users) {
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("The collection of users to be added cannot be null or empty");
        }
        if (users.stream().anyMatch(this::isEntityInvalid)) {
            throw new IllegalArgumentException("User is invalid (one or more of the required fields is either null or empty)");
        }
        
        return repo.saveAll(users);
    }

    public void removeAllUsers() {
        repo.deleteAll();
    }

    public void removeUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        repo.deleteByUsername(username);
    }

    public void removeUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }

        repo.deleteByEmail(email);
    }

    @Override
    public boolean isEntityInvalid(WebUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String fName = user.getFirstName();
        String lName = user.getLastName();
        String email = user.getEmail();

        return (username == null || username.isEmpty()
        || password == null || password.isEmpty()
        || fName == null || fName.isEmpty()
        || lName == null || lName.isEmpty()
        || email == null || email.isEmpty());
    }
    
}
