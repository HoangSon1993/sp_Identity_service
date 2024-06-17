package com.sondev.identityservice.service;

import com.sondev.identityservice.dto.request.UserCreationRequest;
import com.sondev.identityservice.dto.request.UserUpdateRequest;
import com.sondev.identityservice.dto.response.UserResponse;
import com.sondev.identityservice.entity.User;
import com.sondev.identityservice.enums.Role;
import com.sondev.identityservice.exception.AppException;
import com.sondev.identityservice.exception.ErrorCode;
import com.sondev.identityservice.mapper.UserMapper;
import com.sondev.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor // tạo constructor cho tất cả các biến mà define là final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Những field nào có access modify là default thì sẽ trở thành private, final
// (không ghi access modify thì là default)
@Slf4j
public class UserService {

    UserRepository userRepository; // Access modify là default sẽ chuyển thành private final
    UserMapper userMapper; // Access modify là default sẽ chuyển thành private final
    PasswordEncoder passwordEncoder;
    public User createUser(UserCreationRequest request){
//    User user = new User();

        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

//        // Ứng dụng builder
//        UserCreationRequest request1 = UserCreationRequest.builder()
//                .username("")
//                .firstName("")
//                .lastName("")
//                .build();

        User user = userMapper.toUser(request);
        //PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setLastName(request.getLastName());
//        user.setFirstName(request.getFirstName());
//        user.setDob(request.getDob());

        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {

        log.info("In method get User");

        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {

        log.info("In method get my info");

        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse getMyInfo () {
        var context = SecurityContextHolder.getContext();
     String name =   context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));

        userMapper.updateUser(user,request);
        // Đoạn code trên thay thế tất cả các dòng bên dưới


//        user.setPassword(request.getPassword());
//        user.setLastName(request.getLastName());
//        user.setFirstName(request.getFirstName());
//        user.setDob(request.getDob());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }
}
