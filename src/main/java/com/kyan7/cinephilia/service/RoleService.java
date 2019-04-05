package com.kyan7.cinephilia.service;

import com.kyan7.cinephilia.domain.models.service.RoleServiceModel;
import com.kyan7.cinephilia.domain.models.service.UserServiceModel;

import java.util.Set;

public interface RoleService {

    void seedRolesInDatabase();

    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);
}
