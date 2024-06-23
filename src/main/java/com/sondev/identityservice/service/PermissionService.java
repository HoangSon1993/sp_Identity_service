package com.sondev.identityservice.service;

import com.sondev.identityservice.dto.request.PermissionRequest;
import com.sondev.identityservice.dto.response.PermissionResponse;
import com.sondev.identityservice.entity.Permission;
import com.sondev.identityservice.mapper.PermissionMapper;
import com.sondev.identityservice.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // tạo constructor cho tất cả các biến mà define là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Những field nào có access modify là default thì sẽ trở thành private, final
// (không ghi access modify thì là default)
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

   public PermissionResponse create (PermissionRequest request){
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

   public List<PermissionResponse> getAll(){
      var permission = permissionRepository.findAll();
      return permission.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permisson){
        permissionRepository.deleteById(permisson);
    }
}
