package com.sondev.identityservice.mapper;

import com.sondev.identityservice.dto.request.UserCreationRequest;
import com.sondev.identityservice.dto.request.UserUpdateRequest;
import com.sondev.identityservice.dto.response.UserResponse;
import com.sondev.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    //@Mapping(source = "firstName", target = "lastName") // Chỉ định map firstName cho lastName
    //@Mapping(target = "firstName", ignore = true) // Bỏ qua k mapping Fied firstName
//    UserResponse toUserResponse (User user);
    void  updateUser(@MappingTarget User user, UserUpdateRequest request);
}
