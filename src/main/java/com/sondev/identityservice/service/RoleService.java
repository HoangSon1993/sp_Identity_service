package com.sondev.identityservice.service;

import com.sondev.identityservice.dto.request.PermissionRequest;
import com.sondev.identityservice.dto.request.RoleRequest;
import com.sondev.identityservice.dto.response.PermissionResponse;
import com.sondev.identityservice.dto.response.RoleResponse;
import com.sondev.identityservice.entity.Permission;
import com.sondev.identityservice.mapper.PermissionMapper;
import com.sondev.identityservice.mapper.RoleMapper;
import com.sondev.identityservice.repository.PermissionRepository;
import com.sondev.identityservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor // tạo constructor cho tất cả các biến mà define là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// Những field nào có access modify là default thì sẽ trở thành private, final
// (không ghi access modify thì là default)
@Slf4j
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
        // có thể viết gọp lại
    }

    public void delete(String role){
        roleRepository.deleteById(role);
    }
}
