package com.hasanmo.dreamshops.service.user;

import com.hasanmo.dreamshops.dto.UserDto;
import com.hasanmo.dreamshops.model.User;
import com.hasanmo.dreamshops.request.CreateUserRequest;
import com.hasanmo.dreamshops.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getauthenticatedUser();
}
