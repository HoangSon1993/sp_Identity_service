package com.sondev.identityservice.mapper;

import com.sondev.identityservice.dto.request.PermissionRequest;
import com.sondev.identityservice.dto.request.RoleRequest;
import com.sondev.identityservice.dto.response.PermissionResponse;
import com.sondev.identityservice.dto.response.RoleResponse;
import com.sondev.identityservice.entity.Permission;
import com.sondev.identityservice.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
