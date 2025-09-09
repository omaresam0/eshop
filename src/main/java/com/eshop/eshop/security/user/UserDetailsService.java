package com.eshop.eshop.security.user;

import com.eshop.eshop.model.User;
import com.eshop.eshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(()->new UsernameNotFoundException("User not found"));
        return UserDetailsImpl.buildUserDetails(user);
    }
}
