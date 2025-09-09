package com.eshop.eshop.service.User;
import com.eshop.eshop.dto.UserDto;
import com.eshop.eshop.model.User;
import request.CreateUserRequest;
import request.UserUpdateRequest;

public interface UserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticatedUser();
}
