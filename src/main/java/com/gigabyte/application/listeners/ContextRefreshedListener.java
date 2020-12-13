package com.gigabyte.application.listeners;

import java.util.Arrays;
import java.util.HashSet;

import com.gigabyte.application.models.UserPermission;
import com.gigabyte.application.models.UserRole;
import com.gigabyte.application.models.WebUser;
import com.gigabyte.application.services.UserPermissionService;
import com.gigabyte.application.services.UserRoleService;
import com.gigabyte.application.services.WebUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserPermissionService userPermissionService;
    @Autowired
    private WebUserService userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        userService.removeAllUsers();
        userRoleService.removeAllRoles();
        userPermissionService.removeAllPermissions();

        UserPermission canRead = new UserPermission();
        canRead.setName("CAN_READ");
        UserPermission canModify = new UserPermission();
        canModify.setName("CAN_MODIFY");
        UserPermission canDelete = new UserPermission();
        canDelete.setName("CAN_DELETE");
        UserPermission canCreate = new UserPermission();
        canCreate.setName("CAN_CREATE");

        UserRole userRole = new UserRole();
        userRole.setName("USER");
        userRole.setPermissions(new HashSet<>(Arrays.asList(canRead)));
        UserRole adminRole = new UserRole();
        adminRole.setName("ADMIN");
        adminRole.setPermissions(new HashSet<>(Arrays.asList(canRead, canModify, canDelete, canCreate)));

        WebUser me = new WebUser();
        me.setEmail("vladzahiu28@gmail.com");
        me.setFirstName("Vlad-Gabriel");
        me.setLastName("Zahiu");
        me.setPassword("Caloriferul_1995");
        me.setRole(adminRole);
        me.setUsername("ekorad");
        WebUser user = new WebUser();
        user.setEmail("gogu.leustean@yahoo.com");
        user.setFirstName("Gogu");
        user.setLastName("Leustean");
        user.setPassword("parola");
        user.setRole(userRole);
        user.setUsername("goguleustean");

        userService.addAllUsers(Arrays.asList(me, user));
    }

}
