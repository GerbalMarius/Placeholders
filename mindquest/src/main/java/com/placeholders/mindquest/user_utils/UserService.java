package com.placeholders.mindquest.user_utils;


import com.placeholders.mindquest.role_utils.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository users;
    private RoleRepository roles;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository users, RoleRepository roles,  PasswordEncoder passwordEncoder){
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;

    }

    public void saveUser(UserDTO userDTO){
        User user = new User(userDTO.getEmail(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getFirstName(), userDTO.getLastName());


    }
}
