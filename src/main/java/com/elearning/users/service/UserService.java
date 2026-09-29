package com.elearning.users.service;

import com.elearning.common.error.ForbiddenException;
import com.elearning.common.error.NotFoundException;
import com.elearning.users.dto.RoleUpdateRequest;
import com.elearning.users.dto.UserResponse;
import com.elearning.users.dto.UserUpdateRequest;
import com.elearning.users.entity.User;
import com.elearning.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserResponse getUserResponse(Long id) {
        return toResponse(getUserById(id));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = getUserById(id);
        if (request.name() != null) user.setName(request.name());
        if (request.avatarUrl() != null) user.setAvatarUrl(request.avatarUrl());
        if (request.bio() != null) user.setBio(request.bio());
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateRole(Long id, RoleUpdateRequest request) {
        User user = getUserById(id);
        user.setRole(User.Role.valueOf(request.role()));
        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(), user.getName(), user.getEmail(), user.getRole().name(),
            user.getEmailVerified(), user.getAvatarUrl(), user.getBio(), user.getCreatedAt()
        );
    }
}
