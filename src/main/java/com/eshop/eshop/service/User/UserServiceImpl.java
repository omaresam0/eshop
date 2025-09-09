package com.eshop.eshop.service.User;

import com.eshop.eshop.dto.UserDto;
import com.eshop.eshop.exception.AlreadyExistException;
import com.eshop.eshop.exception.ResourceNotFound;
import com.eshop.eshop.model.User;
import com.eshop.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import request.CreateUserRequest;
import request.UserUpdateRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
private final UserRepository userRepository;
private final ModelMapper modelMapper;
private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFound("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user->!userRepository.
                existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setFname(request.getFname());
                    user.setLName(request.getLName());
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return userRepository.save(user);
                }).orElseThrow(() -> new AlreadyExistException(request.getEmail() + " email already found"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFname(request.getFname());
            existingUser.setLName(request.getLName());
            return userRepository.save(existingUser);
        }).orElseThrow(()->new ResourceNotFound("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete, ()->{
            throw new ResourceNotFound("User not found");
        });

    }

    @Override
    public UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new ResourceNotFound("Authenticated user not found"));
    }


}
